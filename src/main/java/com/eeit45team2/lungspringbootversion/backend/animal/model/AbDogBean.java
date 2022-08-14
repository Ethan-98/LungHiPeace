package com.eeit45team2.lungspringbootversion.backend.animal.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;



@Entity //實體類
@Table(name="abdog_table") //資料表
@Getter
@Setter
@ToString

public class AbDogBean {
	@Id //主KEY     自動生成                     每次+1
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long abid; //自動給號碼
	private String abtype; //類
	private String abvariety; //品種
	private String absex;//姓 公 母
	private String abphonto;//圖片
//	@Transient
	private String abarea;
//	@OneToOne(cascade = {CascadeType.PERSIST})
//	@JoinColumn(name = "miNo")
//	private MemberBean memberBean;
//	public String getAbarea() {
//		if (memberBean != null) {
//			return memberBean.getMiName();
//		} else {
//			return null;
//		}
//	}

	private String abname;//收容所地址
	private String abphone;//收容所電話
	private String abemail;//收容所信箱
	private String abdate;//入所日期
	private String abremark;//描述

	private int abaudit;//審核 是否開放認領養

	private String abnickname; //動物名字

	public AbDogBean() {
	}

	public AbDogBean(Long abid, String abtype, String abvariety, String absex, String abphonto, String abarea, String abname, String abphone, String abemail, String abdate, String abremark, int abaudit) {
		this.abid = abid;
		this.abtype = abtype;
		this.abvariety = abvariety;
		this.absex = absex;
		this.abphonto = abphonto;
		this.abarea = abarea;
		this.abname = abname;
		this.abphone = abphone;
		this.abemail = abemail;
		this.abdate = abdate;
		this.abremark = abremark;
		this.abaudit = abaudit;
	}

	public AbDogBean( String abtype, String abvariety, String absex, String abphonto, String abarea, String abname, String abphone, String abemail, String abdate, String abremark, int abaudit) {
		this.abtype = abtype;
		this.abvariety = abvariety;
		this.absex = absex;
		this.abphonto = abphonto;
		this.abarea = abarea;
		this.abname = abname;
		this.abphone = abphone;
		this.abemail = abemail;
		this.abdate = abdate;
		this.abremark = abremark;
		this.abaudit = abaudit;
	}
}

