package com.thoughtworks.training.restfulapi.controller;

import com.thoughtworks.training.restfulapi.model.Tag;
import com.thoughtworks.training.restfulapi.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/tags")
public class TagController {
    @Autowired
    TagService tagService;

    @GetMapping
    public List<Tag> getTags(){
        return tagService.getTags();
    }

    @PostMapping
    public Tag addTag(@RequestBody Tag tag){
        return tagService.addTag(tag);
    }
}
