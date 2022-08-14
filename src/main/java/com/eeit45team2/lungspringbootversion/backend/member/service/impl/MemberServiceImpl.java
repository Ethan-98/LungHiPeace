package com.eeit45team2.lungspringbootversion.backend.member.service.impl;

import com.eeit45team2.lungspringbootversion.backend.member.model.MemberBean;
import com.eeit45team2.lungspringbootversion.backend.member.repository.MemberRepository;
import com.eeit45team2.lungspringbootversion.backend.member.service.MemberService;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.sql.Blob;
import java.util.List;


@Service
@Transactional
public class MemberServiceImpl implements MemberService {


    @Autowired
    private MemberRepository memberRepository;

    @Override
    public List<MemberBean> findAll() {
        return memberRepository.findAll();
    }

	@Override
	public void save(MemberBean theMemberBean) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		if(theMemberBean.getMiNo()!=null){ //是修改時
			String oldPassword = memberRepository.findById(theMemberBean.getMiNo()).get().getMiPassword();
			if(!theMemberBean.getMiPassword().equals(oldPassword)){ //有修改密碼
				theMemberBean.setMiPassword(passwordEncoder.encode(theMemberBean.getMiPassword())); //對密碼進行加密
			}
		}else{  //是新增時
			theMemberBean.setMiPassword(passwordEncoder.encode(theMemberBean.getMiPassword())); //對密碼進行加密
		}
		memberRepository.save(theMemberBean);
	}

	/*儲存: 沒有密碼加密*/
	@Override
	public void updateNoPwdEncoding(MemberBean theMemberBean) {
		if(theMemberBean.getMiNo()!=null){ //是修改時
			memberRepository.save(theMemberBean);
		}
	}

	/* 專門for前台重設密碼使用 */
	@Override
	public void save(MemberBean theMemberBean, String newPassword) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		theMemberBean.setMiPassword(passwordEncoder.encode(newPassword)); //對密碼進行加密
		//memberRepository.save(theMemberBean);
	}

	@Override
	public MemberBean findById(Long miNo) {
		return memberRepository.findById(miNo).get();
	}

	@Override
	public void delete(Long miNo) {
		memberRepository.deleteById(miNo);
	}

	@Override
	public void testDelete(String miNo) {
		Long id = Long.valueOf(miNo);
		memberRepository.deleteById(id);

    }


    @Override
    public Boolean existsByMiAccount(String miAccount) {
        return memberRepository.existsByMiAccount(miAccount);
    }

    @Override
    public MemberBean findByMiAccount(String miAccount) {
        return memberRepository.findByMiAccount(miAccount);
    }






	// MultipartFile 轉 BLOB型態 ，塞進DB
	public MemberBean saveHeadshotInDB(MemberBean memberBean, Boolean isInsert) {
		MultipartFile picture = memberBean.getProductImage(); //取得MultipartFile檔案
		// setImage (建立Blob物件，交由 Hibernate 寫入資料庫)
		if (picture != null && !picture.isEmpty()) {
			// 如果有上傳照片
			try {
				byte[] b = picture.getBytes();
				Blob blob = new SerialBlob(b);
				memberBean.setImage(blob);   //塞BLOB
				memberBean.setLocalfileName(System.currentTimeMillis() + "_" + picture.getOriginalFilename()); // 暫時先這樣寫
				return memberBean;
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("檔案上傳發生異常: " + e.getMessage());
			}
		}else {
			// 如果沒有上傳照片
			if( !isInsert ) { // 是update時
				try {
					memberBean.setImage(findById(memberBean.getMiNo()).getImage());  // 找DB中的舊照片
					memberBean.setLocalfileName(findById(memberBean.getMiNo()).getLocalfileName());  // 找DB中的舊檔名
					System.out.println(" > setImaget 成功");
					return memberBean;
				} catch (Exception e) {
					e.printStackTrace();
					throw new RuntimeException("修改時 檔案上傳發生異常: " + e.getMessage());
				}
			} // 不是update時不用setImage
			return memberBean;
		}
	}

	
}
