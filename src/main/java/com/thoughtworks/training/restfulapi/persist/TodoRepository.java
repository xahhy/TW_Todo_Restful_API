package com.thoughtworks.training.restfulapi.persist;

import com.thoughtworks.training.restfulapi.model.Tag;
import com.thoughtworks.training.restfulapi.model.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {
    Page<Todo> findAllByNameContaining(String name, Pageable pageable);
    Page<Todo> findAllByTagsIn(List<Tag> tags, Pageable pageable);
}
