package com.example.todo.service;

import com.example.todo.model.Todo;

import com.example.todo.model.  TodoRowMapper;
import com.example.todo.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;

@Service
public class TodoH2Service implements TodoRepository {

    @Autowired
    private JdbcTemplate db;

    public ArrayList<Todo> getAllTodos() {
        return (ArrayList<Todo>) db.query("select * from todoList", new TodoRowMapper());
    }

    public Todo getTodoById(int id) {
        try {
            return db.queryForObject("select * from todoList where id = ?", new TodoRowMapper(), id);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    public Todo addTodo(Todo todo) {
        db.update("insert into todoList(id, todo, priority, status) values (?,?,?,?)",
                todo.getId(), todo.getTodo(), todo.getPriority(), todo.getStatus());
        return db.queryForObject("select * from todoList where id = ? and todo = ?", new TodoRowMapper(),
                todo.getId(), todo.getTodo());
    }

    public void deleteTodo(int id) {
        db.update("delete from todoList where id = ?", id);
    }

    public Todo updateTodo(int id, Todo todo) {
        if (todo.getId() != 0) {
            db.update("update todoList set id = ? where id =?", todo.getId(), id);
        }
        if (todo.getTodo() != null) {
            db.update("update todoList set todo = ? where id =?", todo.getTodo(), id);
        }
         if (todo.getPriority() != null) {
            db.update("update todoList set priority = ? where id =?", todo.getPriority(), id);
        }
        if (todo.getStatus() != null) {
            db.update("update todoList set status = ? where id =?", todo.getStatus(), id);
        }
        return getTodoById(id);
    }
}
