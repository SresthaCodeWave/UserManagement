package com.example.UserManagement.Service.ServiceImplementation;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.UserManagement.Dto.TaskUpdatePositionRequest;
import com.example.UserManagement.Dto.TaskUpdateRequest;
import com.example.UserManagement.Entity.Section;
import com.example.UserManagement.Entity.Task;
import com.example.UserManagement.Repository.SectionRepository;
import com.example.UserManagement.Repository.TaskRepository;
import com.example.UserManagement.Service.TaskService;

@Service
public class TaskServiceImp implements TaskService {

  @Autowired
   TaskRepository taskRepository;

  @Autowired
  private SectionRepository sectionRepository;

  @Override
  public Task createTask(Integer sectionId) {
    try {
      Section section = sectionRepository.findById(sectionId).orElseThrow();
      long tasksCount = taskRepository.countBySectionId(sectionId);
      Task task = new Task();
      task.setSectionId(sectionId);
      task.setPosition((int) tasksCount);
      return taskRepository.save(task);
    } catch (Exception e) {
      throw new RuntimeException("Failed to create task: " + e.getMessage());
    }
  }
@Override
  public Task updateTask(Integer taskId, TaskUpdateRequest request) {
    try {
      Task task = taskRepository.findById(taskId).orElseThrow();
      // Update the task properties based on the request
      if (request.getName() != null) {
        task.setName(request.getName());
      }
      if (request.getDescription() != null) {
        task.setDescription(request.getDescription());
      }
      return taskRepository.save(task);
    } catch (Exception e) {
      throw new RuntimeException("Failed to update task: " + e.getMessage());
    }
  }

  @Override
  public void deleteTask(Integer taskId) {
    try {
      Task task = taskRepository.findById(taskId).orElseThrow();
      Integer sectionId = task.getSectionId();
      taskRepository.deleteById(taskId);
      List<Task> tasks = taskRepository.findAllBySectionIdOrderByPositionDesc(sectionId);
      for (int i = 0; i < tasks.size(); i++) {
        Task t = tasks.get(i);
        t.setPosition(i);
        taskRepository.save(t);
      }
    } catch (Exception e) {
      throw new RuntimeException("Failed to delete task: " + e.getMessage());
    }
  }

  @Override
  public void updateTaskPosition(List<TaskUpdatePositionRequest> requests) {
    try {
      for (TaskUpdatePositionRequest request : requests) {
        Task task = taskRepository.findById(request.getId()).orElseThrow();
        task.setSectionId(request.getSectionId());
        task.setPosition(request.getPosition());
        taskRepository.save(task);
      }
    } catch (Exception e) {
      throw new RuntimeException("Failed to update task positions: " + e.getMessage());
    }
  }
}

