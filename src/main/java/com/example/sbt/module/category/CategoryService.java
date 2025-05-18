package com.example.sbt.module.category;

import com.example.sbt.common.dto.PaginationData;
import com.example.sbt.module.category.dto.CategoryDTO;
import com.example.sbt.module.category.dto.SearchCategoryRequestDTO;

import java.util.UUID;

public interface CategoryService {

    CategoryDTO save(CategoryDTO requestDTO);

    CategoryDTO findOneById(UUID id);

    CategoryDTO findOneByIdOrThrow(UUID id);

    PaginationData<CategoryDTO> search(SearchCategoryRequestDTO requestDTO, boolean isCount);

}
