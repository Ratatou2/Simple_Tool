# Simple_Tool
내가 편하려고 or 재밌어 보여서 만들어둔 도구 모음

---
## 1. MovieNameGenerator (영화 이름 생성기)
- 영화 제목을 `[영화제목]([영화 영문 제목], [영화 출시 년도])` 순으로 기록해두고 싶음
- 매번 검색하고 긁어오기 귀찮음
- 그래서 크롤링해서 내가 원하는대로 조합해주는 검색기를 만들었다!

#### [issue]
1. 일단 비정상적인 용량 (jar 추출하고 exe 만들 때 뭔가가 문제인듯 
    => Launch4J 쓰면 커진다는 얘기가 있음 (추가 조사 필요)
2. jsoup.jar이 포함 안되어서 그런지 검색 결과 안나옴 (코드만 돌리면 나옴)
    => 수정 필요

---
## 2. telegram_alert-master(PJT Final ver) (텔레그램 알림툴)
- PID를 활용한 텔레그램 알림 툴
- VPN등이 적용된 서버에서 동작시켜둔 프로세스가 끝났는지 매번 직접 확인하기 전까진 알수가 없음
- 상당히 불편하여서 PID(프로세스 ID)가 종료되면 텔레그램 알림을 받아 볼 수 있도록 알림 기능 제작
- 사용법 : https://blog.naver.com/ratatou2_/223484947069