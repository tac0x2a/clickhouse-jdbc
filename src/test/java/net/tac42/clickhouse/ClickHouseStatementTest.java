package net.tac42.clickhouse;


import com.google.common.collect.ImmutableMap;
import org.apache.http.impl.client.HttpClientBuilder;
import org.testng.annotations.Test;
import net.tac42.clickhouse.settings.ClickHouseProperties;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.Properties;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;


public class ClickHouseStatementTest {
    @Test
    public void testClickhousify() throws Exception {
        String sql = "SELECT ololo FROM ololoed;";
        assertEquals("SELECT ololo FROM ololoed FORMAT TabSeparatedWithNamesAndTypes;", ClickHouseStatementImpl.clickhousifySql(sql));

        String sql2 = "SELECT ololo FROM ololoed";
        assertEquals("SELECT ololo FROM ololoed FORMAT TabSeparatedWithNamesAndTypes;", ClickHouseStatementImpl.clickhousifySql(sql2));

        String sql3 = "SELECT ololo FROM ololoed FORMAT TabSeparatedWithNamesAndTypes";
        assertEquals("SELECT ololo FROM ololoed FORMAT TabSeparatedWithNamesAndTypes", ClickHouseStatementImpl.clickhousifySql(sql3));

        String sql4 = "SELECT ololo FROM ololoed FORMAT TabSeparatedWithNamesAndTypes;";
        assertEquals("SELECT ololo FROM ololoed FORMAT TabSeparatedWithNamesAndTypes;", ClickHouseStatementImpl.clickhousifySql(sql4));

        String sql5 = "SHOW ololo FROM ololoed;";
        assertEquals("SHOW ololo FROM ololoed FORMAT TabSeparatedWithNamesAndTypes;", ClickHouseStatementImpl.clickhousifySql(sql5));

        String sql6 = " show ololo FROM ololoed;";
        assertEquals("show ololo FROM ololoed FORMAT TabSeparatedWithNamesAndTypes;", ClickHouseStatementImpl.clickhousifySql(sql6));
    }

    @Test
    public void testCredentials() throws SQLException, URISyntaxException {
        ClickHouseProperties properties = new ClickHouseProperties(new Properties());
        ClickHouseProperties withCredentials = properties.withCredentials("test_user", "test_password");
        assertTrue(withCredentials != properties);
        assertNull(properties.getUser());
        assertNull(properties.getPassword());
        assertEquals(withCredentials.getUser(), "test_user");
        assertEquals(withCredentials.getPassword(), "test_password");

        ClickHouseStatementImpl statement = new ClickHouseStatementImpl(
                HttpClientBuilder.create().build(),null, withCredentials
                );

        URI uri = statement.buildRequestUri(null, null, null, null, false);
        String query = uri.getQuery();
        assertTrue(query.contains("password=test_password"));
        assertTrue(query.contains("user=test_user"));
    }

    @Test
    public void testMaxMemoryUsage() throws Exception {
        ClickHouseProperties properties = new ClickHouseProperties();
        properties.setMaxMemoryUsage(41L);
        ClickHouseStatementImpl statement = new ClickHouseStatementImpl(HttpClientBuilder.create().build(), null,
                properties);

        URI uri = statement.buildRequestUri(null, null, null, null, false);
        String query = uri.getQuery();
        assertTrue(query.contains("max_memory_usage=41"), "max_memory_usage param is missing in URL");
    }

    @Test
    public void testAdditionalRequestParams() throws Exception {
        ClickHouseProperties properties = new ClickHouseProperties();
        ClickHouseStatementImpl statement = new ClickHouseStatementImpl(
                HttpClientBuilder.create().build(),
                null,
                properties
        );

        URI uri = statement.buildRequestUri(
                null,
                null,
                null,
                ImmutableMap.of("cache_namespace", "aaaa"),
                false
        );
        String query = uri.getQuery();
        assertTrue(query.contains("cache_namespace=aaaa"), "cache_namespace param is missing in URL");
    }
}
