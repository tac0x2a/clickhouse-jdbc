package net.tac42.clickhouse;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class ClickHouseConnectionTest {
    @Test
    public void testGetSetCatalog() throws SQLException {
        ClickHouseDataSource dataSource = new ClickHouseDataSource(
                "jdbc:clickhouse://localhost:8123/default?option1=one%20two&option2=y");
        String[] dbNames = new String[]{"get_set_catalog_test1", "get_set_catalog_test2"};
        try {
            ClickHouseConnectionImpl connection = (ClickHouseConnectionImpl) dataSource.getConnection();
            assertEquals(connection.getUrl(), "jdbc:clickhouse://localhost:8123/default?option1=one%20two&option2=y");
            assertEquals(connection.getCatalog(), "default");
            assertEquals(connection.getProperties().getDatabase(), "default");

            for (String db : dbNames) {
                connection.createStatement().executeUpdate("CREATE DATABASE " + db);
                connection.createStatement().executeUpdate(
                        "CREATE TABLE " + db + ".some_table ENGINE = TinyLog()"
                                + " AS SELECT 'value_" + db + "' AS field");

                connection.setCatalog(db);
                assertEquals(connection.getCatalog(), db);
                assertEquals(connection.getProperties().getDatabase(), db);
                assertEquals(connection.getUrl(),
                        "jdbc:clickhouse://localhost:8123/" + db + "?option1=one%20two&option2=y");

                ResultSet resultSet = connection.createStatement().executeQuery("SELECT field FROM some_table");
                assertTrue(resultSet.next());
                assertEquals(resultSet.getString(1), "value_" + db);
            }
        } finally {
            Connection connection = dataSource.getConnection();
            for (String db : dbNames) {
                connection.createStatement().executeUpdate("DROP DATABASE IF EXISTS " + db);
            }
        }
    }

    @Test
    public void testSetCatalogAndStatements() throws SQLException {
        ClickHouseDataSource dataSource = new ClickHouseDataSource(
                "jdbc:clickhouse://localhost:8123/default?option1=one%20two&option2=y");
        ClickHouseConnectionImpl connection = (ClickHouseConnectionImpl) dataSource.getConnection();
        final String sql = "SELECT currentDatabase()";

        connection.setCatalog("system");
        Statement statement = connection.createStatement();
        connection.setCatalog("default");
        ResultSet resultSet = statement.executeQuery(sql);
        resultSet.next();
        assertEquals(resultSet.getString(1), "system");

        statement = connection.createStatement();
        resultSet = statement.executeQuery(sql);
        resultSet.next();
        assertEquals(resultSet.getString(1), "default");
    }

    @Test
    public void testSetCatalogAndPreparedStatements() throws SQLException {
        ClickHouseDataSource dataSource = new ClickHouseDataSource(
                "jdbc:clickhouse://localhost:8123/default?option1=one%20two&option2=y");
        ClickHouseConnectionImpl connection = (ClickHouseConnectionImpl) dataSource.getConnection();
        final String sql = "SELECT currentDatabase() FROM system.tables WHERE name = ? LIMIT 1";

        connection.setCatalog("system");
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, "tables");
        connection.setCatalog("default");
        ResultSet resultSet = statement.executeQuery();
        resultSet.next();
        assertEquals(resultSet.getString(1), "system");

        statement = connection.prepareStatement(sql);
        statement.setString(1, "tables");
        resultSet = statement.executeQuery();
        resultSet.next();
        assertEquals(resultSet.getString(1), "default");
    }
}
