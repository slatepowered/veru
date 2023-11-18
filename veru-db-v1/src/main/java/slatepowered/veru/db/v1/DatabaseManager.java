package slatepowered.veru.db.v1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;

/**
 * Database management.
 */
@SuppressWarnings("rawtypes")
public class DatabaseManager {

    // databases
    final ArrayList<Database>       db = new ArrayList<>();
    final HashMap<String, Database> dbByName = new HashMap<>();

    // database types
    final ArrayList<DatabaseType<Database>>           dbt = new ArrayList<>();
    final HashMap<String, DatabaseType<Database>> dbtById = new HashMap<>();

    /**
     * Get a list of all created databases.
     *
     * @return The created databases.
     */
    public List<Database> databases() {
        return Collections.unmodifiableList(db);
    }

    /**
     * Get a list of all registered database types.
     *
     * @return The database types.
     */
    public List<DatabaseType<Database>> types() {
        return Collections.unmodifiableList(dbt);
    }

    /**
     * Get a created database by name.
     *
     * @param name The name.
     * @param <D> The database type.
     * @return The database or null if absent.
     */
    @SuppressWarnings("unchecked")
    public <D extends Database> D getDatabase(String name) {
        return (D) dbByName.get(name);
    }

    /**
     * Register a pre-constructed database to the manager.
     *
     * @param database The database.
     * @return This.
     */
    public DatabaseManager addDatabase(Database database) {
        db.add(database);
        dbByName.put(database.name, database);
        return this;
    }

    public <D extends Database> DatabaseManager addDatabase(
            String name,
            BiFunction<DatabaseManager, String, D> constructor,
            Consumer<D> consumer
    ) {
        D db = constructor.apply(this, name);
        addDatabase(db);
        if (consumer != null)
            consumer.accept(db);
        return this;
    }

    public DatabaseManager addType(DatabaseType type) {
        dbt.add(type);
        dbtById.put(type.id, type);
        return this;
    }

    /**
     * Get the global query pool.
     *
     * @return The query pool.
     */
    public QueryPool queryPool() {
        return new QueryPool(null);
    }

}
