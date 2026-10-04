
-- < 생성한 테이블의 필드를 기반하여 데이터를 추가하는 방법 >
-- question 테이블 데이터 
insert into question(subject, content, create_date) 
values('sbb가 무엇인가요?', 'sbb에 대해 알고 싶습니다.', now());

insert into question(subject, content, create_date) 
values('스프링 부트 모델 질문입니다.', 'id는 자동으로 생성되나요?', now());

insert into question(subject, content, create_date, author_id) 
values('엔터티는 무엇인가요?', '엔터티는 자동으로 테이블을 생성해주나요?', now());

insert into question(subject, content, create_date) 
values('JPA는 무엇인가요?', 'JPA는 SQL문을 자동으로 생성합니다.', now());

insert into question(subject, content, create_date) 
values('ORM은 무엇인가요?', '클래스와 테이블이 어떻게 연결이 되나요?', now());

insert into question(subject, content, create_date) 
values('타임리프는 무엇인가요?', '타임리프는 스프링부트에서 사용하는 서버사이드 엔진입니다.?', now());

-- customer 테이블 데이터
/*
insert into customer(customer_id, name, gender, age, phone, email, address) 
values('aaaa', '황동만', '남성', 40, '010-1111-1111', 'aaaa@naver.com', '서울시 관악구 남부순환로 1800');

insert into customer(customer_id, name, gender, age, phone, email, address) 
values('bbbb', '변은아', '여성', 32, '010-2222-2222', 'bbbb@naver.com', '서울시 성동구 독서당로 300');

insert into customer(customer_id, name, gender, age, phone, email, address) 
values('cccc', '박경세', '남성', 40, '010-3333-3333', 'cccc@naver.com', '서울시 종로구 자하문로 10');

insert into customer(customer_id, name, gender, age, phone, email, address) 
values('dddd', '장미란', '여성', 35, '010-4444-4444', 'dddd@naver.com', '인천시 송파구 백제고분로 234');

insert into customer(customer_id, name, gender, age, phone, email, address) 
values('eeee', '오정희', '여성', 55, '010-5555-5555', 'eeee@naver.com', '인천시 강북구 한천로 911');

insert into customer(customer_id, name, gender, age, phone, email, address) 
values('ffff', '이익준', '남성', 35, '010-6666-6666', 'ffff@naver.com', '수원시 성북구 동소문로 63');

insert into customer(customer_id, name, gender, age, phone, email, address) 
values('gggg', '김준완', '남성', 40, '010-7777-7777', 'gggg@naver.com', '수원시 성북구 보문로 168');

insert into customer(customer_id, name, gender, age, phone, email, address) 
values('hhhh', '채송화', '여성', 40, '010-8888-8888', 'hhhh@naver.com', '고양시 광진구 자양로 117');

insert into customer(customer_id, name, gender, age, phone, email, address) 
values('iiii', '양석형', '남성', 32, '010-9999-9999', 'iiii@naver.com', '과천시 성동구 고산자로 270');

insert into customer(customer_id, name, gender, age, phone, email, address) 
values('jjjj', '안정원', '남성', 52, '010-1234-1234', 'jjjj@naver.com', '남양주시 양초구 양초대로 201');

insert into customer(customer_id, name, gender, age, phone, email, address) 
values('kkkk', '추민하', null, null, null, null, null);
*/