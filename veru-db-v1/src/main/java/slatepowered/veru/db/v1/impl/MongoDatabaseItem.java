package slatepowered.veru.db.v1.impl;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.conversions.Bson;
import slatepowered.veru.db.v1.Database;
import slatepowered.veru.db.v1.DatabaseItem;

import java.util.HashMap;
import java.util.Map;

public class MongoDatabaseItem extends DatabaseItem {

    /**
     * The primary key value.
     */
    Object key;

    /**
     * The collection this item is stored in.
     */
    MongoCollection<Document> collection;

    /**
     * The primary key value name.
     */
    String keyName;

    // the changes to be applied
    HashMap<String, Object> changes = new HashMap<>();
    // the document as currently stored
    Document document = new Document();

    public MongoDatabaseItem(Database database,
                             String keyName,
                             MongoCollection<Document> collection,
                             Object key) {
        super(database);
        this.keyName = keyName;
        this.collection = collection;
        this.key = key;
    }

    public Bson createFilter() {
        return Filters.eq(keyName, key);
    }

    @Override
    public Object key() {
        return key;
    }

    @Override
    public void set(String key, Object val) {
        changes.put(key, val);
        if (document != null)
            document.put(key, val);
    }

    @Override
    public <T> T get(String key, Class<T> type) {
        return document.get(key, type);
    }

    @Override
    public MongoDatabaseItem push() {
        // construct update
//        Bson[] bsons = new Bson[changes.size()];
//        int i = 0;
//        for (Map.Entry<String, Object> entry : changes.entrySet()) {
//            bsons[i] = Updates.set(entry.getKey(), entry.getValue());
//            i++;
//        }

        Bson[] bsons = new Bson[document.size()];
        int i = 0;
        for (Map.Entry<String, Object> entry : document.entrySet()) {
            bsons[i] = Updates.set(entry.getKey(), entry.getValue());
            i++;
        }

        Bson update = Updates.combine(bsons);
        changes.clear();

        // execute updates
        collection.updateOne(createFilter(), update, new UpdateOptions().upsert(true));
        return this;
    }

    @Override
    public MongoDatabaseItem pull() {
        document = collection.find(createFilter()).first();
        return this;
    }

    @Override
    public boolean available() {
        return document != null;
    }

    public Document document() {
        return document;
    }

}
