package com.example.sbt.module.category;

import com.example.sbt.common.dto.PaginationData;
import com.example.sbt.module.category.dto.CategoryDTO;
import com.example.sbt.module.category.dto.SearchCategoryRequestDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(rollbackOn = Exception.class)
public class CategoryServiceImpl implements CategoryService {

    @Override
    public CategoryDTO save(CategoryDTO requestDTO) {
        return null;
    }

    @Override
    public CategoryDTO findOneById(UUID id) {
        return null;
    }

    @Override
    public CategoryDTO findOneByIdOrThrow(UUID id) {
        return null;
    }

    @Override
    public PaginationData<CategoryDTO> search(SearchCategoryRequestDTO requestDTO, boolean isCount) {
        return null;
    }

}
