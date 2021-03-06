/*
 * Copyright (c) 2015-2016, Pradeeban Kathiravelu and others. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.emory.bmi.datacafe.rest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static spark.Spark.port;

/**
 * Exposing the server and client APIs of Data Cafe..
 */
public class DatacafeEngine {
    private static Logger logger = LogManager.getLogger(DatacafeEngine.class.getName());

    public static void main(String[] args) {

        logger.info("Initializing the Data Cafe RESTful Interfaces");
        port(DatacafeAPIConstants.REST_PORT);
        DataLakeManager.initialize();
    }
}
