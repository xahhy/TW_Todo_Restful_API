package com.thoughtworks.training.restfulapi.persist;

import com.thoughtworks.training.restfulapi.model.Tag;
import com.thoughtworks.training.restfulapi.model.Todo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Matchers.*;


@DataJpaTest
@RunWith(SpringRunner.class)
public class TodoRepositoryTest {

    @Autowired
    TagRepository tagRepository;
    @Autowired
    TodoRepository todoRepository;

    Tag tag1;
    Tag tag2;

    Todo mockTodo;

    @Before
    public void setUp() throws Exception {
        tag1 = tagRepository.saveAndFlush(new Tag(1L, "tag1"));
        tag2 = tagRepository.saveAndFlush(new Tag(2L, "tag2"));
        mockTodo = Todo.builder()
                .id(1L)
                .name("name")
                .dueDate(new Date())
                .status("todo")
                .tags(new HashSet<>(Arrays.asList(
                        tag1, tag2
                )))
                .build();
        todoRepository.saveAndFlush(mockTodo);
    }

    @Test
    public void shouldGetTodoByName() {
        Page<Todo> result = todoRepository.findAllByNameContaining("name", any());
        assertThat(result.getContent().get(0), equalTo(mockTodo));
    }

    @Test
    public void shouldGetTodoListByOneTag() {
        Todo mockTodo1 = mockTodo.builder()
                .id(2L)
                .name("new name")
                .tags(new HashSet<>(Arrays.asList(tag1)))
                .build();
        todoRepository.saveAndFlush(mockTodo1);
        Page<Todo> result = todoRepository.findAllByTagsIn(Arrays.asList(tag1), new PageRequest(0, 5));
        //hasItem use equals and hashcode to judge two Objects match.
        assertThat(result.getContent(), hasItems(mockTodo, mockTodo1));

    }

    @Test
    public void shouldGetTodoListByTwoTags() {
        Todo mockTodo1 = mockTodo.builder()
                .id(2L)
                .name("new name")
                .tags(new HashSet<>(Arrays.asList(tag1)))
                .build();
        todoRepository.saveAndFlush(mockTodo1);
        Page<Todo> result = todoRepository.findAllByTagsIn(Arrays.asList(tag1, tag2), new PageRequest(0, 5));
        //hasItem use equals and hashcode to judge two Objects match.
        assertThat(result.getContent(), hasItems(mockTodo, mockTodo1));
    }


}