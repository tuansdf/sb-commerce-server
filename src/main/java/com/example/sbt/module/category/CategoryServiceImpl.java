package com.example.sbt.module.category;

import com.example.sbt.common.constant.ResultSetName;
import com.example.sbt.common.dto.PaginationData;
import com.example.sbt.common.exception.CustomException;
import com.example.sbt.common.mapper.CommonMapper;
import com.example.sbt.common.util.ConversionUtils;
import com.example.sbt.common.util.LocaleHelper;
import com.example.sbt.common.util.LocaleHelper.LocaleKey;
import com.example.sbt.common.util.SQLHelper;
import com.example.sbt.module.category.dto.CategoryDTO;
import com.example.sbt.module.category.dto.SearchCategoryRequestDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(rollbackOn = Exception.class)
public class CategoryServiceImpl implements CategoryService {

    private final CommonMapper commonMapper;
    private final EntityManager entityManager;
    private final CategoryRepository categoryRepository;

    @Override
    public CategoryDTO save(CategoryDTO requestDTO) {
        if (requestDTO == null) {
            throw new CustomException(LocaleHelper.getMessage("form.error.missing", new LocaleKey("field.category")));
        }
        if (StringUtils.isBlank(requestDTO.getName())) {
            throw new CustomException(LocaleHelper.getMessage("form.error.missing", new LocaleKey("field.name")));
        }
        if (requestDTO.getName().length() > 255) {
            throw new CustomException(LocaleHelper.getMessage("form.error.over_max_length", new LocaleKey("field.name"), 255));
        }
        if (StringUtils.isNotEmpty(requestDTO.getDescription()) && requestDTO.getDescription().length() > 255) {
            throw new CustomException(LocaleHelper.getMessage("form.error.over_max_length", new LocaleKey("field.description"), 255));
        }
        Category result = null;
        if (requestDTO.getId() != null) {
            Optional<Category> categoryOptional = categoryRepository.findById(requestDTO.getId());
            if (categoryOptional.isPresent()) {
                result = categoryOptional.get();
            }
        }
        if (result == null) {
            result = new Category();
        }
        result.setName(requestDTO.getName());
        result.setDescription(requestDTO.getDescription());
        result.setParentId(requestDTO.getParentId());
        return commonMapper.toDTO(categoryRepository.save(result));
    }

    @Override
    public CategoryDTO findOneById(UUID id) {
        if (id == null) {
            return null;
        }
        return commonMapper.toDTO(categoryRepository.findById(id).orElse(null));
    }

    @Override
    public CategoryDTO findOneByIdOrThrow(UUID id) {
        CategoryDTO result = findOneById(id);
        if (result == null) {
            throw new CustomException(LocaleHelper.getMessage("form.error.not_found", new LocaleKey("field.category")));
        }
        return result;
    }

    @Override
    public PaginationData<CategoryDTO> search(SearchCategoryRequestDTO requestDTO, boolean isCount) {
        PaginationData<CategoryDTO> result = executeSearch(requestDTO, true);
        if (!isCount && result.getTotalItems() > 0) {
            result.setItems(executeSearch(requestDTO, false).getItems());
        }
        return result;
    }

    private PaginationData<CategoryDTO> executeSearch(SearchCategoryRequestDTO requestDTO, boolean isCount) {
        PaginationData<CategoryDTO> result = SQLHelper.initData(requestDTO.getPageNumber(), requestDTO.getPageSize());
        Map<String, Object> params = new HashMap<>();
        StringBuilder builder = new StringBuilder();
        if (isCount) {
            builder.append(" select count(*) ");
        } else {
            builder.append(" select c.* ");
        }
        builder.append(" from category c ");
        builder.append(" where 1=1 ");
        if (requestDTO.getCreatedAtFrom() != null) {
            builder.append(" and c.created_at >= :createdAtFrom ");
            params.put("createdAtFrom", requestDTO.getCreatedAtFrom().truncatedTo(SQLHelper.MIN_TIME_PRECISION));
        }
        if (requestDTO.getCreatedAtTo() != null) {
            builder.append(" and c.created_at <= :createdAtTo ");
            params.put("createdAtTo", requestDTO.getCreatedAtTo().truncatedTo(SQLHelper.MIN_TIME_PRECISION));
        }
        if (!isCount) {
            List<String> validOrderBys = List.of("created_at", "name");
            List<String> validOrderDirections = List.of("asc", "desc");
            if (StringUtils.isNotBlank(requestDTO.getOrderBy()) && validOrderBys.contains(requestDTO.getOrderBy())) {
                builder.append(" order by c.").append(requestDTO.getOrderBy()).append(" ");
                if (validOrderDirections.contains(requestDTO.getOrderDirection())) {
                    builder.append(" ").append(requestDTO.getOrderDirection()).append(" ");
                }
                builder.append(" , c.id asc ");
            } else {
                builder.append(" order by c.id desc ");
            }
            builder.append(SQLHelper.toLimitOffset(result.getPageNumber(), result.getPageSize()));
        }
        if (isCount) {
            Query query = entityManager.createNativeQuery(builder.toString());
            SQLHelper.setParams(query, params);
            long count = ConversionUtils.safeToLong(query.getSingleResult());
            result.setTotalItems(count);
            result.setTotalPages(SQLHelper.toPages(count, result.getPageSize()));
        } else {
            Query query = entityManager.createNativeQuery(builder.toString(), ResultSetName.CATEGORY_SEARCH);
            SQLHelper.setParams(query, params);
            List<CategoryDTO> items = query.getResultList();
            result.setItems(items);
        }
        return result;
    }

}
