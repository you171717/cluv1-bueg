package com.shop.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name="Tag")
@Getter
@Setter
@ToString
public class Tag {

    @Id
    @Column(name="tag_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, length = 50)
    private String tagNm;

    @Column(length = 50)
    private String tagContent;

    private int totalSell;

    //태그 누적 판매량을 증가 시키는 메소드
    public void addTotalSell(){
        this.totalSell += 1;
    }

}
