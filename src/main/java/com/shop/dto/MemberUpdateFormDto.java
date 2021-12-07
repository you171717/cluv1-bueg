package com.shop.dto;

import com.shop.constant.Bank;
import com.shop.constant.NoticeType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class MemberUpdateFormDto {

    @NotBlank(message = "이름은 필수 입력 값입니다.")
    private String name;

    @NotEmpty(message = "이메일은 필수 입력 값입니다.")
    @Email(message = "이메일 형식으로 입력해주세요.")
    private String email;

    private String password;

    @NotEmpty(message = "주소는 필수 입력 값입니다.")
    private String address;

    @NotEmpty(message = "상세 주소는 필수 입력 값입니다.")
    private String addressDetail;

    @NotEmpty(message = "휴대폰 번호는 필수 입력 값입니다.")
    private String phone;

    @NotNull(message = "환불 은행은 필수 입력 값입니다.")
    private Bank refundBank;

    @NotEmpty(message = "환불 계좌 번호는 필수 입력 값입니다.")
    private String refundAccount;

    @NotNull(message = "알림 서비스 종류는 필수 입력 값입니다.")
    private NoticeType noticeType;

}
