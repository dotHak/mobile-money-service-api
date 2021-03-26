package com.hubert.momoservice.service;

import java.util.List;
import java.util.Optional;

public interface GenericService<T, ID> {

    public List<T> getAll();

    public Optional<T> getOne(ID id);

    public T save(T t);

}
