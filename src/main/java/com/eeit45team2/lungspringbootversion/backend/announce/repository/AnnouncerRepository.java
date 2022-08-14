package com.eeit45team2.lungspringbootversion.backend.announce.repository;

import com.eeit45team2.lungspringbootversion.backend.announce.model.AnnounceBean;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnnouncerRepository extends JpaRepository<AnnounceBean,Long> {
    //Repository 儲存區
}
