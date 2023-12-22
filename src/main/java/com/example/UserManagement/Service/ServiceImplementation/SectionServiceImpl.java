package com.example.UserManagement.Service.ServiceImplementation;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.UserManagement.Dto.SectionCreateRequest;
import com.example.UserManagement.Dto.SectionUpdateRequest;
import com.example.UserManagement.Entity.Section;
import com.example.UserManagement.Repository.SectionRepository;
import com.example.UserManagement.Repository.TaskRepository;
import com.example.UserManagement.Service.SectionService;

@Service
public class SectionServiceImpl implements SectionService {
  @Autowired
  private SectionRepository sectionRepository;
  @Autowired
  private TaskRepository taskRepository;
  @Override
  public Section createSection(Integer boardId, SectionCreateRequest request) {
    try {
      Section section = new Section();
      section.setBoardId(boardId);
      section.setTitle(request.getTitle());
      section.setPosition(request.getPosition());
      Section savedSection = sectionRepository.save(section);
      return savedSection;
    } catch (Exception e) {
      throw new RuntimeException("Failed to create section: " + e.getMessage());
    }
  }

  @Override
  public Section updateSection(Integer sectionId, SectionUpdateRequest request) {
    try {
      Section section = sectionRepository.findById(sectionId).orElseThrow();
      // Update the section properties based on the request
      if (request.getTitle() != null) {
        section.setTitle(request.getTitle());
      }
      if (request.getPosition() >= 0) {
        section.setPosition(request.getPosition());
      }
      Section updatedSection = sectionRepository.save(section);
      return updatedSection;
    } catch (Exception e) {
      throw new RuntimeException("Failed to update section: " + e.getMessage());
    }
  }

  @Override
  public void deleteSection(Integer sectionId) {
    try {
      taskRepository.deleteBySectionId(sectionId);
      sectionRepository.deleteById(sectionId);
    } catch (Exception e) {
      throw new RuntimeException("Failed to delete section: " + e.getMessage());
    }
  }
}
