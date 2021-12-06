package com.shop.controller;

import com.shop.constant.Bank;
import com.shop.dto.MemberFormDto;
import com.shop.dto.MemberUpdateFormDto;
import com.shop.entity.Member;
import com.shop.entity.OAuth2Member;
import com.shop.mapstruct.MemberUpdateFormMapper;
import com.shop.repository.MemberRepository;
import com.shop.repository.OAuth2MemberRepository;
import com.shop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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

    @GetMapping(value = "/new")
    public String memberForm(Model model) {
        model.addAttribute("banks", Bank.values());
        model.addAttribute("memberFormDto", new MemberFormDto());
        return "member/memberForm";
    }

    @PostMapping(value = "/new")
    public String memberForm(@Valid MemberFormDto memberFormDto, BindingResult bindingResult, Model model) {
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

}
