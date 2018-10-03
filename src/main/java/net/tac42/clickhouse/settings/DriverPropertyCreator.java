package net.tac42.clickhouse.settings;

import java.sql.DriverPropertyInfo;
import java.util.Properties;

public interface DriverPropertyCreator {
    DriverPropertyInfo createDriverPropertyInfo(Properties properties);
}
