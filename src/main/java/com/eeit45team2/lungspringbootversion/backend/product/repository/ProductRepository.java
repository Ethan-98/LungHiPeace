package com.eeit45team2.lungspringbootversion.backend.product.repository;

import com.eeit45team2.lungspringbootversion.backend.product.model.ProductBean;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<ProductBean, Integer> {
    //Repository 儲存區
//    Optional<ProductBean> findById(Long id);
    ProductBean findProductBeansById(Integer id);

//    ProductBean findByItems(String items);
}