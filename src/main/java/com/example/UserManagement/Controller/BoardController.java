package com.example.UserManagement.Controller;

import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.data.mongodb.core.query.Update;


import java.util.List;
import java.util.Collections;

import com.example.UserManagement.Dto.BoardPositionUpdateRequest;
import com.example.UserManagement.Dto.BoardUpdateRequest;
import com.example.UserManagement.Dto.SectionCreateRequest;
import com.example.UserManagement.Dto.SectionUpdateRequest;
import com.example.UserManagement.Dto.TaskUpdatePositionRequest;
import com.example.UserManagement.Dto.TaskUpdateRequest;
import com.example.UserManagement.Entity.Board;
import com.example.UserManagement.Entity.Section;
import com.example.UserManagement.Entity.Task;
import com.example.UserManagement.Service.BoardService;
import com.example.UserManagement.Service.JwtService;
import com.example.UserManagement.Service.SectionService;
import com.example.UserManagement.Service.TaskService;
import com.example.UserManagement.Service.UserService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/boards")
@Slf4j

public class BoardController {
  @Autowired
  private UserService userService;
 @Autowired
  private BoardService boardService;
 @Autowired
 private JwtService jwtService;
 @Autowired
 private SectionService sectionService;
 @Autowired
 private TaskService taskService;

  // Create a new board

  @PostMapping("/createBoards")
  public ResponseEntity<Board> createBoard(@RequestHeader("Authorization") String token) {
    String userEmail = jwtService.extractUsername(token);
    Board createdBoard = boardService.createBoard(userEmail);
    return ResponseEntity.status(201).body(createdBoard);
  }

  @GetMapping("/getAllBoards")
  public ResponseEntity<List<Board>> getAllBoards(@RequestHeader("Authorization") String token) {
    String userEmail = jwtService.extractUsername(token);
    List<Board> boards = boardService.getAllBoards(userEmail);
    return ResponseEntity.ok(boards);
  }

  @PutMapping("/updateBoardPositions")
  public ResponseEntity<String> updateBoardPosition(@RequestBody List<BoardPositionUpdateRequest> requests) {
    boardService.updateBoardPosition(requests);
    return ResponseEntity.ok("Updated");
  }

  @GetMapping("/favourites")
  public ResponseEntity<List<Board>> getFavoriteBoards(@RequestHeader("Authorization") String token) {
    String userEmail = jwtService.extractUsername(token);
    List<Board> favoriteBoards = boardService.getFavoriteBoards(userEmail);
    return ResponseEntity.ok(favoriteBoards);
  }

  @PutMapping("/favourites")
  public ResponseEntity<String> updateFavoriteBoardPositions(@RequestBody List<BoardPositionUpdateRequest> requests) {
    boardService.updateFavoriteBoardPositions(requests);
    return ResponseEntity.ok("Updated");
  }

  @GetMapping("/{boardId}")
  public ResponseEntity<Board> getOneBoard(@RequestHeader("Authorization") String token, @PathVariable Integer boardId) {
    String userEmail = jwtService.extractUsername(token);
    Board board = boardService.getBoardById(boardId, userEmail);
    return ResponseEntity.ok(board);
  }

  @PutMapping("/{boardId}")
  public ResponseEntity<Board> updateBoard(@PathVariable Integer boardId, @RequestBody BoardUpdateRequest request) {
    Board updatedBoard = boardService.updateBoard(boardId, request);
    return ResponseEntity.ok(updatedBoard);
  }

  @DeleteMapping("/{boardId}")
  public ResponseEntity<String> deleteBoard(@PathVariable Integer boardId, @RequestHeader("Authorization") String token) {
    String userEmail = jwtService.extractUsername(token);
    boardService.deleteBoard(boardId, userEmail);
    return ResponseEntity.ok("Deleted");
  }

  //Section Controller

  @PostMapping("/createSection/{boardId}")
  public ResponseEntity<Section> createSection(
      @PathVariable  Integer boardId,
      @RequestBody SectionCreateRequest request
  ) {
    log.info("Started");
    Section response= sectionService.createSection(boardId, request);

    return  ResponseEntity.ok(response);
  }

  @PutMapping("/updateSection")
  public Section updateSection(
      @RequestParam  Integer boardId,
      @RequestParam  Integer sectionId,
      @RequestBody  SectionUpdateRequest request
  ) {
    return sectionService.updateSection(sectionId, request);
  }

  @DeleteMapping("/deleteSection")
  public void deleteSection(
      @RequestBody  Integer boardId,
      @RequestBody  Integer sectionId
  ) {
    sectionService.deleteSection(sectionId);
  }

  //TaskController
  @PostMapping("/createTask")
  public ResponseEntity<Task> createTask(@RequestParam Integer boardId, @RequestParam Integer sectionId) {
    return new ResponseEntity( taskService.createTask(sectionId), HttpStatus.OK);
  }

  @PutMapping("/update-position")
  public void updateTaskPosition(@RequestParam Integer boardId, @RequestBody List<TaskUpdatePositionRequest> taskIds) {
    taskService.updateTaskPosition(taskIds);
  }

  @DeleteMapping("/{taskId}")
  public void deleteTask(@PathVariable Integer taskId) {
    taskService.deleteTask(taskId);
  }

  @PutMapping("/updateTasks")
  public Task updateTask(@RequestParam Integer boardId, @RequestParam Integer taskId, @RequestBody TaskUpdateRequest request) {
    return taskService.updateTask( taskId, request);
  }
}

