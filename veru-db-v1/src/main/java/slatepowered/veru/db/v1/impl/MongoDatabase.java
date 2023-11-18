package slatepowered.veru.db.v1.impl;

import com.mongodb.client.MongoClient;
import slatepowered.veru.db.v1.Database;
import slatepowered.veru.db.v1.DatabaseManager;

public class MongoDatabase extends Database {

    public MongoDatabase(DatabaseManager manager, String name) {
        super(manager, name, MongoDatabaseType.INSTANCE);
    }

    // mongo client
    protected MongoClient client;
    // mongo database client
    protected com.mongodb.client.MongoDatabase db;

    public MongoClient getClient() {
        return client;
    }

    public com.mongodb.client.MongoDatabase getDatabaseClient() {
        return db;
    }

    @Override
    public boolean isOpen() {
        return db != null;
    }

}
