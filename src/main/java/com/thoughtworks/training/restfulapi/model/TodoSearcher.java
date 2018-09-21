package com.thoughtworks.training.restfulapi.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.thoughtworks.training.restfulapi.service.TodoService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TodoSearcher {
    String name;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    Date startDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    Date endDate;
    List<Long> tagsId;
    String sort;
    String direction;
}
