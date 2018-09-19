package com.thoughtworks.training.restfulapi.persist;

import com.thoughtworks.training.restfulapi.model.Todo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.internal.matchers.Matches;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Matchers.*;


@DataJpaTest
@RunWith(SpringRunner.class)
public class TodoRepositoryTest {

    @Autowired
    TodoRepository todoRepository;

    Todo mockTodo = Todo.builder()
            .name("name")
            .dueDate(new Date())
            .status("todo")
            .build();

    @Before
    public void setUp() throws Exception {
        todoRepository.save(mockTodo);
    }

    @Test
    public void shouldGetTodoByName() {
        Page<Todo> result = todoRepository.findAllByNameContaining("name", any());
        assertThat(result.getContent().get(0), equalTo(mockTodo));
    }



}