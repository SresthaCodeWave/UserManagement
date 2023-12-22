package com.example.UserManagement.Repository;

import java.util.List;
import java.util.Optional;

import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.UserManagement.Entity.Board;
import com.example.UserManagement.Entity.Users;

@Repository
public interface BoardRepository extends JpaRepository<Board, Integer> {
  List<Board> findAllByUserOrderByIdDesc(Integer user);

  Optional<Board> findByIdAndUser(Integer boardId, Integer user);

  List<Board> findAllByUserAndFavouriteTrueAndIdNot(Integer userId, Integer boardId);

  List<Board> findAllByUser(Integer userId);

  List<Board> findAllByUserAndFavouriteTrueOrderByIdDesc(Users user);
}
