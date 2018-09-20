package com.thoughtworks.training.restfulapi.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Builder
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"todos"})
@EqualsAndHashCode
public class Tag {
    @Id
    @GeneratedValue
    private Long id;
    private String name;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = true)
    @JsonIgnore
    private User user;

    @ManyToMany(mappedBy = "tags")
    @JsonIgnore
    @JsonIgnoreProperties(value = "todos")
    private Set<Todo> todos = new HashSet<>();

    public Tag(Long id, String name) {
        this.id = id;
        this.name = name;
    }

}
