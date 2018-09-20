package com.thoughtworks.training.restfulapi.persist;

import com.thoughtworks.training.restfulapi.model.Tag;
import com.thoughtworks.training.restfulapi.model.Todo;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import static org.hamcrest.Matchers.*;
import static org.mockito.Matchers.any;


@DataJpaTest
@RunWith(SpringRunner.class)
public class TodoRepositoryTest{

    @Autowired
    TagRepository tagRepository;
    @Autowired
    TodoRepository todoRepository;

    Tag tag1;
    Tag tag2;
    Date startDate;
    Date endDate;
    Date mockDate10_8;
    Date mockDate10_11;
    Todo mockTodo10_8;
    Todo mockTodo10_11;
    private PageRequest pageable = new PageRequest(0, 10);

    @Before
    public void setUp() throws Exception {
        tag1 = tagRepository.save(new Tag(0L, "tag1"));
        tag2 = tagRepository.save(new Tag(2L, "tag2"));
        startDate = new SimpleDateFormat("yyyy-MM-dd").parse("2018-10-01");
        endDate = new SimpleDateFormat("yyyy-MM-dd").parse("2018-10-10");
        mockDate10_8 = new SimpleDateFormat("yyyy-MM-dd").parse("2018-10-08");
        mockDate10_11 = new SimpleDateFormat("yyyy-MM-dd").parse("2018-10-11");
        mockTodo10_8 = Todo.builder()
                .id(1L)
                .name("name")
                .dueDate(mockDate10_8)
                .status("todo")
                .tags(Arrays.asList(
                        tag1, tag2
                ))
                .build();
        mockTodo10_11 = Todo.builder()
                .id(11L)
                .name("10_11")
                .dueDate(mockDate10_11)
                .status("todo")
                .tags(Arrays.asList(
                        tag1, tag2
                ))
                .build();
        todoRepository.save(mockTodo10_8);
    }

    @Test
    public void shouldGetTodoByName() {
        Page<Todo> result = todoRepository.findAllByNameContaining("name", any());
        Assert.assertThat(result.getContent().get(0).toString(), is(mockTodo10_8.toString()));
    }

    @Test
    public void shouldGetTodoListByOneTag() {
        Todo mockTodo1 = mockTodo10_8.builder()
                .id(2L)
                .name("new name")
                .tags(Arrays.asList(tag1))
                .build();
        todoRepository.save(mockTodo1);
        Page<Todo> result = todoRepository.findAllByTagsIn(Arrays.asList(tag1), pageable);
        //hasItem use equals and hashcode to judge two Objects match.
        //TODO: 这里在对比两个Todo对象是否相等时会返回不相等的结果.没有正确为Todo.tags这个列表属性正确生成equals方法
        //可以在调试终端中查看result.getContent().get(0).tags.equals(mockTodo10_8.tags)的值是false
        Assert.assertThat(result.getContent(), hasItems(mockTodo10_8, mockTodo1));
    }

    @Test
    public void shouldGetTodoListByTwoTags() {
        Todo mockTodo1 = mockTodo10_8.builder()
                .id(2L)
                .name("new name")
                .tags(Arrays.asList(tag1))
                .build();
        todoRepository.save(mockTodo1);
        Page<Todo> result = todoRepository.findAllByTagsIn(Arrays.asList(tag1, tag2), pageable);
        //hasItem use equals and hashcode to judge two Objects match.
        Assert.assertThat(result.getContent(), hasItems(mockTodo10_8, mockTodo1));
    }

    @Test
    public void shouldGetTodoListBetweenDates() {
        Page<Todo> result = todoRepository.findAllByDueDateBetween(startDate, endDate, pageable);
        Assert.assertThat(result, hasItems(mockTodo10_8));
        Assert.assertThat(result, not(hasItems(mockTodo10_11)));
    }
}