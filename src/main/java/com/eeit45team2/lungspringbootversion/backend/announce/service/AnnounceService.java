package com.eeit45team2.lungspringbootversion.backend.announce.service;

import com.eeit45team2.lungspringbootversion.backend.announce.model.AnnounceBean;

import java.util.List;

public interface AnnounceService {
	
		//show all
		public List<AnnounceBean> findAll();
		
		//save
		public void save(AnnounceBean theAnnounceBean);
		
		//FindById
		public AnnounceBean FindById(Long anNo);
		
		//delete
		public void delete(Long anNo);

	public void testDelete(String anNo);
}
