package com.thoughtworks.training.restfulapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

public class Todo {
    private Long id;
    private String name;
    private String status;
//    @JsonProperty("timestamp")
    private Date dueDate;

    public Todo() {
    }

    public Todo(long id, String name, String status, Date dueDate) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.dueDate = dueDate;
    }

    public Long getId() {
        return id;
    }

    public Todo setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }
}
