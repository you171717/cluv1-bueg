### Intern Project Framework Setting
1. H2 설치
   1. https://www.h2database.com/ -> 다운로드 및 설치
   2. `jdbc:h2:~/intern` -> 한번 수행
   3. `~/intern.mv.db` 파일 생성 확인 
   4. 이후 부터는 `jdbc:h2:tcp://localhost/~/intern` 이렇게 접속

2. com.gsitm.intern.test 패키지는 db 접속을 위한 test용 패키지입니다.