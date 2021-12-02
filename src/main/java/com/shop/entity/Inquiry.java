package com.shop.entity;

import com.shop.constant.Role;
import com.shop.dto.InquiryFormDto;
import com.shop.dto.MemberFormDto;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="inquiry")
@Getter @Setter
@ToString
@NoArgsConstructor
public class Inquiry extends BaseEntity{

    @Id
    @Column(name = "inquiry_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;

    private String content;


    @Builder
    public Inquiry(Long id, String title, String content) {
        this.id=id;
        this.title = title;
        this.content = content;
    }

    public static Inquiry createInquiry(InquiryFormDto inquiryFormDto){
        Inquiry inquiry =new Inquiry();
        inquiry.setTitle(inquiryFormDto.getTitle());
        inquiry.setContent(inquiryFormDto.getContent());
        return inquiry;
    }

    public void updateInquiry(InquiryFormDto inquiryRepository){
        this.title=inquiryRepository.getTitle();
        this.content=inquiryRepository.getContent();
    }


}
