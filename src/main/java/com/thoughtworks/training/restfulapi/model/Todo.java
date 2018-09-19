package com.thoughtworks.training.restfulapi.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.*;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString
@EqualsAndHashCode(exclude = {"id"})
public class Todo {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String status;
    @JsonFormat(
            shape = JsonFormat.Shape.STRING,
            pattern = "yyyy-MM-dd",
            locale = "zh-CN",
            timezone = "Asia/Shanghai")
    private Date dueDate;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "todo_tag",
            joinColumns = @JoinColumn(name = "todo_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id", referencedColumnName = "id")
    )
    Set<Tag> tags = new HashSet<>();
}
