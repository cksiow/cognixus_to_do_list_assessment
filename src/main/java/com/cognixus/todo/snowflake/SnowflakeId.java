package com.cognixus.todo.snowflake;

/**
 * Snow flake id assignment interface
 *
 * @param <T>
 */
public interface SnowflakeId<T> {

    void setId(T id);

    T getId();
}
