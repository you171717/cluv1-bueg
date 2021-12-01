package com.shop.controller;

import com.shop.constant.BidDepositType;
import com.shop.dto.BidDto;
import com.shop.dto.BidSearchDto;
import com.shop.dto.ReverseAuctionDto;
import com.shop.entity.Bid;
import com.shop.entity.Member;
import com.shop.repository.BidRepository;
import com.shop.repository.MemberRepository;
import com.shop.service.BidService;
import com.shop.service.ReverseAuctionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.security.Principal;
import java.text.DecimalFormat;
import java.util.Map;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class BidController {

    private final MemberRepository memberRepository;

    private final ReverseAuctionService reverseAuctionService;

    private final BidService bidService;

    private final BidRepository bidRepository;

    @GetMapping(value = "/bid/{reverseAuctionId}")
    public String bidPayment(@PathVariable("reverseAuctionId") Long reverseAuctionId, Principal principal, Model model) {
        Member member = memberRepository.findByEmail(principal.getName());
        ReverseAuctionDto reverseAuctionDto = reverseAuctionService.getReverseAuctionDtl(reverseAuctionId);

        model.addAttribute("member", member);
        model.addAttribute("reverseAuctionDto", reverseAuctionDto);

        return "bid/bidPayment";
    }

    @PostMapping(value = "/bid/{reverseAuctionId}")
    public String bidPaymentProcess(@PathVariable("reverseAuctionId") Long reverseAuctionId, @RequestParam Map<String, String> paramMap, Principal principal, Model model) {
        BidDepositType bidDepositType = BidDepositType.valueOf(paramMap.get("bidDepositType").toUpperCase());

        Member member = memberRepository.findByEmail(principal.getName());

        Bid bid;

        if(bidDepositType.equals(BidDepositType.KAKAO_PAY)) {
            Long payId = Long.parseLong(paramMap.get("tid").substring(1));

            bid = bidService.saveBidWithPayment(member.getEmail(), reverseAuctionId, payId);
        } else {
            bid = bidService.saveBid(member.getEmail(), reverseAuctionId);
        }

        model.addAttribute("bid", bid);

        return "bid/bidComplete";
    }

    @GetMapping(value = {"/bids","/bids/{page}"})
    public String bidList(BidSearchDto bidSearchDto, @PathVariable("page") Optional<Integer> page, Principal principal, Model model) {
        Member member = memberRepository.findByEmail(principal.getName());

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 5);
        Page<BidDto> bidDtoList = bidService.getUserBidPage(principal.getName(), bidSearchDto, pageable);

        model.addAttribute("bidDtoList", bidDtoList);
        model.addAttribute("bidSearchDto", bidSearchDto);
        model.addAttribute("maxPage", 5);
        model.addAttribute("depositName", bidService.getUniqueDepositName(member));

        return "bid/bidList";
    }

    @GetMapping(value = {"/admin/bids","/admin/bids/{page}"})
    public String bidMng(BidSearchDto bidSearchDto, @PathVariable("page") Optional<Integer> page, Model model) {
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 10);
        Page<BidDto> bidDtoList = bidService.getAdminBidPage(bidSearchDto, pageable);

        model.addAttribute("bidDtoList", bidDtoList);
        model.addAttribute("bidSearchDto", bidSearchDto);
        model.addAttribute("maxPage", 10);

        return "bid/bidMng";
    }

    @GetMapping(value = "/admin/bid/{bidId}/approve")
    public String bidApprove(@PathVariable("bidId") Long bidId) {
        bidService.approveBid(bidId);

        return "redirect:/admin/bids";
    }

    @GetMapping(value = "/admin/bid/{bidId}/refund")
    public @ResponseBody ResponseEntity bidRefund(@PathVariable("bidId") Long bidId) {
        try {
            Bid bid = bidRepository.findById(bidId).orElseThrow(EntityNotFoundException::new);
            Member member = bid.getMember();

            DecimalFormat decimalFormat = new DecimalFormat("#,###");

            String result = "계좌 번호 : " + "(" + member.getRefundBank() + ") " + member.getRefundAccount()
                    + "\n입금자명 : " + bid.getDepositName()
                    + "\n환불 금액 : ₩" + decimalFormat.format(bid.getDepositAmount());

            return new ResponseEntity<String>(result, HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity<String>("환불 정보를 찾을 수 없습니다.", HttpStatus.BAD_REQUEST);
        }
    }

}
