package com.todolist.todolist.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.todolist.todolist.domain.todo.Todo;

@SuppressWarnings("null")
public interface TodoRepository extends JpaRepository<Todo, String> {

  @Query("SELECT t FROM todos t WHERE t.user.id = :userId AND t.active = true")
  List<Todo> findByUserId(String userId);

  @Modifying
  @Query("UPDATE todos t SET t.active = false WHERE t.id = :id")
  void deleteById(String id);
}