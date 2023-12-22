package com.example.UserManagement.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.UserManagement.Entity.Section;

@Repository
public interface SectionRepository extends JpaRepository<Section, Integer> {
  List<Section> findAllByBoardId(Integer id);

  void deleteAllByBoardId(Integer boardId);
}
