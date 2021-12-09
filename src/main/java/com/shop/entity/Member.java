package com.shop.entity;

import com.shop.constant.Bank;
import com.shop.constant.NoticeType;
import com.shop.constant.Role;
import com.shop.dto.MemberFormDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;

@Entity
@Table(name="member")
@Getter
@Setter
@ToString
public class Member extends BaseEntity {

    @Id
    @Column(name="member_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "member")
    private OAuth2Member oAuth2Member;

    @Column(nullable = false)
    private String name;

    @Column(unique = true)
    private String email;

    private String password;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String addressDetail;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Bank refundBank;

    @Column(nullable = false)
    private String refundAccount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private int point = 1000;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NoticeType noticeType;

    public static Member createMember(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder) {
        String password = passwordEncoder.encode(memberFormDto.getPassword());

        Member member = new Member();
        member.setName(memberFormDto.getName());
        member.setEmail(memberFormDto.getEmail());
        member.setAddress(memberFormDto.getAddress());
        member.setAddressDetail(memberFormDto.getAddressDetail());
        member.setPassword(password);
        member.setRole(Role.USER);
        member.setPhone(memberFormDto.getPhone());
        member.setPoint(1000);
        member.setRefundBank(memberFormDto.getRefundBank());
        member.setRefundAccount(memberFormDto.getRefundAccount());
        member.setNoticeType(memberFormDto.getNoticeType());

        return member;
    }

    public void updatePoint(int accPoint, int usedPoint) {
        this.point = this.point - accPoint + usedPoint;
    }

}
