package com.shop.controller;

import com.shop.constant.Bank;
import com.shop.dto.MemberFormDto;
import com.shop.dto.MemberUpdateFormDto;
import com.shop.entity.AuthToken;
import com.shop.entity.Member;
import com.shop.entity.OAuth2Member;
import com.shop.mapstruct.MemberUpdateFormMapper;
import com.shop.repository.MemberRepository;
import com.shop.repository.OAuth2MemberRepository;
import com.shop.service.AuthTokenService;
import com.shop.service.EmailService;
import com.shop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.security.Principal;

@RequestMapping("/members")
@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberUpdateFormMapper memberUpdateFormMapper;
    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final OAuth2MemberRepository oAuth2MemberRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final AuthTokenService authTokenService;

    @GetMapping(value = "/new")
    public String memberForm(Model model) {
        model.addAttribute("banks", Bank.values());
        model.addAttribute("memberFormDto", new MemberFormDto());
        return "member/memberForm";
    }

    @PostMapping(value = "/new")
    public String memberForm(@Valid MemberFormDto memberFormDto, BindingResult bindingResult, Model model, HttpSession httpSession) {

        //생성한 인증코드와 사용자가 입력한 인증코드 비교확인
        if(!StringUtils.equals(memberFormDto.getCode(), httpSession.getAttribute("authCode"))){
            FieldError fieldError = new FieldError("memberFormDto", "code", "인증코드가 같지 않습니다.");
            bindingResult.addError(fieldError);
        }
        if(bindingResult.hasErrors()) {
            model.addAttribute("banks", Bank.values());

            return "member/memberForm";
        }

        try {
            Member member = Member.createMember(memberFormDto, passwordEncoder);
            memberService.saveMember(member);
        } catch(IllegalStateException e) {
            model.addAttribute("banks", Bank.values());
            model.addAttribute("errorMessage", e.getMessage());

            return "member/memberForm";
        }

        return "redirect:/members/login";
    }

    @GetMapping(value = "/login")
    public String loginMember() {
        return "/member/memberLoginForm";
    }

    @GetMapping(value = "/login/error")
    public String loginError(Model model) {
        model.addAttribute("loginErrorMsg", "아이디 또는 비밀번호를 확인해주세요");
        return "/member/memberLoginForm";
    }

    @GetMapping(value = "/update")
    public String updateMemberForm(Principal principal, Model model) {
        Member member = memberRepository.findByEmail(principal.getName());
        OAuth2Member oAuth2Member = oAuth2MemberRepository.findByMember(member);

        MemberUpdateFormDto memberUpdateFormDto = memberUpdateFormMapper.toDto(member);
        memberUpdateFormDto.setPassword("");

        model.addAttribute("banks", Bank.values());
        model.addAttribute("memberUpdateFormDto", memberUpdateFormDto);
        model.addAttribute("oAuth2Member", oAuth2Member);

        return "member/memberUpdateForm";
    }

    @PostMapping(value = "/update")
    public String updateMember(@Valid MemberUpdateFormDto memberUpdateFormDto, Principal principal, BindingResult bindingResult, Model model) {
        Member member = memberRepository.findByEmail(principal.getName());
        OAuth2Member oAuth2Member = oAuth2MemberRepository.findByMember(member);

        model.addAttribute("oAuth2Member", oAuth2Member);

        if(!memberUpdateFormDto.getPassword().equals("")) {
            String password = memberUpdateFormDto.getPassword();

            if(password.length() < 8 || password.length() > 16) {
                FieldError fieldError = new FieldError("memberUpdateFormDto", "password", "비밀번호는 8자 이상, 16자 이하로 입력해주세요.");
                bindingResult.addError(fieldError);
            }
        }

        if(bindingResult.hasErrors()) {
            model.addAttribute("banks", Bank.values());

            return "member/memberUpdateForm";
        }

        try {
            member.setName(memberUpdateFormDto.getName());
            member.setAddress(memberUpdateFormDto.getAddress());
            member.setRefundBank(memberUpdateFormDto.getRefundBank());
            member.setRefundAccount(memberUpdateFormDto.getRefundAccount());

            if(oAuth2Member == null && !memberUpdateFormDto.getPassword().equals(""))
                member.setPassword(passwordEncoder.encode(memberUpdateFormDto.getPassword()));

            memberRepository.save(member);
        } catch(IllegalStateException e) {
            model.addAttribute("banks", Bank.values());
            model.addAttribute("errorMessage", e.getMessage());

            return "member/memberUpdateForm";
        }

        return "redirect:/";
    }

    @GetMapping(value ="/findPassword")
    public String findPassword(){
        return "member/findPassword";
    }

    @PostMapping(value ="/findPassword")
    public String sendEmail(String email, String name, Model model) {
        try {
            memberService.checkEmailAndName(email, name);

            // 비밀번호 변경 URL 이메일 전송
            emailService.sendPasswordEmail(email);
            // alert창 페이지 띄우고 redirect로 로그인 페이지로 이동
            model.addAttribute("message", "이메일을 확인하여 비밀번호를 변경해주세요.");
            model.addAttribute("location","/members/login");
        } catch(Exception e) {
            model.addAttribute("errorMessage", e.getMessage());

            return "member/findPassword";
        }

        return "redirect";
    }

    @GetMapping(value = "/updatePassword")
    public String readMemberInfo(@RequestParam("code") String code, @RequestParam("email") String email, Model model) {
        // URL에 포함된 코드 가져와서 코드로 일치하는 토큰 가져오기
        AuthToken authtoken = authTokenService.getTokenByCode(code);

        //토큰 상태 확인
        boolean expireYn = authTokenService.validateExpireToken(email, code);
        if(expireYn == false){
            throw new IllegalStateException("만료된 토큰입니다.");
        }

        authTokenService.invalidateToken(email);

        // 토큰에 있는 사용자 정보 중 ID만 가져오기
        Long memberId = authtoken.getMember().getId();

        // View에서 ID를 포함시켜서 PostMapping으로 값을 가져올 수 있게 하기
        model.addAttribute("memberId", memberId );

        return "member/updatePassword";
    }

    @PostMapping(value = "/updatePassword")
    public String updatePassword(Long memberId, String password, Model model) {
        try {
            // 비밀번호 변경
            memberService.updatePassword(memberId, password);
            model.addAttribute("message", "비밀번호가 변경되었습니다.");
            model.addAttribute("location","/members/login");

        } catch(Exception e) {
            model.addAttribute("errorMessage", e.getMessage());

            return "member/updatePassword";
        }
        return "redirect";
    }

    @PostMapping(value = "/signUpEmail")
    public @ResponseBody
    ResponseEntity AuthCodeEmail(String email, HttpSession httpSession){
        // 인증코드 메일로 전송
        emailService.sendEmailAuthCode(email, httpSession);

        return new ResponseEntity<String>("", HttpStatus.OK);
    }

}
