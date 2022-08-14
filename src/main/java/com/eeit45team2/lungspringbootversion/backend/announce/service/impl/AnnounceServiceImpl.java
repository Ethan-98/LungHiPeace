package com.eeit45team2.lungspringbootversion.backend.announce.service.impl;

import com.eeit45team2.lungspringbootversion.backend.announce.model.AnnounceBean;
import com.eeit45team2.lungspringbootversion.backend.announce.repository.AnnouncerRepository;
import com.eeit45team2.lungspringbootversion.backend.announce.service.AnnounceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AnnounceServiceImpl implements AnnounceService {

    @Autowired
    private AnnouncerRepository announcerRepository;

    @Override
    public List<AnnounceBean> findAll() {
        return announcerRepository.findAll();
    }

    @Override
    public void save(AnnounceBean theAnnounceBean) {
        announcerRepository.save(theAnnounceBean);
    }

    @Override
    public AnnounceBean FindById(Long anNo) {
        return announcerRepository.findById(anNo).get();
        //找到這筆資料回傳,Update方法
    }

    @Override
    public void delete(Long anNo) {
        announcerRepository.deleteById(anNo);
    }

    @Override
    public void testDelete(String  anNo) {
        Long id = Long.valueOf( anNo);
        announcerRepository.deleteById(id);
    }
}
