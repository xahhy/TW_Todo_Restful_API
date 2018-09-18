package com.thoughtworks.training.restfulapi.controller;

import com.thoughtworks.training.restfulapi.TestUtil;
import com.thoughtworks.training.restfulapi.model.Todo;
import com.thoughtworks.training.restfulapi.service.TodoService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TodoControllerTest {

    @MockBean
    TodoService todoService;

    @Autowired
    MockMvc mockMvc;

    Todo todo;
    Todo newTodo;
    private String SOME_TODO_NAME = "name";
    private String NEW_TODO_NAME = "new name";
    private String SOME_TODO_STATUS = "status";
    private String NEW_TODO_STATUS = "status";
    private String SOME_TODO_DUE_DATE = "2018-10-01";
    private String NEW_TODO_DUE_DATE = "2018-10-11";

    @Before
    public void setUp() throws Exception {
        todo = new Todo(0L, SOME_TODO_NAME, SOME_TODO_STATUS, new SimpleDateFormat("yyyy-MM-dd").parse(SOME_TODO_DUE_DATE));
        newTodo = new Todo(0L, NEW_TODO_NAME, NEW_TODO_STATUS, new SimpleDateFormat("yyyy-MM-dd").parse(NEW_TODO_DUE_DATE));
        when(todoService.getTodoList()).thenReturn(Arrays.asList(
                todo
        ));
    }

    @Test
    public void shouldGetAllTodos() throws Exception {
        mockMvc.perform(get("/todos")).andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(0L));
    }

    @Test
    public void shouldGetOneTodo() throws Exception {
        when(todoService.getTodoById(0L)).thenReturn(todo);
        mockMvc.perform(get("/todos/{id}", 0L))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(0L))
                .andExpect(jsonPath("$.name").value(SOME_TODO_NAME))
                .andExpect(jsonPath("$.status").value(SOME_TODO_STATUS));
    }


    @Test
    public void shouldAddOneTodo() throws Exception {
        when(todoService.addTodo(any())).thenReturn(todo);
        mockMvc.perform(
                post("/todos")
                        .contentType(TestUtil.APPLICATION_JSON_UTF8)
                        .content(TestUtil.convertObjectToJsonBytes(todo))
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(SOME_TODO_NAME))
                .andExpect(jsonPath("$.status").value(SOME_TODO_STATUS))
                .andExpect(jsonPath("$.dueDate").value(SOME_TODO_DUE_DATE));
    }

    @Test
    public void shouldUpdateOneExistTodo() throws Exception {
        when(todoService.updateTodo(eq(0L), any())).thenReturn(newTodo);
        mockMvc.perform(
                put("/todos/{id}", 0)
                        .contentType(TestUtil.APPLICATION_JSON_UTF8)
                        .content(TestUtil.convertObjectToJsonBytes(todo))
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(NEW_TODO_NAME))
                .andExpect(jsonPath("$.status").value(NEW_TODO_STATUS))
                .andExpect(jsonPath("$.dueDate").value(NEW_TODO_DUE_DATE));
    }

    @Test
    public void shouldDeleteOneTodo() throws Exception {
        mockMvc.perform(
                delete("/todos/{id}", 0)
                        .contentType(TestUtil.APPLICATION_JSON_UTF8)
                        .content(TestUtil.convertObjectToJsonBytes(todo))
        )
                .andDo(print())
                .andExpect(status().isOk());
        verify(todoService).deleteTodo(0L);
    }
}