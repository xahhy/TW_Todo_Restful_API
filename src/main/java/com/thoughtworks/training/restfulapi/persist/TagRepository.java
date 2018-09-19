package com.thoughtworks.training.restfulapi.persist;

import com.thoughtworks.training.restfulapi.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    Tag getByName(String name);
}
