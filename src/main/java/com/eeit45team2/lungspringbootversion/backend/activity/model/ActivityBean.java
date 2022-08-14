package com.eeit45team2.lungspringbootversion.backend.activity.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "Activity_Table")
@Getter
@Setter
@ToString
public class ActivityBean {

//	@OneToOne(fetch = FetchType.LAZY, mappedBy = "order", cascade = CascadeType.ALL)
//	private Set<ActivityBean> orderItems = new LinkedHashSet<>();
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long ac_id;// 勿寫doble int型態，小寫型態不准用

	String ac_name;

	Date ac_date;

	@Column(columnDefinition = "NVARCHAR(200) NOT NULL")
	String ac_participant;

	String ac_venue;

	Integer ac_quota;

	Integer ac_waitlist_quota;

//	Integer ac_fee;

	String ac_organizer;

//	Integer acaudit;//審核 是否上架

	String localFileName;

	@Transient
	MultipartFile productImage;

	public ActivityBean() {
	}

	public Long getAc_id() {
		return ac_id;
	}

	public void setAc_name(String ac_name) {
		this.ac_name = ac_name;
	}

	public void setAc_date(Date ac_date) {
		this.ac_date = ac_date;
	}

	public void setAc_participant(String ac_participant) {
		this.ac_participant = ac_participant;
	}


	public void setAc_venue(String ac_venue) {
		this.ac_venue = ac_venue;
	}

	public void setAc_quota(Integer ac_quota) {
		this.ac_quota = ac_quota;
	}

	public void setAc_waitlist_quota(Integer ac_waitlist_quota) {
		this.ac_waitlist_quota = ac_waitlist_quota;
	}

//	public void setAc_fee(Integer ac_fee) {
//		this.ac_fee = ac_fee;
//	}

	public void setAc_organizer(String ac_organizer) {
		this.ac_organizer = ac_organizer;
	}

	public String getLocalFileName() {
		return localFileName;
	}

	public void setLocalFileName(String localfileName) {
		this.localFileName = localfileName;
	}

	public MultipartFile getProductImage() {
		return productImage;
	}

//	public void setAcaudit(Integer type) {
//		this.acaudit = type;
//	}

}
