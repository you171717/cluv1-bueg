package com.shop.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shop.dto.*;
import com.shop.entity.QItem;
import com.shop.entity.QItemImg;
import com.shop.entity.QUsedItem;
import com.shop.entity.QUsedItemImg;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public class UsedItemRepositoryCustomImpl implements UsedItemRepositoryCustom {

    private JPAQueryFactory queryFactory;

    @Override
    public Page<UsedItemDto> getUsedItemPage(UsedItemSearchDto usedItemSearchDto, Pageable pageable) {
        QUsedItem usedItem = QUsedItem.usedItem;
        QUsedItemImg usedItemImg = QUsedItemImg.usedItemImg;

        QueryResults<UsedItemDto> results = queryFactory
                .select(
                        new QUsedItemDto(
                                usedItem.id,
                                usedItem.itemName,
                                usedItem.detail,
                                usedItemImg.imgUrl,
                                usedItem.price,
                                usedItem.startDay,
                                usedItem.endDay
                        )
                )
                .from(usedItemImg)
                .join(usedItemImg.usedItem, usedItem)
                .where(usedItemImg.repimgYn.eq("Y"))
                // now() 에서 Date() 로 변경 / 이유: 시연을 위해서라면 DB에서 가져온 시간의 값이 아닌  로컬에서 가져온 시간의 값이 필요하기 때문이다.
                // now() : DB의 시간 조회 / Date() : 현재 로컬의 시간을 조회
                .where(usedItem.startDay.loe((Expression<LocalDateTime>) new Date()))
                .where(usedItem.endDay.goe((Expression<LocalDateTime>) new Date()))
                .where(itemNameLike(usedItemSearchDto.getSearchQuery()))
                .orderBy(usedItem.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<UsedItemDto> content = results.getResults();
        long total = results.getTotal();
        return new PageImpl<>(content, pageable, total);
    }

    private BooleanExpression itemNameLike(String searchQuery){
        return StringUtils.isEmpty(searchQuery) ? null : QUsedItem.usedItem.itemName.like("%" + searchQuery + "%");
    }
}
