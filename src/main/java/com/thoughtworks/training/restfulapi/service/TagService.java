package com.thoughtworks.training.restfulapi.service;

import com.thoughtworks.training.restfulapi.model.Tag;
import com.thoughtworks.training.restfulapi.persist.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TagService {
    @Autowired
    TagRepository tagRepository;

    Tag save(Tag tag){
        Tag result = tagRepository.findByName(tag.getName());
        if (result == null){
            return tagRepository.save(tag);
        }
        else return result;
    }

    Boolean isExistByName(Tag tag){
        return tagRepository.existsByName(tag.getName());
    }
}
