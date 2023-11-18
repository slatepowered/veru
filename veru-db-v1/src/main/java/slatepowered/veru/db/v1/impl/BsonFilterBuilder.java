package slatepowered.veru.db.v1.impl;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.TextSearchOptions;
import org.bson.BsonDocument;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;

public class BsonFilterBuilder {

    // the BSON objects
    List<Bson> bson = new ArrayList<>();

    public Bson build() {
        if (bson.size() == 0)
            return new BsonDocument();
        Bson current = bson.get(0);
        int l = bson.size();
        for (int i = 1; i < l; i++)
            current = Filters.and(current, bson.get(i));
        return current;
    }

    public BsonFilterBuilder with(Bson filter) {
        bson.add(filter);
        return this;
    }

    public BsonFilterBuilder eq(String field, Object value) {
        return with(Filters.eq(field, value));
    }

    public BsonFilterBuilder in(String field, Object... values) {
        return with(Filters.in(field, values));
    }

    public BsonFilterBuilder search(String text) {
        return search(text, new TextSearchOptions().caseSensitive(false));
    }

    public BsonFilterBuilder search(String text, TextSearchOptions options) {
        return with(Filters.text(text, options));
    }

}
