package slatepowered.veru.db.v1;

import slatepowered.veru.data.Values;

public abstract class DatabaseType<D extends Database> {

    protected final String id;

    public DatabaseType(String id) {
        this.id = id;
    }

    public String getString() {
        return id;
    }

    /* Connections */

    protected abstract void login(D database, Login login);
    protected abstract void close(D database);

    /* Queries */

    protected abstract void putEnv(Database db, Values values);

}
