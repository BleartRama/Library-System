package util;

import java.sql.SQLException;
import java.util.List;

public interface Searchable<T> {
    List<T> findAll() throws SQLException;
}