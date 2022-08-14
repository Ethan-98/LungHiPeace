package com.eeit45team2.lungspringbootversion.backend.product.service;

import com.eeit45team2.lungspringbootversion.backend.product.model.ProductBean;

import java.util.List;


public interface ProductService {

    //show all
    public List<ProductBean> findAll();

    //save
    public void save(ProductBean theProductBean);

    //getOrder
    public ProductBean FindById(Integer id);

    //delete
    public void delete(Integer id);

    ProductBean getProducts(int id);

//    public ProductBean FindByItems(String items);


}

