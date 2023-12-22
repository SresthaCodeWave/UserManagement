package com.example.UserManagement.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.mongodb.core.query.Update;

import java.util.List;

import com.example.UserManagement.Dto.BoardPositionUpdateRequest;
import com.example.UserManagement.Dto.BoardUpdateRequest;
import com.example.UserManagement.Entity.Board;

public interface BoardService {


  public Board createBoard(String userEmail) ;

  public List<Board> getAllBoards(String userEmail)  ;

  public void updateBoardPosition(List<BoardPositionUpdateRequest> requests) ;

  public List<Board> getFavoriteBoards(String userEmail) ;

  public void updateFavoriteBoardPositions(List<BoardPositionUpdateRequest> requests) ;
  public Board getBoardById(Integer boardId, String userEmail) ;

  public Board updateBoard(Integer boardId, BoardUpdateRequest request) ;

  public void deleteBoard(Integer boardId, String userEmail)  ;
}

