package com.shop.service;

import com.shop.entity.AuthToken;
import com.shop.entity.Member;
import com.shop.repository.AuthTokenRepository;
import com.shop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthTokenService {

    private final AuthTokenRepository authTokenRepository;
    private final MemberRepository memberRepository;
    private final EncryptionService encryptionService;

    public String getTokenCode(String email) {
        return encryptionService.encryptMD5(email + LocalDateTime.now());
    }

    public AuthToken getTokenByEmail(String email) {
        Member member = memberRepository.findByEmail(email);

        return authTokenRepository.findFirstByMemberOrderByRegTimeDesc(member);
    }

    public AuthToken getTokenByCode(String code){
        return authTokenRepository.findFirstByCodeOrderByRegTimeDesc(code);
    }

    public AuthToken createToken(String email) {
        Member member = memberRepository.findByEmail(email);

        if(member == null) {
            throw new IllegalStateException("등록된 이메일이 아닙니다.");
        }

        String tokenCode = this.getTokenCode(email);
        LocalDateTime tokenExpireDate = LocalDateTime.now().plusHours(1);

        AuthToken authToken = new AuthToken();
        authToken.setMember(member);
        authToken.setCode(tokenCode);
        authToken.setExpireDate(tokenExpireDate);

        return authTokenRepository.save(authToken);
    }

    public void invalidateToken(String email) {
        AuthToken authToken = this.getTokenByEmail(email);
        authToken.setUseYn("Y");

        authTokenRepository.save(authToken);
    }

    public boolean validateExpireToken(String email, String code) {
        AuthToken authToken = this.getTokenByEmail(email);

        if(!code.equals(authToken.getCode())
        || authToken.getUseYn().equals("Y")
        || authToken.getExpireDate().isBefore(LocalDateTime.now())) {
            return false;
        }

        return true;
    }

}
