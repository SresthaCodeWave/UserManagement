package com.example.UserManagement.Service.ServiceImplementation;

import java.util.List;

import javax.transaction.Transactional;

import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.UserManagement.Dto.BoardPositionUpdateRequest;
import com.example.UserManagement.Dto.BoardUpdateRequest;
import com.example.UserManagement.Entity.Board;
import com.example.UserManagement.Entity.Section;
import com.example.UserManagement.Entity.Users;
import com.example.UserManagement.Repository.BoardRepository;
import com.example.UserManagement.Repository.SectionRepository;
import com.example.UserManagement.Repository.TaskRepository;
import com.example.UserManagement.Repository.UserTableRepository;
import com.example.UserManagement.Service.BoardService;

@Service
public class BoardServiceImpl implements BoardService {
@Autowired
private BoardRepository boardRepository;
@Autowired
private UserTableRepository userTableRepository;
@Autowired
private SectionRepository sectionRepository;
@Autowired
private TaskRepository taskRepository;

  @Transactional
  @Override
  public Board createBoard(String userEmail) {
    Users user= userTableRepository.findByEmail(userEmail);
    try {
      List<Board> boards = boardRepository.findAll();
      int boardsCount = boards.size();
      Board board = new Board();
      board.setUser(user.getUserId());
      board.setPosition(boardsCount > 0 ? boardsCount : 0);
      return boardRepository.save(board);
    } catch (Exception e) {
      throw new RuntimeException("Failed to create a board: " + e.getMessage());
    }
  }

  @Override
  public List<Board> getAllBoards(String userEmail) {
    Users user= userTableRepository.findByEmail(userEmail);
    try {
      return boardRepository.findAllByUserOrderByIdDesc(user.getUserId());
    } catch (Exception e) {
      throw new RuntimeException("Failed to get all boards: " + e.getMessage());
    }
  }

  @Transactional
  @Override
  public void updateBoardPosition(List<BoardPositionUpdateRequest> requests) {
    try {
      for (BoardPositionUpdateRequest request : requests) {
        Board board = boardRepository.findById(request.getId()).orElseThrow();
        board.setPosition(request.getPosition());
        boardRepository.save(board);
      }
    } catch (Exception e) {
      throw new RuntimeException("Failed to update board positions: " + e.getMessage());
    }
  }

  @Override
  public Board getBoardById(Integer boardId, String userEmail) {
    Users user= userTableRepository.findByEmail(userEmail);
    try {
      Board board = boardRepository.findByIdAndUser(boardId, user.getUserId()).orElse(null);
      if (board == null) {
        throw new RuntimeException("Board not found");
      }

//      List<Section> sections = sectionRepository.findAllByBoardId(board.getId());
//      for (Section section : sections) {
//        List<Task> tasks = taskRepository.findAllBySectionIdOrderByPositionDesc(section.getId());
//        section.setTasks(tasks);
//      }
//      board.setSections(sections);
      return board;
    } catch (Exception e) {
      throw new RuntimeException("Failed to get a board: " + e.getMessage());
    }
  }

  @Override
  @Transactional
  public Board updateBoard(Integer boardId, BoardUpdateRequest request) {
    try {
      Board currentBoard = boardRepository.findById(boardId).orElse(null);
      if (currentBoard == null) {
        throw new RuntimeException("Board not found");
      }

      if (request.getTitle() == null || request.getTitle().isEmpty()) {
        request.setTitle("Untitled");
      }
      if (request.getDescription() == null || request.getDescription().isEmpty()) {
        request.setDescription("Add description here");
      }

      if (request.getFavourite() != null && currentBoard.isFavourite() != request.getFavourite()) {
        List<Board> favourites = boardRepository.findAllByUserAndFavouriteTrueAndIdNot(currentBoard.getUser(), boardId);
        if (!request.getFavourite()) {
          for (int i = 0; i < favourites.size(); i++) {
            Board favourite = favourites.get(i);
            favourite.setFavouritePosition(i);
            boardRepository.save(favourite);
          }
        }
      }

      Board updatedBoard = boardRepository.findByIdAndUser(boardId, currentBoard.getUser()).orElse(null);
      if (updatedBoard == null) {
        throw new RuntimeException("Board not found");
      }
      updatedBoard.setTitle(request.getTitle());
      updatedBoard.setDescription(request.getDescription());
      updatedBoard.setFavourite(request.getFavourite());
//      updatedBoard.setFavouritePosition(request.getFavouritePosition());
      return boardRepository.save(updatedBoard);
    } catch (Exception e) {
      throw new RuntimeException("Failed to update a board: " + e.getMessage());
    }
  }

  @Override
  public List<Board> getFavoriteBoards(String userEmail) {
    Users user= userTableRepository.findByEmail(userEmail);
    try {
      return boardRepository.findAllByUserAndFavouriteTrueOrderByIdDesc(user);
    } catch (Exception e) {
      throw new RuntimeException("Failed to get favorite boards: " + e.getMessage());
    }
  }

  @Override
  @Transactional
  public void updateFavoriteBoardPositions(List<BoardPositionUpdateRequest> requests) {
    try {
      for (BoardPositionUpdateRequest request : requests) {
        Board board = boardRepository.findById(request.getId()).orElseThrow();
        board.setFavouritePosition(request.getPosition());
        boardRepository.save(board);
      }
    } catch (Exception e) {
      throw new RuntimeException("Failed to update favorite board positions: " + e.getMessage());
    }
  }

  @Override
  @Transactional
  public void deleteBoard(Integer boardId, String userEmail) {
    Users user= userTableRepository.findByEmail(userEmail);
    try {
      List<Section> sections = sectionRepository.findAllByBoardId(boardId);
      for (Section section : sections) {
        taskRepository.deleteAllBySectionId(section.getId());
      }
      sectionRepository.deleteAllByBoardId(boardId);

      Board currentBoard = boardRepository.findById(boardId).orElse(null);
      if (currentBoard == null) {
        throw new RuntimeException("Board not found");
      }

      if (currentBoard.isFavourite()) {
        List<Board> favourites = boardRepository.findAllByUserAndFavouriteTrueAndIdNot(user.getUserId(), boardId);
        for (int i = 0; i < favourites.size(); i++) {
          Board favourite = favourites.get(i);
          favourite.setFavouritePosition(i);
          boardRepository.save(favourite);
        }
      }

      boardRepository.deleteById(boardId);

      List<Board> boards = boardRepository.findAllByUser(user.getUserId());
      for (int i = 0; i < boards.size(); i++) {
        Board board = boards.get(i);
        board.setPosition(i);
        boardRepository.save(board);
      }
    } catch (Exception e) {
      throw new RuntimeException("Failed to delete board: " + e.getMessage());
    }
  }
}