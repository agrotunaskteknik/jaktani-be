package com.cartas.jaktani.repository;

import com.cartas.jaktani.model.CartCache;
import com.cartas.jaktani.model.MultiCartCache;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MultiCartCacheRepository extends CrudRepository<MultiCartCache, String> {}
