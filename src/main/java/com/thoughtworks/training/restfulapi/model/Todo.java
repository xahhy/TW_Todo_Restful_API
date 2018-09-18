package com.thoughtworks.training.restfulapi.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Todo {
    private Long id;
    private String name;
    private String status;
    //    @JsonProperty("timestamp")
    @JsonFormat(
            shape = JsonFormat.Shape.STRING,
            pattern = "yyyy-MM-dd",
            locale = "zh-CN",
            timezone = "Asia/Shanghai")
    private Date dueDate;

}
