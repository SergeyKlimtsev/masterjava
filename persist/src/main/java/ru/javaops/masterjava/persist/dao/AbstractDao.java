package ru.javaops.masterjava.persist.dao;

/**
 * gkislin
 * 27.10.2016
 * <p>
 * <p>
 */
public interface AbstractDao<T> {
    void clean();

    T insert(T o);
}
