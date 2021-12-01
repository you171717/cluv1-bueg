package com.shop.service;

import com.shop.constant.OAuth2ProviderType;
import com.shop.entity.Member;
import com.shop.entity.OAuth2Member;
import com.shop.repository.MemberRepository;
import com.shop.repository.OAuth2MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
@RequiredArgsConstructor
public class OAuth2Service {

    private final MemberRepository memberRepository;

    private final OAuth2MemberRepository oAuth2MemberRepository;

    private final NaverOAuth2Service naverOAuth2Service;
    private final GoogleOAuth2Service googleOAuth2Service;
    private final KakaoOAuth2Service kakaoOAuth2Service;

    public OAuth2ServiceType getProviderService(OAuth2ProviderType providerType) {
        switch(providerType) {
            case NAVER: return naverOAuth2Service;
            case GOOGLE: return googleOAuth2Service;
            case KAKAO: return kakaoOAuth2Service;
        }

        throw new IllegalArgumentException("Invalid OAuth2 Provider Type");
    }

    public String getRedirectURL(OAuth2ProviderType providerType) {
        OAuth2ServiceType service = this.getProviderService(providerType);

        return service.getRedirectURL();
    }

    public Authentication getAuthentication(String email) {
        Member member = memberRepository.findByEmail(email);

        if(member == null)
            return null;

        OAuth2Member oAuth2Member = oAuth2MemberRepository.findByMember(member);

        if(oAuth2Member == null)
            return null;

        return this.getAuthentication(oAuth2Member);
    }

    public Authentication getAuthentication(OAuth2Member oAuth2Member) {
        Member member = oAuth2Member.getMember();

        String authority = "ROLE_" + member.getRole().toString();

        Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();

        grantedAuthorities.add(new SimpleGrantedAuthority(authority));

        return new UsernamePasswordAuthenticationToken(member.getEmail(), null, grantedAuthorities);
    }

    public OAuth2Member saveOAuth2User(OAuth2Member oAuth2Member) {
        Member member = oAuth2Member.getMember();

        validateDuplicateMember(member);

        return oAuth2MemberRepository.save(oAuth2Member);
    }

    private void validateDuplicateMember(Member member) {
        Member findMember = memberRepository.findByEmail(member.getEmail());

        if(findMember != null) {
            throw new IllegalStateException("해당 이메일은 사이트 회원 가입에 사용된 이메일입니다, 일반 로그인을 이용해 주세요.");
        }
    }

}
