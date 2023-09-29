package src.main;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class MovieNameGenerator {
    public static void main(String[] args){
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("[System] Start");
        String originalUrl = "https://search.naver.com/search.naver?query=영화+";
        System.out.print("찾는 영화 제목 입력 : ");

        try {
            // 키워드 + 검색 URL 만들기
            String keyWord = br.readLine();
            String url = originalUrl + keyWord;

            // 페이지 정보 크롤링
            Connection conn = Jsoup.connect(url);
            Document document = conn.get();

            // String 정보 get
            String movieTitle = document.getElementsByClass("area_text_title").select("strong").text();
            String[] movieSubInfos = document.getElementsByClass("sub_title").text().split(" ");
            String movieSubTitle = "";
            String movieYear = "";

            int repeat = movieSubInfos.length;

            // 영화 제목 + 년도
            for (int i = 1; i < repeat; i++) {
                if (i == repeat - 1) {  // 마지막 String 값엔 연도가 들어있음
                    try {  // 근데 간혹 개봉하지 않은 영화는 연도 데이터 없음, 없는 경우엔 임의값(0000)으로 넣고 진행
                        int temp = Integer.parseInt(movieSubInfos[repeat - 1]);
                        if (0 < temp) movieYear = movieSubInfos[repeat - 1];
                    } catch(Exception e) {
                        movieSubTitle += movieSubInfos[repeat - 1];
                        movieYear = "0000";
                    }

                    break;  // 마지막이니 movieSubTitle에 추가해줄 것 없이 break;
                }

                movieSubTitle = movieSubTitle + movieSubInfos[i] + " ";
            }

            String movieFolderName = movieTitle + "(" + movieSubTitle.trim() + ", " + movieYear + ")";
            movieFolderName = checkNameValidate(movieFolderName);

            System.out.println(movieFolderName);
        } catch (Exception e) {
            System.out.println("[Exception] Error Occur");
            e.printStackTrace();
        }
    }

    // 폴더 이름에 사용할 수 없는 특수문자 제거
    public static String checkNameValidate(String folderName) {
        return folderName.replaceAll("[\\\\/:*?\"<>|]", "");
    }
}

//스파이더맨: 어크로스 더 유니버스(Spider-Man: Across the Spider-Verse, 2023)
// -> 스파이더맨 어크로스 더 유니버스(Spider-Man Across the Spider-Verse, 2023)
