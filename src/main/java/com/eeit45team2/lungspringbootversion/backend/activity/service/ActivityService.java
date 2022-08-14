package com.eeit45team2.lungspringbootversion.backend.activity.service;

import com.eeit45team2.lungspringbootversion.backend.activity.model.ActivityBean;

import java.util.List;


//@Service
//@Transactional
public interface ActivityService {
	
	//show all
		public List<ActivityBean> findAll();

		public List<ActivityBean> abdoglistAll(String keyword);
		//save
		public void save(ActivityBean theActivityBean);

		//getOrder
		public ActivityBean FindById(long ac_id);
		
		//delete
		public void delete(Long ac_id);
	//delete
		public void deleteAP(Long orderNo);

}
