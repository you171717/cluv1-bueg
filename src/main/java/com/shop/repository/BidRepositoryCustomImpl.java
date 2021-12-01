package com.shop.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shop.constant.BidDepositType;
import com.shop.constant.BidSearchSortColumn;
import com.shop.dto.BidDto;
import com.shop.dto.BidSearchDto;
import com.shop.dto.QBidDto;
import com.shop.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.persistence.EntityManager;
import java.util.List;

public class BidRepositoryCustomImpl implements BidRepositoryCustom {

    private JPAQueryFactory queryFactory;

    public BidRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    private BooleanExpression searchByLike(String searchQuery) {
        return QBid.bid.reverseAuction.item.itemNm.like("%" + searchQuery + "%");
    }

    private BooleanExpression searchByApprovedYn(String searchApprovedYn, QBid bid2) {
        return new CaseBuilder()
                .when(bid2.approvedYn.isNull())
                .then(QBid.bid.approvedYn)
                .otherwise("F")
                .like("%" + searchApprovedYn + "%");
    }

    private BooleanExpression searchDepositTypeEq(BidDepositType bidDepositType) {
        return bidDepositType == null ? null : QBid.bid.depositType.eq(bidDepositType);
    }

    private OrderSpecifier orderBy(BidSearchDto bidSearchDto) {
        BidSearchSortColumn sortColumn = bidSearchDto.getSortColumn();
        Sort.Direction sortDirection = bidSearchDto.getSortDirection();

        OrderSpecifier orderSpecifier = null;

        if(sortColumn.equals(BidSearchSortColumn.REG_TIME)) {
            if(sortDirection.isAscending()) {
                orderSpecifier = QBid.bid.regTime.asc();
            } else {
                orderSpecifier = QBid.bid.regTime.desc();
            }
        } else if(sortColumn.equals(BidSearchSortColumn.NAME)) {
            if(sortDirection.isAscending()) {
                orderSpecifier = QBid.bid.reverseAuction.item.itemNm.asc();
            } else {
                orderSpecifier = QBid.bid.reverseAuction.item.itemNm.desc();
            }
        } else if(sortColumn.equals(BidSearchSortColumn.PRICE)) {
            if(sortDirection.isAscending()) {
                orderSpecifier = QBid.bid.depositAmount.asc();
            } else {
                orderSpecifier = QBid.bid.depositAmount.desc();
            }
        }

        return orderSpecifier;
    }

    @Override
    public Page<BidDto> getAdminBidPage(BidSearchDto bidSearchDto, Pageable pageable) {
        QReverseAuction reverseAuction = QReverseAuction.reverseAuction;
        QItemImg itemImg = QItemImg.itemImg;
        QMember member = QMember.member;
        QItem item = QItem.item;
        QBid bid = QBid.bid;
        QBid bid2 = new QBid("bid2");

        QueryResults<BidDto> results = queryFactory
                .select(
                        new QBidDto(
                                bid.id,
                                itemImg.imgUrl,
                                item.itemNm,
                                member.email,
                                bid.depositType,
                                bid.depositName,
                                bid.depositAmount,
                                new CaseBuilder()
                                        .when(bid2.approvedYn.isNull())
                                        .then(bid.approvedYn)
                                        .otherwise("F"),
                                bid.approvedTime,
                                bid.regTime
                        )
                )
                .from(bid)
                .leftJoin(bid2).on(
                        bid2.reverseAuction
                                .eq(bid.reverseAuction)
                                .and(bid2.approvedYn.eq("Y"))
                                .and(bid2.id.ne(bid.id))
                )
                .join(bid.member, member)
                .join(bid.reverseAuction, reverseAuction)
                .join(bid.reverseAuction.item, item)
                .join(itemImg).on(itemImg.item.eq(bid.reverseAuction.item).and(itemImg.repImgYn.eq("Y")))
                .where(searchByLike(bidSearchDto.getSearchQuery()))
                .where(searchByApprovedYn(bidSearchDto.getSearchApprovedYn(), bid2))
                .where(searchDepositTypeEq(bidSearchDto.getSearchDepositType()))
                .orderBy(this.orderBy(bidSearchDto))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<BidDto> content = results.getResults();

        long total = results.getTotal();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<BidDto> getUserBidPage(Member curMember, BidSearchDto bidSearchDto, Pageable pageable) {
        QReverseAuction reverseAuction = QReverseAuction.reverseAuction;
        QItemImg itemImg = QItemImg.itemImg;
        QMember member = QMember.member;
        QItem item = QItem.item;
        QBid bid = QBid.bid;
        QBid bid2 = new QBid("bid2");

        QueryResults<BidDto> results = queryFactory
                .select(
                        new QBidDto(
                                bid.id,
                                itemImg.imgUrl,
                                item.itemNm,
                                member.email,
                                bid.depositType,
                                bid.depositName,
                                bid.depositAmount,
                                new CaseBuilder()
                                        .when(bid2.approvedYn.isNull())
                                        .then(bid.approvedYn)
                                        .otherwise("F"),
                                bid.approvedTime,
                                bid.regTime
                        )
                )
                .from(bid)
                .leftJoin(bid2).on(
                        bid2.reverseAuction
                                .eq(bid.reverseAuction)
                                .and(bid2.approvedYn.eq("Y"))
                                .and(bid2.id.ne(bid.id))
                )
                .join(bid.member, member)
                .join(bid.reverseAuction, reverseAuction)
                .join(bid.reverseAuction.item, item)
                .join(itemImg).on(itemImg.item.eq(bid.reverseAuction.item).and(itemImg.repImgYn.eq("Y")))
                .where(searchByLike(bidSearchDto.getSearchQuery()))
                .where(searchByApprovedYn(bidSearchDto.getSearchApprovedYn(), bid2))
                .where(bid.member.eq(curMember))
                .orderBy(this.orderBy(bidSearchDto))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<BidDto> content = results.getResults();

        long total = results.getTotal();

        return new PageImpl<>(content, pageable, total);
    }

}