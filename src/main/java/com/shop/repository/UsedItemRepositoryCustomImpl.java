package com.shop.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shop.constant.UsedItemSellStatus;
import com.shop.dto.QUsedItemDto;
import com.shop.dto.UsedItemDto;
import com.shop.dto.UsedItemSearchDto;
import com.shop.entity.QUsedItem;
import com.shop.entity.QUsedItemImg;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

public class UsedItemRepositoryCustomImpl implements UsedItemRepositoryCustom {

    private JPAQueryFactory queryFactory;

    public UsedItemRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    private BooleanExpression usedItemNameLike(String searchQuery){
        return StringUtils.isEmpty(searchQuery) ? null : QUsedItem.usedItem.name.like("%" + searchQuery + "%");
    }

    @Override
    public Page<UsedItemDto> getAllUsedItemPage(UsedItemSearchDto usedItemSearchDto, Pageable pageable) {
        QUsedItem usedItem = QUsedItem.usedItem;
        QUsedItemImg usedItemImg = QUsedItemImg.usedItemImg;

        QueryResults<UsedItemDto> results = queryFactory
                .select(
                        new QUsedItemDto(
                                usedItem.id,
                                usedItem.name,
                                usedItem.detail,
                                usedItem.usedItemSellStatus,
                                usedItemImg.imgUrl,
                                usedItem.price,
                                usedItem.endTime
                        )
                )
                .from(usedItemImg)
                .join(usedItemImg.usedItem, usedItem)
                .where(usedItemImg.repimgYn.eq("Y"))
                .where(usedItemNameLike(usedItemSearchDto.getSearchQuery()))
                .where(usedItem.endTime.goe(LocalDateTime.now()))
                .where(usedItem.usedItemSellStatus.ne(UsedItemSellStatus.SOLD_OUT))
                .orderBy(usedItem.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<UsedItemDto> content = results.getResults();

        long total = results.getTotal();

        return new PageImpl<>(content, pageable, total);
    }

    public Page<UsedItemDto> getUserUsedItemPage(String email, UsedItemSearchDto usedItemSearchDto, Pageable pageable) {
        QUsedItem usedItem = QUsedItem.usedItem;
        QUsedItemImg usedItemImg = QUsedItemImg.usedItemImg;

        QueryResults<UsedItemDto> results = queryFactory
                .select(
                        new QUsedItemDto(
                                usedItem.id,
                                usedItem.name,
                                usedItem.detail,
                                usedItem.usedItemSellStatus,
                                usedItemImg.imgUrl,
                                usedItem.price,
                                usedItem.endTime
                        )
                )
                .from(usedItemImg)
                .join(usedItemImg.usedItem, usedItem)
                .where(usedItemImg.repimgYn.eq("Y"))
                .where(usedItem.owner.email.eq(email))
                .where(usedItemNameLike(usedItemSearchDto.getSearchQuery()))
                .orderBy(usedItem.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<UsedItemDto> content = results.getResults();

        long total = results.getTotal();

        return new PageImpl<>(content, pageable, total);
    }

}
