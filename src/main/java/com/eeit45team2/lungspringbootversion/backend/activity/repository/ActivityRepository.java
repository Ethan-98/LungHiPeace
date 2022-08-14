package com.eeit45team2.lungspringbootversion.backend.activity.repository;

import com.eeit45team2.lungspringbootversion.backend.activity.model.ActivityBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ActivityRepository extends JpaRepository<ActivityBean, Long> {
    //Repository 儲存區
    @Query("SELECT p FROM ActivityBean p WHERE p.ac_name LIKE %?1%"
            + " OR p.ac_venue LIKE %?1%"
            + " OR p.ac_participant LIKE %?1%")
    public List<ActivityBean> search(String keyword);
}
