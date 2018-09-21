package com.thoughtworks.training.restfulapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Builder
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class User {
    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private String password;
}
