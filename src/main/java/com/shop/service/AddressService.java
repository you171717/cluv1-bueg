package com.shop.service;

import com.shop.dto.AddressDto;
import com.shop.entity.Address;
import com.shop.entity.Member;
import com.shop.repository.AddressRepository;
import com.shop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;
    private final MemberRepository memberRepository;

    public Long saveAddress(AddressDto addressDto, String email) {
        Member member = memberRepository.findByEmail(email);
        Address address = Address.createAddress(member, addressDto);

        addressRepository.save(address);

        return address.getId();
    }

}
