package com.todolist.todolist.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RestController;

import com.todolist.todolist.domain.todo.RequestTodo;
import com.todolist.todolist.domain.todo.Todo;
import com.todolist.todolist.domain.user.User;
import com.todolist.todolist.infra.security.TokenService;
import com.todolist.todolist.repositories.TodoRepository;
import com.todolist.todolist.repositories.UserRepository;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@SuppressWarnings("rawtypes")
@RequestMapping("/todo")
@CrossOrigin(origins = "http://localhost:5173")
public class TodosController {

  @Autowired
  private TodoRepository todoRepository;

  @Autowired
  private UserRepository userRepository;

  @GetMapping
  public ResponseEntity getAllTodos(@RequestHeader HttpHeaders headers) {
    @SuppressWarnings("null")
    String token = headers.get("Authorization").get(0);
    token = token.replace("Bearer ", "");
    String login = new TokenService().validateToken(token);
    System.out.println(login);
    User user = userRepository.findUserByLogin(login);
    var allTodos = todoRepository.findByUserId(user.getId());
    allTodos.forEach(x -> x.setUser(null));
    return ResponseEntity.ok(allTodos);
  }

  @PostMapping
  public ResponseEntity registerTodo(@RequestBody @Valid RequestTodo requestTodo,
      @RequestHeader HttpHeaders headers) {
    String token = headers.get("Authorization").get(0);
    token = token.replace("Bearer ", "");
    String login = new TokenService().validateToken(token);
    System.out.println(login);
    UserDetails user = userRepository.findByLogin(login);
    Todo newTodo = new Todo(requestTodo);
    newTodo.setUser(user);

    todoRepository.save(newTodo);
    return ResponseEntity.ok().build();
  }

  @PatchMapping("/{id}")
  public ResponseEntity updateTodo(@PathVariable("id") String id, @RequestBody @Valid RequestTodo requestTodo) {
    Optional<Todo> todo = todoRepository.findById(id);
    todo.ifPresent(t -> {
      t.setTitle(requestTodo.title());
      t.setDescription(requestTodo.description());
      t.setCompleted(requestTodo.completed());
      todoRepository.save(t);
    });

    return ResponseEntity.ok().build();
  }

  @PatchMapping("/{id}/complete")
  public ResponseEntity completeTodo(@PathVariable("id") String id, @RequestBody @Valid RequestTodo requestTodo) {
    Optional<Todo> todo = todoRepository.findById(id);
    todo.ifPresent(t -> {
      t.setCompleted(requestTodo.completed());
      todoRepository.save(t);
    });

    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/delete/{id}")
  public ResponseEntity deleteTodo(@PathVariable("id") String id) {
    todoRepository.deleteById(id);
    return ResponseEntity.ok().build();
  }

}
