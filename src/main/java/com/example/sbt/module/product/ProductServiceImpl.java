package com.example.sbt.module.product;

import com.example.sbt.common.constant.ResultSetName;
import com.example.sbt.common.dto.PaginationData;
import com.example.sbt.common.exception.CustomException;
import com.example.sbt.common.mapper.CommonMapper;
import com.example.sbt.common.util.ConversionUtils;
import com.example.sbt.common.util.SQLHelper;
import com.example.sbt.module.product.dto.ProductDTO;
import com.example.sbt.module.product.dto.SearchProductRequestDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(rollbackOn = Exception.class)
public class ProductServiceImpl implements ProductService {

    private final CommonMapper commonMapper;
    private final EntityManager entityManager;
    private final ProductRepository productRepository;

    @Override
    public ProductDTO save(ProductDTO requestDTO) {
        if (requestDTO == null) {
            throw new CustomException(HttpStatus.BAD_REQUEST);
        }
        Product result = null;
        if (requestDTO.getId() != null) {
            Optional<Product> productOptional = productRepository.findById(requestDTO.getId());
            if (productOptional.isPresent()) {
                result = productOptional.get();
            }
        }
        if (result != null) {
            requestDTO.setId(result.getId());
        }
        result = commonMapper.toEntity(requestDTO);
        return commonMapper.toDTO(productRepository.save(result));
    }

    @Override
    public ProductDTO findOneById(UUID id) {
        Optional<Product> result = productRepository.findById(id);
        return result.map(commonMapper::toDTO).orElse(null);
    }

    @Override
    public ProductDTO findOneByIdOrThrow(UUID id) {
        ProductDTO result = findOneById(id);
        if (result == null) {
            throw new CustomException(HttpStatus.NOT_FOUND);
        }
        return result;
    }

    @Override
    public Integer getStockByProductId(UUID id) {
        return productRepository.findStockByProductId(id);
    }

    @Override
    public PaginationData<ProductDTO> search(SearchProductRequestDTO requestDTO, boolean isCount) {
        PaginationData<ProductDTO> result = executeSearch(requestDTO, true);
        if (!isCount && result.getTotalItems() > 0) {
            result.setItems(executeSearch(requestDTO, false).getItems());
        }
        return result;
    }

    private PaginationData<ProductDTO> executeSearch(SearchProductRequestDTO requestDTO, boolean isCount) {
        PaginationData<ProductDTO> result = SQLHelper.initData(requestDTO.getPageNumber(), requestDTO.getPageSize());
        Map<String, Object> params = new HashMap<>();
        StringBuilder builder = new StringBuilder();
        if (isCount) {
            builder.append(" select count(*) ");
        } else {
            builder.append(" select p.id, p.name, p.description, p.stock, p.price, p.variant_of, p.category_id, ");
            builder.append(" fo.file_url as image_url, p.created_at, p.updated_at ");
        }
        builder.append(" from product p ");
        builder.append(" left join file_object fo on (fo.id = p.image_file_id) ");
        builder.append(" where 1=1 ");
        if (requestDTO.getPriceFrom() != null) {
            builder.append(" and p.price >= :priceFrom ");
            params.put("priceFrom", requestDTO.getPriceFrom());
        }
        if (requestDTO.getPriceTo() != null) {
            builder.append(" and p.price <= :priceTo ");
            params.put("priceTo", requestDTO.getPriceTo());
        }
        if (requestDTO.getCreatedAtFrom() != null) {
            builder.append(" and p.created_at >= :createdAtFrom ");
            params.put("createdAtFrom", requestDTO.getCreatedAtFrom().truncatedTo(SQLHelper.MIN_TIME_PRECISION));
        }
        if (requestDTO.getCreatedAtTo() != null) {
            builder.append(" and p.created_at <= :createdAtTo ");
            params.put("createdAtTo", requestDTO.getCreatedAtTo().truncatedTo(SQLHelper.MIN_TIME_PRECISION));
        }
        if (!isCount) {
            List<String> validOrderBys = List.of("created_at", "price");
            List<String> validOrderDirections = List.of("asc", "desc");
            if (StringUtils.isNotBlank(requestDTO.getOrderBy()) && validOrderBys.contains(requestDTO.getOrderBy())) {
                builder.append(" order by p.").append(requestDTO.getOrderBy()).append(" ");
                if (validOrderDirections.contains(requestDTO.getOrderDirection())) {
                    builder.append(" ").append(requestDTO.getOrderDirection()).append(" ");
                }
            } else {
                builder.append(" order by p.created_at desc ");
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
            Query query = entityManager.createNativeQuery(builder.toString(), ResultSetName.PRODUCT_SEARCH);
            SQLHelper.setParams(query, params);
            List<ProductDTO> items = query.getResultList();
            result.setItems(items);
        }
        return result;
    }

}
