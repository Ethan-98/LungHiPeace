package com.eeit45team2.lungspringbootversion.backend.animal.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import com.eeit45team2.lungspringbootversion.backend.animal.model.AbDogBean;
import com.eeit45team2.lungspringbootversion.backend.animal.repository.AbDogRepository;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.mail.SimpleMailMessage;
//import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class AbDogService {

	@Autowired
	private JavaMailSender mailSender;
	@Autowired
	private AbDogRepository abdogrepo;

	//findAll搜尋SQL無條件
	//findById條件式 get

//	public Page<AbDogBean> listAll(int pageNum) {
//		int pageSize = 7;//一頁7筆
//		//頁數 - 1 電腦0 開始 所以頁數 - 1
//		Pageable pageable =   PageRequest.of(pageNum - 1, pageSize);
//		//sql裡面叫limit 總筆數count		return abdogrepo.findAll();//指令 查詢用法
//		return abdogrepo.findAll(pageable);
//
////

	public List<AbDogBean> abdoglistAll(String keyword){
	if (keyword != null) {
		return abdogrepo.search(keyword);
	}

	return abdogrepo.findAll();//指令 查詢用法
}


		public void save(AbDogBean abdog) {
			abdogrepo.save(abdog);
		}


	public AbDogBean get(long abid) {
		return abdogrepo.findById(abid).get();//找到這筆資料 回傳 更正用法
	}

	public void delete (long abid) {
		abdogrepo.deleteById(abid);//命令
	}



}
