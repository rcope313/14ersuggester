package database.dao;

import java.sql.Connection;

public abstract class Dao {
    final Connection conn;

    public Dao(Connection conn) {
        this.conn = conn;
    }

}
