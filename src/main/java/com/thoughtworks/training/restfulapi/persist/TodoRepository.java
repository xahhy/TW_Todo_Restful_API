package com.thoughtworks.training.restfulapi.persist;

import com.thoughtworks.training.restfulapi.model.Tag;
import com.thoughtworks.training.restfulapi.model.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {
    Page<Todo> findAllByNameContaining(String name, Pageable pageable);
    Page<Todo> findAllByTagsIn(List<Tag> tags, Pageable pageable);
    Page<Todo> findAllByDueDateBetween(Date startDate, Date endDate, Pageable pageable);
    Page<Todo> findAllByUser_Id(Long id, Pageable pageable);
    Todo findOneByUser_IdAndId(Long userId, Long id);
    @Transactional
    void deleteByUser_IdAndId(Long userId, Long id);
}
