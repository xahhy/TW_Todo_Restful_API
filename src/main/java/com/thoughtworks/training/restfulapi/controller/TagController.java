package com.thoughtworks.training.restfulapi.controller;

import com.thoughtworks.training.restfulapi.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/tags")
public class TagController {
    @Autowired
    TagService tagService;


}
