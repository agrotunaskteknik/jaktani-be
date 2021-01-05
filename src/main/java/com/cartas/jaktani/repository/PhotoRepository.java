package com.cartas.jaktani.repository;

import com.cartas.jaktani.model.Document;
import com.cartas.jaktani.model.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Integer> {
    List<Photo> findAllByRefferenceId(Integer refferenceId);
    List<Photo> findAllByUrlPath(String urlPath);
    List<Photo> findAllByRefferenceIdAndStatusIsNot(Integer refferenceId, Integer status);

}
