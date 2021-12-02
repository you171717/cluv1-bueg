package com.shop.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name="faq")
@Getter
@Setter
@ToString
public class FAQ{
    @Id
    @Column(name="faq_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Lob
    @Column(nullable=false)
    private String question;

    @Lob
    @Column(nullable=false)
    private String answer;
}
