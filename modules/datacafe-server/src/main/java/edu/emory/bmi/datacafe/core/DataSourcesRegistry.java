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
package edu.emory.bmi.datacafe.core;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * A minimalistic representation of a data source
 */
public class DataSourcesRegistry {
    private static List<String> fullNames;

    public static void init() {
        fullNames = new ArrayList<>();
    }

    public static void addDataSource(String database, String collection) {
        fullNames.add(constructFullDataSourceName(database, collection));
    }

    public static String[] getFullNamesAsArray() {
        String[] fullNameArray = new String[fullNames.size()];

        for (int i = 0; i < fullNames.size(); i++) {
            fullNameArray[i] = fullNames.get(i);
        }
        return fullNameArray;
    }

    /**
     * Constructs full name for the data stores in data lake
     *
     * @param data data elements
     * @return fullName
     */
    protected static String constructFullDataSourceName(String... data) {
        String fullName = "";
        if (data.length > 0) {
            for (String element : data) {
                if (!fullName.equals("")) {
                    fullName += "_";
                }
                fullName += element;
            }
        } else {
            fullName = UUID.randomUUID().toString();
        }
        return fullName;
    }
}