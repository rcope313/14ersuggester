package database.dao;

import java.sql.Connection;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public abstract class Dao {
    final Connection conn;

    public Dao(Connection conn) {
        this.conn = conn;
    }



}
