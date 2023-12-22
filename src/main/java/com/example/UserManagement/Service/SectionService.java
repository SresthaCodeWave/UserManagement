package com.example.UserManagement.Service;

import com.example.UserManagement.Dto.SectionCreateRequest;
import com.example.UserManagement.Dto.SectionUpdateRequest;
import com.example.UserManagement.Entity.Section;

public interface SectionService {
  public Section createSection(Integer boardId, SectionCreateRequest request);
  public Section updateSection(Integer sectionId, SectionUpdateRequest request);
  public void deleteSection(Integer sectionId);
}
