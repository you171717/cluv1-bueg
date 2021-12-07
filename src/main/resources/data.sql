INSERT INTO cluv1.category (cate_code, cate_name) VALUES (1,"레드"), (2,"화이트"), (3,"스파클링"), (4,"로제"), (5,"기타");
INSERT INTO cluv1.faq (`faq_id`,`answer`,`question`) VALUES (1,'상품이 누락되었다면 교환을 통해 상품을 다시 받거나, 반품하고 환불받을 수 있습니다.','[상품누락] 상품을 구매했는데 일부만 배송되었어요.');
INSERT INTO cluv1.faq (`faq_id`,`answer`,`question`) VALUES (2,'갑작스러운 주문량 증가 등 예상치 못한 상황이 발생할 경우 \'상품준비중\' 단계가 길어질 수 있습니다.','[배송일정] 주문한 상품의 배송상태가 계속 상품준비중으로 표시됩니다.');
INSERT INTO cluv1.faq (`faq_id`,`answer`,`question`) VALUES (3,'상품이 파손되어 배송된 경우 교환 및 반품을 신청하실 수 있습니다.','[상품파손] 배송 받은 상품이 파손되었을 경우 어떻게 해야 하나요?');
INSERT INTO cluv1.faq (`faq_id`,`answer`,`question`) VALUES (4,'주문하신 상품은 결제완료 후 안내된 배송예정일까지 배송됩니다. ','[배송일정] 주문한 상품은 언제 배송되나요 ?');
INSERT INTO cluv1.faq (`faq_id`,`answer`,`question`) VALUES (5,'회원정보 수정에서 환불계좌 수정이 가능합니다. ','[환불계좌] 환불계좌를 등록/변경하고 싶어요.');
INSERT INTO cluv1.faq (`faq_id`,`answer`,`question`) VALUES (6,'주문자 본인이 아닌 경우 정보 제공에 제한이 있으며 교환/반품 처리가 가능하지 않습니다. ','[교환 반품] 선물 받은 상품에 문제가 있습니다.');
INSERT INTO cluv1.tag (`tag_id`, `tag_content`, `tag_nm`, `total_sell`) VALUES ('1', '거실관련 가구', '거실', '50'), ('2', '침실관련 가구', '침실', '20'), ('3', '1인 전용 가구', '1인', '60'), ('4', '다수 인원용 가구', '4인', '22'), ('5', '계절별 가구_봄', '봄', '23'), ('6', '계절별 가구_여름', '여름', '55'), ('7', '계절별 가구_가을', '가을', '25'), ('8', '계절별 가구_겨울', '겨울', '23');