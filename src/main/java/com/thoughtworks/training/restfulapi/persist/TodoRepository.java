package com.thoughtworks.training.restfulapi.persist;

import com.thoughtworks.training.restfulapi.model.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {
    List<Todo> findAllByNameContaining(String name);
}
