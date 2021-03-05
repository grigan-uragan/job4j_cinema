package ru.job4j.store;

import java.util.List;

public interface Store<T> {

    int save(T element);

    List<T> findAll();

    T findById(int id);
}
