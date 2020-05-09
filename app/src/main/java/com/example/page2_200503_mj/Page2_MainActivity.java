package com.example.page2_200503_mj;

import android.content.Intent;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com_page2_1.Page2_1_MainActivity;
import com_page2_1.Recycler_item;
import com_page2_1.course;


public class Page2_MainActivity extends AppCompatActivity {

    ArrayList<course> items = new ArrayList<>();

    //리사이클러뷰에 연결할 데이터
    List<Recycler_item> cardview_items = new ArrayList<>();


    Page2_MainActivity mainActivity;
    private String  subject;
    private String contentTypeId, cat1, cat2;

    //역 이름을 받아서 지역코드랑 시군구코드 받기 위한 배열
    int station_code = 49;
    String[] arr_line = null;
    String[] _name = new String[station_code];           //txt에서 받은 역이름
    String[] _areaCode = new String[station_code];       //txt에서 받은 지역코드
    String[] _sigunguCode = new String[station_code];    //txt에서 받은 시군구코드
    String  areaCode, sigunguCode;
    String returnResult, url;

    String name_1[];  //returnResult를 줄바꿈 단위로 쪼개서 넣은 배열/ name_1[0]에는 한 관광지의 이름,url,contentId,위치가 다 들어가 있다.
    String name_2[] = new String[3];  //name_1를 "  " 단위로 쪼개서 넣은 배열/ [0]= contentID/ [1]=mapx/ [2]에= mapy/ [3]= img_Url/ [4]= name이 들어가 있다.
//    int length = name_1.length;


    //xml 파싱한 값을 분류해서 쪼개 넣음
    String name[] = new String[99999];        //관광지 이름
    String img_Url[] = new String[9999];     //이미지 URL
    String contentid[] = new String[99999];   //관광지ID



    //page2 코스 텍스트
    TextView t1, t2, t3, t4, t5, t6, t7, t8;

   TextView subject_title;

   //page2 코스 더보기
    TextView courseMore;


