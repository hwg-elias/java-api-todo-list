package com.todolist.todolist.domain.todo;

import org.springframework.security.core.userdetails.UserDetails;

import com.todolist.todolist.domain.user.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "todos")
@Entity(name = "todos")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Todo {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  private String title;

  private String description;

  private Boolean completed;

  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "user_id", referencedColumnName = "id")
  private User user;

  public Todo(RequestTodo requestTodo) {
    this.title = requestTodo.title();
    this.description = requestTodo.description();
    if (requestTodo.completed() == null) {
      this.completed = false;
    } else {
      this.completed = requestTodo.completed();
    }
  }

  public void setUser(UserDetails user) {
    this.user = (User) user;
  }

}
