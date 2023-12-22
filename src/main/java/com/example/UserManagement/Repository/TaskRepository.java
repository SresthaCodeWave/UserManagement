package com.example.UserManagement.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.UserManagement.Entity.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {

  void deleteBySectionId(Integer sectionId);

  long countBySectionId(Integer sectionId);

  List<Task> findAllBySectionIdOrderByPositionDesc(Integer sectionId);

  void deleteAllBySectionId(Integer id);
}