    //관광지 주제별 코스를 저장하는 배열
    String getSubject;
    String[] st1;
    String[] st2;
    String[] st3;
    String[] st4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2_main);

        t1 = (TextView) findViewById(R.id.page2_course_txt1);
        t2 = (TextView) findViewById(R.id.page2_course_txt2);
        t3 = (TextView) findViewById(R.id.page2_course_txt3);
        t4 = (TextView) findViewById(R.id.page2_course_txt4);
        t5 = (TextView) findViewById(R.id.page2_course2_txt1);
        t6 = (TextView) findViewById(R.id.page2_course2_txt2);
        t7 = (TextView) findViewById(R.id.page2_course2_txt3);
        t8 = (TextView) findViewById(R.id.page2_course2_txt4);


        subject_title = (TextView) findViewById(R.id.page2_title);

        courseMore = (TextView) findViewById(R.id.page2_courseMore);

        //텍스트뷰 밑줄
        courseMore.setPaintFlags(courseMore.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);


        //앞에서 값을 받아옴
        Intent intent = getIntent();
        subject = intent.getStringExtra("subject_name");
        Toast.makeText(getApplicationContext(), getSubject, Toast.LENGTH_SHORT).show();

        getData();



        //리사이클러뷰 구현 부분
        RecyclerView recyclerView = findViewById(R.id.page2_fragment_recyclerview);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                //아이템들은 한 칸씩 차지
                 return 1;
            }
        });

        recyclerView.setLayoutManager( gridLayoutManager);
        recyclerView.setHasFixedSize(true);


        //url에 들어갈 contentTypeId, cat1, cat2 코드를 찾기
        url_code();

        //관광 api 연결
        SearchTask task = new SearchTask();
        try {
            String RESULT = task.execute().get();
            Log.i("전달 받은 값", RESULT);

            //사진링크, 타이틀(관광명), 분야뭔지 분리
            name_1 = RESULT.split("\n");

            for (int i = 0; i < name_1.length; i++) {
                name_2 = name_1[i].split("  ");

                //img_Url이 없는 경우도 있기 때문에, length = 3 = 있음/ 2 = 없음
                if (name_2.length == 3) {
                    contentid[i] = name_2[0];
                    img_Url[i] = name_2[1];
                    name[i] = name_2[2];
                } else {
                    contentid[i] = name_2[0];
                    img_Url[i] = null;
                    name[i] = name_2[1];
                }
            }


            //리사이클러에 들어갈 데이터를 넣는다
            for (int i = 0; i < name_1.length; i++) {
                cardview_items.add(new Recycler_item(img_Url[i], name[i], contentid[i] , subject));
            }


        } catch (InterruptedException ex) {
            ex.printStackTrace();
        } catch (ExecutionException ex) {
            ex.printStackTrace();
        }


        //리사이클러뷰 연결
        recyclerView.setAdapter(new Page2_CardView_adapter(cardview_items, mainActivity));

        courseMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Page2_MainActivity.this, Page2_1_MainActivity.class);
                intent.putExtra("subject_title", subject);
                startActivity(intent);
            }
        });



    }



    private void getData () {
        switch (subject) {
            case "자연":
                st1 = new String[]{"횡성", "여수", "제천"};
                st2 = new String[]{"강릉", "남원", "경주"};
                st3 = new String[]{"동해", "임실", "포항"};
                st4 = new String[]{"삼척", "군산", "영덕"};
                break;
            case "역사":
                st1 = new String[]{"광주", "전주", "나주", "경주", "제천"};
                st2 = new String[]{"김제", "남원", "보성", "안동", "충주"};
                st3 = new String[]{"익산", "곡성", "순천", "영주", "청주"};
                st4 = new String[]{"군산", "임실", "화순", "서울", "천안"};
                break;
            case "휴양":
                st1 = new String[]{"아산", "동해", "포항"};
                st2 = new String[]{"보령", "동두천", "부산"};
                st3 = new String[]{"정읍", "서울", "아산"};
                st4 = new String[]{"여수", "고양", "서울"};
                break;
            case "체험":
                st1 = new String[]{"익산", "곡성", "의정부"};
                st2 = new String[]{"보성", "순천", "양평"};
                st3 = new String[]{"광양", "광양", "강릉"};
                st4 = new String[]{"순천", "논산", "보성"};
                break;
            case "산업":
                st1 = new String[]{"서울", "제천"};
                st2 = new String[]{"인천", "남양주"};
                st3 = new String[]{"대전", "마석"};
                st4 = new String[]{"대구", "부산"};
                break;
            case "건축/조형":
                st1 = new String[]{"서울", "인천"};
                st2 = new String[]{"인천", "서울"};
                st3 = new String[]{"군산", "가평"};
                st4 = new String[]{"부산", "춘천"};
                break;
            case "문화":
                st1 = new String[]{"목포", "수원"};
                st2 = new String[]{"광주", "천안"};
                st3 = new String[]{"공주", "대전"};
                st4 = new String[]{"청주", "대구"};
                break;
            case "레포츠":
                st1 = new String[]{"남양주", "서울"};
                st2 = new String[]{"평창", "남양주"};
                st3 = new String[]{"횡성", "광명"};
                st4 = new String[]{"성남", "가평"};
                break;
            default:
                break;
        }

        if (st1.length != 0) {
            for (int i = 0; i < st1.length; i++) {
                items.add(new course(getSubject, st1[i], st2[i], st3[i], st4[i]));
            }
        }

        subject_title.setText("#" + subject);

        t1.setText(items.get(0).getSt1());
        t2.setText(items.get(0).getSt2());
        t3.setText(items.get(0).getSt3());
        t4.setText(items.get(0).getSt4());
        t5.setText(items.get(1).getSt1());
        t6.setText(items.get(1).getSt2());
        t7.setText(items.get(1).getSt3());
        t8.setText(items.get(1).getSt4());


        // adapter의 값이 변경(+추가) 되었다는 것을 알려줌 .
        //viewpager_adapter.notifyDataSetChanged();
    }


    //관광api 연결
    class SearchTask extends AsyncTask<String, Void, String>
    {
        @Override
        protected void onPreExecute() {
            //초기화 단계에서 사용
//            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {
            //Log.d("시작", "시작");

                url = "https://api.visitkorea.or.kr/openapi/service/rest/KorService/areaBasedList?serviceKey=" +
                        "7LT0Q7XeCAuzBmGUO7LmOnrkDGK2s7GZIJQdvdZ30lf7FmnTle%2BQoOqRKpjcohP14rouIrtag9KOoCZe%2BXuNxg%3D%3D" +
                        "&pageNo=1&numOfRows=5&MobileApp=AppTest&MobileOS=ETC&arrange=B" +
                        "&contentTypeId=" + contentTypeId +
                        "&sigunguCode=" +
                        "&areaCode="+
                        "&cat1=" + cat1 + "&cat2=" + cat2 + "&cat3=" +
                        "&listYN=Y";


            URL xmlUrl;
            returnResult = "";

            try {
                boolean title = false;
                boolean firstimage = false;
                boolean item = false;
                boolean contentid = false;

                xmlUrl = new URL(url);
                Log.d("url", url);
                xmlUrl.openConnection().getInputStream();
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                XmlPullParser parser = factory.newPullParser();
                parser.setInput(xmlUrl.openStream(), "utf-8");

                int eventType = parser.getEventType();
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    switch (eventType) {
                        case XmlPullParser.START_DOCUMENT:
                            break;

                        case XmlPullParser.START_TAG: {
                            if (parser.getName().equals("item")) {
                                item = true;
                            }
                            if (parser.getName().equals("contentid")) {
                                contentid = true;
                                // Log.d("태그 시작", "태그 시작2");
                            }
                            if (parser.getName().equals("firstimage")) {
                                firstimage = true;
                                // Log.d("태그 시작", "태그 시작3");
                            }
                            if (parser.getName().equals("title")) {
                                title = true;
                                //Log.d("태그 시작", "태그 시작4");
                            }
                            break;
                        }

                        case XmlPullParser.TEXT: {
                            if (contentid) {
                                returnResult += parser.getText() + "  ";
                                contentid = false;
                            }
                            if (firstimage) {
                                returnResult += parser.getText() + "  ";
                                firstimage = false;
                            }
                            if (title) {
                                returnResult += parser.getText() + "\n";
                                //Log.d("태그 받음", "태그받음4");
                                title = false;
                            }
                            break;
                        }
                        case XmlPullParser.END_TAG:
                            if (parser.getName().equals("item")) {
                                break;
                            }
                        case XmlPullParser.END_DOCUMENT:
                            break;
                    }
                    eventType = parser.next();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("err", "erro");
            }
            return returnResult;
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        }
    }







    //URL에 들어갈 contentTypeId, cat1, cat2를 지정하는 작업
    private void url_code() {
        switch (subject) {
            case "자연":
                contentTypeId = "12";
                cat1 = "A01";
                cat2 = "";
                break;
            case "역사":
                contentTypeId = "12";
                cat1 = "A02";
                cat2 = "A0201";
                break;
            case "휴양":
                contentTypeId = "12";
                cat1 = "A02";
                cat2 = "A0202";
                break;
            case "체험":
                contentTypeId = "12";
                cat1 = "A02";
                cat2 = "A0203";
                break;
            case "산업":
                contentTypeId = "12";
                cat1 = "A02";
                cat2 = "A0204";
                break;
            case "건축/조형":
                contentTypeId = "12";
                cat1 = "A02";
                cat2 = "A0205";
                break;
            case "문화":
                contentTypeId = "14";
                cat1 = "A02";
                cat2 = "A0206";
                break;
            case "레포츠":
                contentTypeId = "28";
                cat1 = "A03";
                cat2 = "";
                break;
            default:
                break;
        }
    }



}











