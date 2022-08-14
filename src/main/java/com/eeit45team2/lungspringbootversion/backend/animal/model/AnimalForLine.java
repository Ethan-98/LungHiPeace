package com.eeit45team2.lungspringbootversion.backend.animal.model;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity //實體類
@Getter
@Setter
@ToString
@Table(name="LineAnimalTable")
public class AnimalForLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long amlId;

    private String amlType; // 類別
    private String amlSpecies; // 品種
    private String amlSex; // 公 母
    private String amlImage; // 照片
    private String amlName; // 姓名
    private String amlAddress;  // 收容所位置

}
