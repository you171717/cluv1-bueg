package com.shop.entity;

import com.shop.dto.AddressDto;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Address {

    @Id
    @Column(name="address_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private String name;

    private String phone;

    private String address;

    private String addressDetail;

    public static Address createAddress(Member member, AddressDto addressDto) {
        Address address = new Address();
        address.setMember(member);
        address.setName(addressDto.getName());
        address.setPhone(addressDto.getPhone());
        address.setAddress(addressDto.getAddress());
        address.setAddressDetail(addressDto.getAddress_detail());

        return address;
    }

}
