package com.cartas.jaktani.repository;

import com.cartas.jaktani.model.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TypeRepository extends JpaRepository<Type, Integer> {
	List<Type> findAllTypeByAndStatusIsNot(Integer status);
	List<Type> findAllByTypeGroupIdAndStatusIsNot(Integer typeGroupId, Integer status);
	Optional<Type> findByIdAndStatusIsNot(Integer id, Integer status);
	Optional<Type> findFirstByNameAndTypeGroupIdAndIdIsNotAndStatusIsNot(String name, Integer typeGroupId, Integer id, Integer status);
	Optional<Type> findFirstByNameAndTypeGroupIdAndStatusIsNot(String name, Integer typeGroupId, Integer status);
}
