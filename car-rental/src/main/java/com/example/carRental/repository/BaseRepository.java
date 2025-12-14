package com.example.carRental.repository;

import com.example.carRental.models.BaseEntity;

import java.util.List;
import java.util.Optional;

public interface BaseRepository<T extends BaseEntity> {

    T create(T entity);

    Optional<T> findById(String id);

    T update(T entity);

    List<T> getAll();
}
