package com.todolist.todolist.repositories;

import com.todolist.todolist.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserRepository extends JpaRepository<User, String> {
  UserDetails findByLogin(String login);

  @Query("SELECT u FROM users u WHERE u.login = :login")
  User findUserByLogin(String login);
}
