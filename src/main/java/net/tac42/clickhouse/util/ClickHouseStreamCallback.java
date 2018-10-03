package net.tac42.clickhouse.util;

import java.io.IOException;

/**
 * @author Dmitry Andreev <a href="mailto:AndreevDm@yandex-team.ru"></a>
 */
public interface ClickHouseStreamCallback {
    void writeTo(ClickHouseRowBinaryStream stream) throws IOException;
}
