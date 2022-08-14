package com.eeit45team2.lungspringbootversion.backend.animal.repository;

import com.eeit45team2.lungspringbootversion.backend.animal.model.AbDogBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AbDogRepository extends JpaRepository<AbDogBean,Long> {
    //Repository 儲存區
    @Query("SELECT p FROM AbDogBean p WHERE p.abtype LIKE %?1%"
            + " OR p.abvariety LIKE %?1%"
            + " OR p.abarea LIKE %?1%"
            + " OR CONCAT(p.absex, '') LIKE %?1%")
    public List<AbDogBean> search(String keyword);

}
