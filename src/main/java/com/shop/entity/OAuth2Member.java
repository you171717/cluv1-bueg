package com.shop.entity;

import com.shop.constant.OAuth2ProviderType;
import com.shop.constant.Role;
import com.shop.dto.OAuth2FormDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="oauth")
@Getter
@Setter
@ToString
public class OAuth2Member extends BaseTimeEntity implements Serializable {

    @Id
    @Column(name = "member_id")
    private Long id;

    @MapsId
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "member_id")
    private Member member;

    @Enumerated(EnumType.STRING)
    private OAuth2ProviderType type;

    public static OAuth2Member createOAuth2Member(OAuth2FormDto oAuth2FormDto, OAuth2ProviderType type) {
        Member member = new Member();
        member.setName(oAuth2FormDto.getName());
        member.setEmail(oAuth2FormDto.getEmail());
        member.setAddress(oAuth2FormDto.getAddress());
        member.setRefundBank(oAuth2FormDto.getRefundBank());
        member.setRefundAccount(oAuth2FormDto.getRefundAccount());
        member.setRole(Role.USER);

        OAuth2Member oAuth2Member = new OAuth2Member();
        oAuth2Member.setMember(member);
        oAuth2Member.setType(type);

        return oAuth2Member;
    }

}
