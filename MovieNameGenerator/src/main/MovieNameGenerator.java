package src.main;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MovieNameGenerator {
    public static String movieTextFileName = "movieNameList.txt";  // 검색할 영화 이름 입력할 텍스트 파일
    public static String originalUrl = "https://search.naver.com/search.naver?query=영화+";  // 네이버 기본 영화 검색 URL


    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("[System][영화 이름 생성기]");

        createTextFile();  // 파일 생성
        
        System.out.println("[System] 생성된 메모장에 검색하고자 하는 영화 제목을 입력하고 저장한 뒤 -를 입력할 것");

        while (true) {
            String breakPoint = br.readLine();
            if (breakPoint.equals("-")) break;
            else System.out.println("[System] -가 아닙니다. 다시 입력하세요 : ");
        }

        List<String> movieInfos = readTextFile();  // 텍스트 파일에서 검색하려는 영화 제목 읽어오기

        // 메서드에서 출력과정을 처리해두면 나중에 어디서 출력되는지 찾아야하는 번거로움이 있음
        // 기능을 제외한 모든 출력은 main문 안에서 해결할 것
        for (String movieName : movieInfos) System.out.println(movieName);

        System.out.println("[System] 프로그램을 종료 시퀀스 : - 입력");
        while (true) {
            String breakPoint = br.readLine();
            if (breakPoint.equals("-")) break;
            else System.out.println("[System] 프로그램을 종료하려면 - 입력 후 엔터를 입력해주세요 ");
        }
    }


    // 텍스트 파일 생성
    public static void createTextFile() {
        File fileToDelete = new File(movieTextFileName);

        if (fileToDelete.exists()) {  // 파일이 존재하면 파일 삭제 시도 진행
            if (fileToDelete.delete()) System.out.println("[createTextFile][DeleteSuccess]기존 파일 삭제 성공: " + movieTextFileName);
            else System.err.println("[createTextFile][DeleteFail] 기존 파일 삭제 실패: " + movieTextFileName);
        }

        try {  // 파일 생성 시도
            if (fileToDelete.createNewFile()) System.out.println("[createTextFile][CreateSuccess] 새 파일 생성 성공: " + movieTextFileName);
            else System.err.println("[createTextFile][CreateFail] 새 파일 생성 실패: " + movieTextFileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // 텍스트 파일 읽어오기
    public static List<String> readTextFile() throws FileNotFoundException {
        BufferedReader br = new BufferedReader(new FileReader(movieTextFileName));
        List<String> movieInfos = new ArrayList<String>();

        try {
            String movieName;
            while ((movieName = br.readLine()) != null) {
                movieInfos.add(movieNameGenerator(movieName.trim()));  // 영화 제목
            }
        } catch (IOException e) {
            System.err.println("[readTextFile][IOException] 파일 읽기 중 오류 발생: " + e.getMessage());
        }

        return movieInfos;
    }


    // 영화 이름 생성기
    public static String movieNameGenerator(String findMovieName) {
        System.out.println("[System] 영화 제목 검색 기능 시작");
        
        String movieFolderName = findMovieName;

        try {
            // 키워드 + 검색 URL 만들기
            String url = originalUrl + findMovieName;

            // 페이지 정보 크롤링
            Connection conn = Jsoup.connect(url);
            Thread.sleep(500);  // 페이지 다 불러오기도 전에 읽어들이니 여기서 터짐 (interval 줌)
            Document document = conn.get();

            // String 정보 get
            String movieTitle = document.getElementsByClass("area_text_title").select("strong").text();
            String[] movieSubInfos = document.getElementsByClass("sub_title").text().split(" ");
            String movieSubTitle = "";
            String movieYear = "";

            int repeat = movieSubInfos.length;

            // [영화제목]([영화 영문 제목], [영화 출시 년도]) 형식으로 영화 제목 세팅
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

            movieFolderName = movieTitle + "(" + movieSubTitle.trim() + ", " + movieYear + ")";
            movieFolderName = checkNameValidate(movieFolderName);

        } catch (Exception e) {
            System.out.println("[movieNameGenerator][Exception] Error Occur");
            e.printStackTrace();
        }

        return movieFolderName;
    }


    // 폴더 이름에 사용할 수 없는 특수문자 제거
    public static String checkNameValidate(String folderName) {
        return folderName.replaceAll("[\\\\/:*?\"<>|]", "");
    }
}

//스파이더맨: 어크로스 더 유니버스(Spider-Man: Across the Spider-Verse, 2023)
// -> 스파이더맨 어크로스 더 유니버스(Spider-Man Across the Spider-Verse, 2023)
