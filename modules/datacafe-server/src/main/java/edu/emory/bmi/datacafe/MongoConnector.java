/*
 * Title:        S²DN 
 * Description:  Orchestration Middleware for Incremental
 *               Development of Software-Defined Cloud Networks.
 * Licence:      Eclipse Public License - v 1.0 - https://www.eclipse.org/legal/epl-v10.html
 *
 * Copyright (c) 2015, Pradeeban Kathiravelu <pradeeban.kathiravelu@tecnico.ulisboa.pt>
 */
package edu.emory.bmi.datacafe;

import com.mongodb.*;

/**
 * Connects to the Mongo database
 */
public class MongoConnector {

    private static final MongoClient mongoClient = new MongoClient(new ServerAddress(DatacafeConstants.MONGO_CLIENT_HOST,
            DatacafeConstants.MONGO_CLIENT_PORT));

    /**
     * Gets cursor for a collection in a given database.
     * @param database the data base
     * @param collection the collection in the data base
     * @return a cursor that can be iterated.
     */
    public DBCursor getCursor(String database, String collection) {
        DB db = mongoClient.getDB(database);
        DBCollection dbCollection = db.getCollection(collection);
        return dbCollection.find();
    }
}
