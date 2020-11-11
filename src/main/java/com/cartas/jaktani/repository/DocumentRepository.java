package com.cartas.jaktani.repository;

import com.cartas.jaktani.model.Document;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Integer> {
	Optional<Document> findByIdAndStatusIsNot(Integer id, Integer status);
	Optional<Document> findByRefferenceIdAndTypeAndStatusIsNot(Integer refferenceId, Integer type, Integer status);
	List<Document> findAllByRefferenceIdAndTypeAndStatusIsNot(Integer refferenceId, Integer type, Integer status);
	List<Document> findAllByRefferenceIdAndTypeAndStatusIsNotOrderByOrderNumberAsc(Integer refferenceId, Integer type, Integer status);
	List<Document> findAllByRefferenceIdAndTypeAndCodeAndStatusIsNotOrderByOrderNumberAsc(Integer refferenceId, Integer type, Integer code, Integer status);
	List<Document> findAllByTypeAndStatusIsNotOrderByOrderNumberAsc(Integer type, Integer status);
	Document findFirstByRefferenceIdAndTypeAndOrderNumberAndStatusIsNot(Integer refferenceId, Integer type, Integer orderNumber, Integer status);
}
