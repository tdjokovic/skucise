package com.example.skucise.repositories.interfaces;

import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CRUDRepository<T,ID> {
    List<T> getAll();

    boolean create(T t);

    T get(ID id);

    boolean update(T t, ID id);

    boolean delete(ID id);
}
