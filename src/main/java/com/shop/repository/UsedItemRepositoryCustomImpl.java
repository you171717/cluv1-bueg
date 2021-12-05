package com.shop.repository;

import com.querydsl.core.QueryResults;
import com.shop.dto.ItemSearchDto;
import com.shop.dto.MainItemDto;
import com.shop.dto.QMainItemDto;
import com.shop.dto.UsedItemDto;
import com.shop.entity.QItem;
import com.shop.entity.QItemImg;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;

public class UsedItemRepositoryCustomImpl {
    @Override
    public Page<UsedItemDto> getUsedItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {

        QItem item = QItem.item;
        QItemImg itemImg = QItemImg.itemImg;

        QueryResults<MainItemDto> results = queryFactory
                .select(
                        new QMainItemDto(
                                item.id,
                                item.itemNm,
                                item.itemDetail,
                                itemImg.imgUrl,
                                item.price)
                )
                .from(itemImg)
                .join(itemImg.item, item)
                .where(itemImg.repimgYn.eq("Y"))
                .where(item.useItemStatus.eq(UseItemStatus.valueOf("OLD")))
                // now() 에서 Date() 로 변경 / 이유: 시연을 위해서라면 DB에서 가져온 시간의 값이 아닌  로컬에서 가져온 시간의 값이 필요하기 때문이다.
                // now() : DB의 시간 조회 / Date() : 현재 로컬의 시간을 조회
                .where(item.start_day.loe(new Date()))
                .where(item.end_day.goe(new Date()))
                .where(itemNmLike(itemSearchDto.getSearchQuery()))
                .orderBy(item.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();
        List<MainItemDto> content = results.getResults();
        long total = results.getTotal();
        return new PageImpl<>(content, pageable, total);
    }
}
