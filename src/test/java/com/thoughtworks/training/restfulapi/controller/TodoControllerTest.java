package com.thoughtworks.training.restfulapi.controller;

import com.thoughtworks.training.restfulapi.TestUtil;
import com.thoughtworks.training.restfulapi.exceptions.NotFoundException;
import com.thoughtworks.training.restfulapi.model.Todo;
import com.thoughtworks.training.restfulapi.service.TodoService;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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

    Page<Todo> mockPage;
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
        todo = Todo.builder()
                .id(0L)
                .name(SOME_TODO_NAME)
                .status(SOME_TODO_STATUS)
                .dueDate(new SimpleDateFormat("yyyy-MM-dd").parse(SOME_TODO_DUE_DATE))
                .tags(new ArrayList<>())
                .build();
        newTodo = Todo.builder()
                .id(0L)
                .name(NEW_TODO_NAME)
                .status(NEW_TODO_STATUS)
                .dueDate(new SimpleDateFormat("yyyy-MM-dd").parse(NEW_TODO_DUE_DATE))
                .tags(new ArrayList<>())
                .build();
        mockPage = new PageImpl<>(Arrays.asList(todo));
    }

    @Test
    @WithMockUser
    public void shouldGetAllTodos() throws Exception {
        when(todoService.getTodoList()).thenReturn(Arrays.asList(
                todo
        ));
        given(todoService.getPageableTodoList(any())).willReturn(mockPage);
        mockMvc.perform(get("/todos")).andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(0L));
    }

    @Test
    @WithMockUser
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
    @WithMockUser
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
    @WithMockUser
    public void shouldUpdateOneExistTodo() throws Exception {
        when(todoService.updateTodo(any(), any())).thenReturn(newTodo);
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
    @WithMockUser
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


    @Test
    @WithMockUser
    public void shouldReturnNotFoundWhenDeleteUnExistTodo() throws Exception {
        when(todoService.deleteTodo(1L)).thenThrow(new NotFoundException());
        mockMvc.perform(
                delete("/todos/{id}", 1)
                        .contentType(TestUtil.APPLICATION_JSON_UTF8)
                        .content(TestUtil.convertObjectToJsonBytes(todo))
        )
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}