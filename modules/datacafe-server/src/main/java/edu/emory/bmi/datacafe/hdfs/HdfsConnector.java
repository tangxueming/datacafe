/*
 * Copyright (c) 2015 Pradeeban Kathiravelu and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package edu.emory.bmi.datacafe.hdfs;

import edu.emory.bmi.datacafe.constants.DatacafeConstants;
import edu.emory.bmi.datacafe.constants.HDFSConstants;
import edu.emory.bmi.datacafe.core.CoreDataObject;
import edu.emory.bmi.datacafe.core.WarehouseConnector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Connecting to HDFS through Datacafe.
 */
public class HdfsConnector implements WarehouseConnector {
    private static Logger logger = LogManager.getLogger(HdfsConnector.class.getName());

    /**
     * Writes the data sources to Hive
     *
     * @param datasourceNames names of the data sources
     * @param params          parameters of the data sources as a 2-d array - an array for each of the data source
     * @param queries         queries for each of the data source.
     * @param writables       array of lists for each data sources to be written to the data warehouse.
     */
    public static void writeDataSourcesToWarehouse(String[] datasourceNames, String[][] params,
                                                   String[] queries, List<?>[] writables) {
        List<String>[] texts = new ArrayList[writables.length];

        for (int i = 0; i < writables.length; i++) {
            texts[i] = CoreDataObject.getWritableString(params[i], writables[i]);
        }

        WarehouseConnector warehouseConnector = new HdfsConnector();
        warehouseConnector.writeToWarehouse(datasourceNames, texts, queries);
    }


    @Override
    public void writeToWarehouse(String[] datasourcesNames, List<String>[] texts, String[] queries) {
        for (int i = 0; i < DatacafeConstants.NUMBER_OF_COMPOSING_DATA_SOURCES; i++) {
            createFile(datasourcesNames[i], texts[i]);

            HadoopConnector.writeToHDFS(datasourcesNames[i]);
            logger.info("Successfully written the data to the warehouse: " + datasourcesNames[i]);

        }
    }

    @Override
    public void createFile(String fileName, List<String> lines) {

        Charset utf8 = StandardCharsets.UTF_8;
        String file = fileName;
        try {

            if (DatacafeConstants.IS_APPEND) {
                Files.write(Paths.get(file), lines, utf8, StandardOpenOption.CREATE,
                        StandardOpenOption.APPEND
                );
            } else {
                Files.write(Paths.get(file), lines, utf8, StandardOpenOption.CREATE,
                        StandardOpenOption.TRUNCATE_EXISTING);
            }
            logger.info("Successfully written the output to the file, " + file);
        } catch (IOException e) {
            logger.error("Error in creating the warehouse file: " + file, e);
        }
        if (DatacafeConstants.IS_REMOTE_SERVER) {
            FileRemoteManager.copyFile(file);
        }
    }

    /**
     * Writes to Hive
     * @param csvFileName, file to be written to Hive
     * @param hiveTable, the table name in Hive
     * @param query, query to execute
     * @throws SQLException, if execution failed.
     */
    public void writeToHive(String csvFileName, String hiveTable, String query)  throws SQLException {
        try {
            Class.forName(HDFSConstants.DRIVER_NAME);
        } catch (ClassNotFoundException e) {
            logger.error("Exception in finding the driver", e);
        }

        Connection con = DriverManager.getConnection(HDFSConstants.HIVE_CONNECTION_URI, HDFSConstants.HIVE_USER_NAME,
                HDFSConstants.HIVE_PASSWORD);
        Statement stmt = con.createStatement();

        stmt.execute("drop table if exists " + hiveTable);

        stmt.execute("create table " + hiveTable+ query);

        String csvFilePath = HDFSConstants.HIVE_CSV_DIR + csvFileName;

        String sql = "load data local inpath '" + csvFilePath + "' into table " + hiveTable;
        stmt.execute(sql);
    }
}