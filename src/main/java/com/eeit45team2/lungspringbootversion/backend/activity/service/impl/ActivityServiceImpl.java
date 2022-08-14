package com.eeit45team2.lungspringbootversion.backend.activity.service.impl;

import com.eeit45team2.lungspringbootversion.backend.activity.model.ActivityBean;
import com.eeit45team2.lungspringbootversion.backend.activity.repository.ActivityRepository;
import com.eeit45team2.lungspringbootversion.backend.activity.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ActivityServiceImpl implements ActivityService {

    @Autowired
    private ActivityRepository activityRepository;


    @Override
    public List<ActivityBean> findAll() {
        return activityRepository.findAll();
    }

    @Override
    public List<ActivityBean> abdoglistAll(String keyword) {
        if (keyword != null) {
            return activityRepository.search(keyword);
        }

        return activityRepository.findAll();//指令 查詢用法
    }


    @Override
    public void save(ActivityBean theActivityBean) {
        activityRepository.save(theActivityBean);
    }




    @Override
    public ActivityBean FindById(long ac_id) {
        return activityRepository.findById(ac_id).get();
        //找到這筆資料回傳,Update方法
    }
//	public ActivityBean FindById(Long ac_id) {
//		return ActivityDao.getActivityBean(ac_id);
//	}

    @Override
    public void delete(Long ac_id) {
        activityRepository.deleteById(ac_id);
    }

    @Override
    public void deleteAP(Long orderNo) {

    }


}
