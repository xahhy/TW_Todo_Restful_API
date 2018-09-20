package com.thoughtworks.training.restfulapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Builder
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"todos"})
@EqualsAndHashCode(of = {"name"})
public class Tag {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = true)
    @JsonIgnore
    private User user;

    @ManyToMany(mappedBy = "tags", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Todo> todos = new HashSet<>();

    public Tag(Long id, String name) {
        this.id = id;
        this.name = name;
    }

}
