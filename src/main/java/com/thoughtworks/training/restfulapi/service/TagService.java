package com.thoughtworks.training.restfulapi.service;

import com.thoughtworks.training.restfulapi.model.Tag;
import com.thoughtworks.training.restfulapi.model.User;
import com.thoughtworks.training.restfulapi.persist.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class TagService {
    @Autowired
    TagRepository tagRepository;

    Tag getTagById(Tag tag) {
        User user = TokenService.getPrincipal();
        Tag result = tagRepository.findByUser_IdAndId(user.getId(), tag.getId());
        if (result == null) {
            return null;
        } else return result;
    }

    Boolean isExistByName(Tag tag) {
        return tagRepository.existsByName(tag.getName());
    }

    public List<Tag> getTags() {
        User user = TokenService.getPrincipal();
        return tagRepository.findAllByUser_Id(user.getId());
    }

    public Tag addTag(Tag tag) {
        User user = TokenService.getPrincipal();
        tag.setUser(user);
        Tag _tag = tagRepository.findByUser_IdAndName(user.getId(), tag.getName());
        if (_tag == null){
            return tagRepository.save(tag);
        }else{
            return _tag;
        }
    }
}
