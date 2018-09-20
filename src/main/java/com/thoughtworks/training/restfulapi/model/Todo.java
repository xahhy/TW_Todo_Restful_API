package com.thoughtworks.training.restfulapi.model;

import com.fasterxml.jackson.annotation.*;
import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Builder
@Accessors(chain=true)
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
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = true)
    @JsonIgnore
    private User user;
    @JsonFormat(
            shape = JsonFormat.Shape.STRING,
            pattern = "yyyy-MM-dd",
            locale = "zh-CN",
            timezone = "Asia/Shanghai")
    private Date dueDate;

    @ManyToMany//(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "todo_tag",
            joinColumns = @JoinColumn(name = "todo_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id", referencedColumnName = "id")
    )
    @JsonIgnore
    @JsonManagedReference
    List<Tag> tags = new ArrayList<>();

    @Transient
    @JsonGetter("tags")
    public List<Long> getTagsId() {
        return tags.stream().map(Tag::getId).collect(Collectors.toList());
    }


    @Transient
    @JsonSetter("tags")
    public void setTagsId(List<Long> tagsId) {
        this.tags = tagsId.stream().map(tagId -> Tag.builder().id(tagId).build()).collect(Collectors.toList());
    }
}
