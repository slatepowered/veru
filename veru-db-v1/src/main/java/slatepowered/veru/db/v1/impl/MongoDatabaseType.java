package slatepowered.veru.db.v1.impl;

import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClient;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClients;
import org.bson.UuidRepresentation;
import slatepowered.veru.data.Values;
import slatepowered.veru.db.v1.Database;
import slatepowered.veru.db.v1.DatabaseType;
import slatepowered.veru.db.v1.Login;

import java.util.logging.Level;

public class MongoDatabaseType extends DatabaseType<MongoDatabase> {

    public static final MongoDatabaseType INSTANCE = new MongoDatabaseType();
    public static final String ID = "mongodb";

    public MongoDatabaseType() {
        super(ID);

        // set mongo logging level
        java.util.logging.Logger logger = java.util.logging.Logger.getLogger("org.mongodb.driver");
        logger.setLevel(Level.OFF);
    }

    @Override
    protected void login(MongoDatabase database, Login login) {
        try {
            if (!(login instanceof Login.URILogin))
                throw new IllegalArgumentException("login must be a URILogin");
            Login.URILogin ul = (Login.URILogin) login;

            // create connection string
            // and client settings
            ConnectionString connectionString = new ConnectionString(ul.getURI());
            MongoClientSettings settings = MongoClientSettings.builder()
                    .uuidRepresentation(UuidRepresentation.STANDARD)
                    .applyConnectionString(connectionString)
                    .build();

            // login to client
            MongoClient mongoClient = MongoClients.create(settings);
            database.client = mongoClient;

            // get database
            database.db = mongoClient.getDatabase(ul.getDatabase());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void close(MongoDatabase database) {
        database.client.close();
    }

    @Override
    protected void putEnv(Database db, Values values) {

    }

}
