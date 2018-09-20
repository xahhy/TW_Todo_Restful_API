package com.thoughtworks.training.restfulapi.persist;

import com.thoughtworks.training.restfulapi.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    Tag findByName(String name);
    Boolean existsByName(String name);

    Set<Tag> findAllByUser_Id(Long userId);

    Tag findByUser_IdAndName(Long userId, String name);
}
