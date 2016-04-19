/*
 * Copyright (c) 2015 Pradeeban Kathiravelu and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package edu.emory.bmi.datacafe.mongo;

import edu.emory.bmi.datacafe.constants.MongoConstants;
import edu.emory.bmi.datacafe.core.AbstractExecutorEngine;
import edu.emory.bmi.datacafe.core.DataSource;
import edu.emory.bmi.datacafe.core.DataSourceBean;
import edu.emory.bmi.datacafe.hdfs.HdfsConnector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jongo.MongoCursor;

import java.util.*;

/**
 * The core Data Cafe Executor Engine.
 */
public class ExecutorEngine extends AbstractExecutorEngine {
    private static Logger logger = LogManager.getLogger(ExecutorEngine.class.getName());
    private static Map<Class<? extends DataSourceBean>, MongoCursor> mongoCursorMap;

    private static List[] dataSourceBeans;
    private static int autoInc = 0;

    public ExecutorEngine(Map<Class<? extends DataSourceBean>, DataSource> dataSources) {
        super(dataSources);
        mongoCursorMap = new HashMap<>();
        initializeDatasourceNames();
    }

    private void initializeDatasourceNames() {
        MongoEngine dataSourceEngine = new MongoEngine();
        for (Class<? extends DataSourceBean> dataSourceBean : dataSourceMap.keySet()) {
            DataSource dataSource = dataSourceMap.get(dataSourceBean);
            dataSourceEngine.addDataSource(dataSource);
            datasourceNames.put(dataSourceBean, dataSource.getFullName());
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Data source names are initialized");
        }
    }

    /**
     * Retrieve pointers to the interested data source IDs. Map: DataSourceBean -> idConstraints
     *
     * @param idConstraintsMap Queries for data source ids.
     */
    public void retrieveDataLakeIDs(Map<Class<? extends DataSourceBean>, String> idConstraintsMap) {
        // Get the IDs
        for (Class<? extends DataSourceBean> clazz : idConstraintsMap.keySet()) {
            MongoCursor tempCursors = MongoEngine.initializeEntry(
                    dataSourceMap.get(clazz).getDatabase(), dataSourceMap.get(clazz).getCollection(),
                    idConstraintsMap.get(clazz), clazz);
            mongoCursorMap.put(clazz, tempCursors);
        }
        dataSourceBeans = new List[mongoCursorMap.size()];
    }

    /**
     * Called once per each of the data sources
     * @param clazz the class param
     * @param queryConstructor query constructor
     */
    public void createList(Class clazz, QueryConstructor queryConstructor) {
        List retrievedDataList = new ArrayList<>();

        for (Object obj_ : mongoCursorMap.get(clazz)) {
            DataSourceBean minimalObj = (DataSourceBean) obj_;
            DataSourceBean tempObject = (DataSourceBean) MongoEngine.findEntry(queryConstructor.getDatabase(),
                    queryConstructor.getCollection(), "{" + MongoConstants.ID_ATTRIBUTE + ":'" +
                            minimalObj.getKey() + "'}, " + queryConstructor.getConstraints(), clazz
            );
            retrievedDataList.add(tempObject);
        }
        dataSourceBeans[autoInc++] = retrievedDataList;
    }


    /**
     * Construct the data lake
     */
    public void constructDataLake(String[][] interestedAttributes) {
        String[] queries = MongoConnector.constructQueries(interestedAttributes);

        Collection<String> values = datasourceNames.values();
        String[] tempDataSourceNames = values.toArray(new String[values.size()]);

        if (logger.isDebugEnabled()) {
            for (int i = 0; i < tempDataSourceNames.length; i++) {
                logger.debug(i + ": " + tempDataSourceNames[i]);
                logger.debug(queries[i]);
                dataSourceBeans[i].get(i);
                for (int j = 0; j < interestedAttributes[i].length; j++) {
                    logger.debug(interestedAttributes[i][j]);
                }
            }
        }

        HdfsConnector.writeDataSourcesToWarehouse(tempDataSourceNames, interestedAttributes, queries, dataSourceBeans);
    }
}