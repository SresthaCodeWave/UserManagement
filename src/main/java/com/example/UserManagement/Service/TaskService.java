package com.example.UserManagement.Service;

import java.util.List;

import com.example.UserManagement.Dto.TaskUpdatePositionRequest;
import com.example.UserManagement.Dto.TaskUpdateRequest;
import com.example.UserManagement.Entity.Task;

public interface TaskService {

  public Task createTask(Integer sectionId);
  public Task updateTask(Integer taskId, TaskUpdateRequest request);
  public void deleteTask(Integer taskId);
  public void updateTaskPosition(List<TaskUpdatePositionRequest> requests);
}
