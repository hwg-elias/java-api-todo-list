package com.todolist.todolist.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.todolist.todolist.domain.todo.Todo;

public interface TodoRepository extends JpaRepository<Todo, String> {

  @Query("SELECT t FROM todos t WHERE t.user.id = :userId")
  List<Todo> findByUserId(String userId);
}