package com.example.sbt.module.product;

import com.example.sbt.common.dto.PaginationData;
import com.example.sbt.module.product.dto.ProductDTO;
import com.example.sbt.module.product.dto.SearchProductRequestDTO;

import java.util.List;
import java.util.UUID;

public interface ProductService {

    ProductDTO save(ProductDTO productDTO);

    ProductDTO findOneById(UUID id);

    ProductDTO findOneByIdOrThrow(UUID id);

    List<ProductDTO> findAllByIdIn(List<UUID> id);

    Integer getStockByProductId(UUID id);

    PaginationData<ProductDTO> search(SearchProductRequestDTO requestDTO, boolean isCount);

}
