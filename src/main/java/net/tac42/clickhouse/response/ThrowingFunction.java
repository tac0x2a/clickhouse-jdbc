package net.tac42.clickhouse.response;

public interface ThrowingFunction<F,R> {
    R apply(F f) throws Exception;
}