package com.shop.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class AuthToken extends BaseEntity {

    @Id
    @Column(name = "token_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String code;

    private LocalDateTime expireDate;

    private String useYn = "N";

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name= "member_id")
    private Member member;

}
