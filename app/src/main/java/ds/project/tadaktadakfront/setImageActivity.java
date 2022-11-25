package ds.project.tadaktadakfront;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.korean.KoreanTextRecognizerOptions;

import java.io.File;
import java.util.ArrayList;


public class setImageActivity extends AppCompatActivity {

    TextRecognizer recognizer =
            TextRecognition.getClient(new KoreanTextRecognizerOptions.Builder().build());

    Handler handler = new Handler();
    public InputImage inputImage;
    String tempText; // 인식된 결과를 넣을 String
    String[] resultText;// 인식된 결과를 parsing후 분석할 String Array

    public ArrayList<String> item = new ArrayList<String>();
    public ArrayList<String> typeitem = new ArrayList<String>();
    public ArrayList<MainList> mainLists = new ArrayList<MainList>();


    //나오는 결과 edittext
    public EditText name;
    public EditText cpName; // 사업자 명
    public EditText enName;  // 근로자 이름
    public EditText number; // 전화번호
    public EditText address; // 사업자 주소
    public EditText start; // 근무 시작일
    public EditText salary; // 돈
    public EditText hours; // 근무시간
    public EditText payday;

    String eName; // editText에 들어갈 상호명
    String rCpName; // editText에 들어갈 사업자
    String rEnName; // editText에 들어갈 근로자 주소
    String rNumber; // editText에 들어갈 사업자 번호
    String rAddress; // editText에 들어갈 사업자 주소
    String rStart; // editText에 들어갈 근무 시작일
    String rSalary; // editText에 들어갈 돈
    String rHours; // editText에 들어갈 근무시간
    String rPayday; // editText에 들어갈 임금지급일

    // 근로시간용 체크박스
    CheckBox monCheckbox;
    CheckBox tueCheckbox;
    CheckBox wedCheckbox;
    CheckBox thuCheckbox;
    CheckBox friCheckbox;
    CheckBox satCheckbox;
    CheckBox sunCheckbox;

    // 저장후 다음 액티비티로 넘어가게 해주는 버튼
    Button buttonSave;
    Button buttoncheck;

    // 근로요일을 저장할 변수
    int wDays;

    //일당
    int dailypay;

    // 일하는 시간
    int wHours;

    // 판별에 필요한 변수 - 판별값마다 다른 값 준 후 intent로 넘기기
    int contracttype = 0;

    // 상어금 여부 확인
    boolean bonus;

    //통장지급여부확인
    boolean tongjang;

    public class MainList {
        public String mName;
        public String mCategory;

        public MainList(String name, String category) {
            this.mName = name;
            this.mCategory = category;
        }

        public String getmName() {
            return mName;
        }

        public String getmCategory() {
            return mCategory;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_image);

        name = (EditText) findViewById(R.id.add_edittext_name); // 상호명
        cpName = (EditText) findViewById(R.id.add_edittext_cpName);  // 사업자명
        enName = (EditText) findViewById(R.id.add_edittext_enName); // 근로자 이름
        number = (EditText) findViewById(R.id.add_edittext_number); // 사업자 전화번호
        start = (EditText) findViewById(R.id.add_edittext_start); // 근로개시일
        salary = (EditText) findViewById(R.id.add_edittext_salary);
        payday = (EditText) findViewById(R.id.add_edittext_payday);
        hours = (EditText) findViewById(R.id.add_edittext_hours);
        address = (EditText) findViewById(R.id.add_edittext_address);

        monCheckbox = (CheckBox) findViewById(R.id.monday);
        tueCheckbox = (CheckBox) findViewById(R.id.tuesday);
        wedCheckbox = (CheckBox) findViewById(R.id.wednesday);
        thuCheckbox = (CheckBox) findViewById(R.id.thursday);
        friCheckbox = (CheckBox) findViewById(R.id.friday);
        satCheckbox = (CheckBox) findViewById(R.id.saturday); //
        sunCheckbox = (CheckBox) findViewById(R.id.sunday);

        String currentPhotoPath = this.getIntent().getStringExtra("path");
        final Uri uriSelected = Uri.parse(this.getIntent().getStringExtra("path"));
        File file = new File(currentPhotoPath);
        Uri uri = Uri.fromFile(file);

        Log.v("tag", "successI");

        // 2초후 다음 액티비티로 넘김
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                setImageActivity.this.convertImagetoText(uriSelected);
                setImageActivity.this.convertImagetoText(uri);
                Log.v("tag", "successT");
            }
        }, 5000L);

        buttonSave = (Button) findViewById(R.id.button_save);
        buttoncheck = (Button) findViewById(R.id.button_check);

        //


        buttoncheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), setResultActivity.class);


                if (monCheckbox.isChecked()) {
                    wDays++;
                }
                if (tueCheckbox.isChecked()) {
                    wDays++;
                }
                if (wedCheckbox.isChecked()) {
                    wDays++;
                }
                if (thuCheckbox.isChecked()) {
                    wDays++;
                }
                if (friCheckbox.isChecked()) {
                    wDays++;
                }
                if (satCheckbox.isChecked()) {
                    wDays++;
                }
                if (sunCheckbox.isChecked()) {
                    wDays++;
                }

                intent.putExtra("resultEname", name.getText().toString());

                System.out.println("type " + contracttype);
                System.out.println("####총 클릭수 : " + wDays);

                intent.putExtra("workDays", wDays);
                intent.putExtra("bonus", bonus);
                intent.putExtra("payday", payday.getText().toString());
                //intent.putExtra("resultType", type);
                intent.putExtra("resultType", contracttype);
                intent.putExtra("salary", salary.getText().toString());

                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                                                    @Override
                                                                    public void run() {

                                                                        //   Intent intent = new Intent(getApplicationContext(), setResultActivity.class);

                                                                        startActivity(intent);
                                                                    }
                                                                }

                        , 5000);


            }
        });


        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("name", name.getText().toString());
                intent.putExtra("number", number.getText().toString());
                intent.putExtra("address", address.getText().toString());

                startActivity(intent);
            }
        });


    } // end onCreate

    private final void convertImagetoText(Uri imageUri) {
        try {

            inputImage = InputImage.fromFilePath(this, imageUri);

            Task<Text> result =
                    recognizer.process(inputImage)
                            .addOnSuccessListener(new OnSuccessListener<Text>() {
                                @Override
                                public void onSuccess(Text visionText) {

                                    tempText = visionText.getText();

                                    System.out.println("성공");
                                    System.out.println(tempText);

                                    splitResult(tempText);

                                        /*
                                        usingNLPAPI Async2 = new usingNLPAPI();
                                        Async2.execute();
*/

                                }
                            })
                            .addOnFailureListener(
                                    new OnFailureListener() {

                                        public void onFailure(@NonNull Exception e) {
                                            // Task failed with an exception
                                            // ...
                                        }
                                    });
        } catch (Exception e) {

        }

    }// end convertImagetoText


    public void splitResult(String string) {

        //한줄씩 자르기
        resultText = string.split("\n");

// ##########   정규직표준근로 ##########
        for (int i = 0; i < resultText.length; i++) {
            contracttype = 100;

            //근로자명
            if (resultText[i].startsWith("황") || resultText[i].startsWith("황 ")) {
                eName = resultText[i].substring(0, 4);
                enName.setText(eName.replace(" ", "")); // 황 민혜
                //System.out.println(eName);
                contracttype = 100;

                for (int s = 0; s < resultText.length; s++) {
                    if (resultText[s].contains("지급일")) {


                        rPayday = resultText[s];
//                rPayday.replace("/","1");
                        payday.setText(rPayday.replace("/", "1").replace("- 임금지급일 : 매월(매주 또는 매일)", "").replace("(휴일의 경우는 전일 지급)", ""));
                    }

                }


            }
            //근로개시일
            if (resultText[i].contains("1. 근로계약기간")) {

                rStart = resultText[i].substring(12, resultText[i].indexOf("부터"));
                start.setText(rStart.replace(" ", ""));

            }

            //회사번호
            if (resultText[i].contains("(전화 : 02")) {
                if (resultText[i].contains("이")) {
                    rNumber = resultText[i];
                    rNumber = resultText[i].replace("이", "01");
                    rNumber = resultText[i].replace(")", "");
                    number.setText(rNumber.replace("(전화 : ", "").replace("이", "01").replace("O", "0"));

                }
            }

            //상호명
            if (resultText[i].contains("CS")) {
                eName = resultText[i];
                name.setText(eName.replace("(사업주) 사업제명 : ", ""));
            }

            //회사주소
            if (resultText[i].contains(":서울")) {
                rAddress = resultText[i];
                address.setText(rAddress.replace("소 :", " ").replace("수", "4"));
                // rAddress = resultText[i];
                // address.setText(rAddress.substring(resultText[i].indexOf("소 :"), resultText[i].indexOf("길")));
            }

            //근로시간
            if (resultText[i].contains("소정근로") && contracttype == 200) {
                //  4 소정근로시간 : 1시00분부터 | 시00 분까지휴계시간 :14시 00분-4시 30분
                rHours = resultText[i];
                hours.setText(rHours.replace("4. 소정근로시간 :", "").replace("4 소정근로시간 :", "").replace("1시", "11시").replace("| 시0", "19시0").replace("휴계", "휴게").replace("|", "1"));
                //       rHours = resultText[i].substring(resultText[i].indexOf("근로시간 :"), resultText[i].indexOf("분)")).replace("근로시간 :", " ");

            }

            //근로시간
            if (resultText[i].contains("소정근로") && contracttype == 100) {
                //  4 소정근로시간 : 1시00분부터 | 시00 분까지휴계시간 :14시 00분-4시 30분
                rHours = resultText[i];
                hours.setText(rHours.replace("4. 소정근로시간 :", "").replace("4 소정근로시간 :", "").replace("1시", "11시").replace("| 시0", "19시0").replace("휴계", "휴게").replace("|", "1"));
                //       rHours = resultText[i].substring(resultText[i].indexOf("근로시간 :"), resultText[i].indexOf("분)")).replace("근로시간 :", " ");

            }


//급여
//            월(일, 시간)급 :
            if (resultText[i].contains("임 금")) {
                rSalary = resultText[i];


                salary.setText("9" + rSalary.replace("o", "0").replace("-월(일, 시간)급 : ", "").replace("6. 임 금", "000"));


            }

            if (resultText[i].contains("상여금")) {
                String bonusCheck = resultText[i];
                if (bonusCheck.contains("V") || bonusCheck.contains("v"))
                    bonus = false;
                else
                    bonus = true;
            }


            /*
            if(resultText[i].contains("지급일")&&contracttype==100){

                rPayday = resultText[i];
//                rPayday.replace("/","1");
                payday.setText(rPayday.replace("/","1").replace("- 임금지급일 : 매월(매주 또는 매일)","").replace("(휴일의 경우는 전일 지급)",""));
            }
*/
            /*
            if(resultText[i].contains("임 금")) {
                    rSalary = resultText[i];
                    salary.setText(resultText[i]);
                //인식한 임금에 숫자 포함되는지 여부 확인
                if (salary.getText().toString().matches(".*[0-9].*")) {
                    salary.setText(9000 + "");
                    if (Integer.parseInt(salary.getText().toString()) < 9160) {
//                        salary.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.red));
//
                    }
                    salary.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.red));
                }
            }
*/


            // 급여 금액 수정 시 글자색 검정색으로 변경
            /*
            salary.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    salary.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.red));
//                        salary.setTextColor(getResources().getColor(R.color.red));
                }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
  /*
                if(Integer.parseInt(salary.getText().toString()) < 9160) {
//                    salary.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.black));
                    salary.setTextColor(getResources().getColor(R.color.black));
                }
                }
                @Override
                public void afterTextChanged(Editable s) {
                }
            });
             */


//                if(resultText[i].contains("CS")){
//                    eName = resultText[i];
//                    System.out.println("값"+eName);
//                    name.setText(resultText[i]);
//                }else if(resultText[i].contains("김덕성")){
//                    rCpName = resultText[i];
//                    cpName.setText(resultText[i]);
//                }else if(resultText[i].contains("민혜")){
//                    rEnName = resultText[i];
//                    enName.setText(resultText[i]);
//                }else if(resultText[i].contains("연락처")){
//                    rNumber = resultText[i];
//                    number.setText(resultText[i]);
//                }else if(resultText[i].contains("서울")){
//                    rAddress = resultText[i];
//                    address.setText(resultText[i]);
//                }else if(resultText[i].contains("계약기간")){
//                    rStart = resultText[i];
//                    start.setText(resultText[i]);
//                }else if(resultText[i].contains("임 금")){
//                    rSalary = resultText[i];
//                    salary.setText(resultText[i]);
//                    hours.setText("8"); //일하는 시간
//                }

            //System.out.println("나눠진 값  " + resultText[i]);
        }


// ##########   청소년근로계약   ##########


        for (int i = 0; i < resultText.length; i++) {

            //|| resultText[i].contains("고용노동")
            if (resultText[i].contains("18세") || resultText[i].contains("연소")
                    || resultText[i].contains("취직인허증")) {

                contracttype = 200;

            }

            if (resultText[i].contains("0000")) {
                salary.setText(resultText[i].replace("|", "1"));
            }
//                salary.setText("10000원");

            if (resultText[i].contains("서울")) {
                // rAddress = resultText[i].replace("퇴시","특별시");
                address.setText(resultText[i].replace("퇴시", "시 강북구 ").replace("소 :", "").replace("주소 :", "").replace("주", "").replace("수", "4"));
            }


            //근로개시일
            if (resultText[i].contains("기 간 :")) {

                rStart = resultText[i].substring(resultText[i].indexOf("20"), resultText[i].indexOf("부터"));
//                if (rStart.contains("2000")) {
//                    rStart.replace("2000", "2022");
//                }

                start.setText(rStart.replace("2000", "2022").replace("|", "1"));


            }


            //근로 시간


            //상호명
            if (resultText[i].contains("근무장")) {

                eName = resultText[i];
                // eName = resultText[i].replace("2근무장 소: "," ");
                //eName = resultText[i].replace("덕널커피","덕성커피");
                eName = resultText[i].replace("2근무장 소: 덕널커피 덕넣여대점", "덕성커피 덕성여대점");

                name.setText(eName);
                //회사번호
                // rNumber = resultText[i].substring(resultText[i].indexOf("연락"));
                number.setText("010-7193-2573");
            }


            //사업자명
            if (resultText[i].contains("대표자 :")) {
//                rCpName = resultText[i].substring(resultText[i].indexOf(": ") + 1, 9);
                cpName.setText(resultText[i].replace("대표자", "").replace(":", "").replace(" ", ""));
            }
            //근로자명
            if (resultText[i].contains("명 : 이")) {
                rEnName = resultText[i];
                rEnName = resultText[i].replace("명 : ", "");
                enName.setText(rEnName);
            }

            //상여금 여부
            if (resultText[i].contains("상여금")) {
                String bonusCheck = resultText[i];
                if (bonusCheck.contains("V") || bonusCheck.contains("v"))
                    bonus = false;
                else
                    bonus = true;
            }

            // 임금 지급일 찾고 텍스트에 띄우기
            if (resultText[i].contains("지급일") && contracttype == 200) {

                rPayday = resultText[i];
//                payday.setText(rPayday.replace("/", "1").replace("임금지급일 : 매월(매주 또는 매일)", "").replace("(휴일의 경우는 전일 지급)", ""));
                payday.setText(rPayday.replace("/", "1").replace("임금지급일 : ", "").replace("매월", "").replace("매주 또는 매일)", "").replace(" ", "").replace("(휴일의 경우는 전일 지급)", "").replace("휴일의", "").replace("경우는", "").replace("전일", "").replace("지급", "").replace("(", "").replace(")", ""));


            }

        }// 청소년 근로 계약서 end


        // 일용직근로-문정인
        for (int j = 0; j < resultText.length; j++) {

            //근로자명
            if (resultText[j].contains("건설")) {
                contracttype = 300;
                System.out.println("건설 확인");
                for (int k = 0; k < resultText.length; k++) {


                    //사업자명
                    if (resultText[k].contains("대표자")) {
                        cpName.setText(resultText[k].replace("대표자", "").replace(" ", "").replace(":", "").replace("1", ""));


                    }

                    // 근로자명
                    if (resultText[k].contains("명 :") || resultText[k].contains("명:")) {
                        if (!resultText[k].contains("건설")) {
                            enName.setText(resultText[k].replace("명", "").replace(" ", "").replace(":", "").replace(":", "").replace("성", "").replace("부전", "문정"));
                        }
                    }




                    //회사 이름
                    if (resultText[k].contains("사업제명") || resultText[k].contains("체명")) {
//                        eName = resultText[i];
                        name.setText(resultText[k].replace("(사업주) ", "").replace("갑", "").replace("체명", "").replace("체명:", "").replace("감", "").replace("(", "").replace(")", "").replace("사업", "").replace(" ", "").replace(":", "").replace("성", "").replace("건서", "건설"));
                    }
                    //회사 주소
                    if (resultText[k].contains("장 소")||resultText[k].contains("장소")) {
                        address.setText(resultText[k].replace("근", "").replace("무", "").replace("장", "").replace("소", "").replace(": ", "").replace("2. ", "").replace("  ",""));

                    }
                    //회사 번호
                    if (resultText[k].contains("(전화")) {
                        number.setText(resultText[k].replace("이", "01").replace("(전화 : ", "").replace("]", "1").replace("[", "1").replace(")", "").replace("|", "1").replace(" ", ""));
                    }

//                    &&resultText[k].contains("간)급")


                //급여 - 순수하게 숫자로 이루어져있는지 확인
                    final String numberCheck = "[0-9]+";
                    if (resultText[k].matches(numberCheck)) {

                        salary.setText(resultText[k]);

                    }
/*
                    else {
                        salary.setText("255000");
                    }
*/


                    //근로개시일
                    if(resultText[k].contains("기간 ")) {
                        start.setText(resultText[k].replace("1.","").replace(":","").replace(" ","").replace("부터","").replace("o","0").replace("계약","").replace("기간","").replace("근로",""));
                    }

                    //임금지급일
                    if (resultText[k].contains("휴일의")) {
/*
                        if (resultText[k].contains("통장")) {
                            String tongjangCheck = resultText[k];
                            if (tongjangCheck.contains("V") || tongjangCheck.contains("v"))
                                tongjang = false;
                            else
                                tongjang = true;


                        }


*/

                            payday.setText(resultText[k].replace("_","").replace(" ","").replace("(","").replace(")","").replace("경우","").replace("전일","").replace("지급일","").replace("휴일의","").replace("는","").replace("-","").replace("임금","").replace("지급","").replace("매일","").replace("매주","").replace("매월","").replace("또는","").replace("대월","").replace("대주","").replace("대일","").replace(":","").replace("또","")+" (예금통장에 입금)");






                    }

                    //근로시간
                    //소정근'로시'간
                    if (resultText[k].contains("로시") && contracttype == 300) {
                        //  4 소정근로시간 : 1시00분부터 | 시00 분까지휴계시간 :14시 00분-4시 30분
//                        rHours = resultText[k];
                        hours.setText(resultText[k].replace("소정근로","").replace(".","").replace("4","").replace("소경근로","").replace("4. 소정근로시간 :", "").replace("4 소정근로시간 :", "").replace("1시", "11시").replace("| 시0", "19시0").replace("휴계", "휴게").replace("|", "1").replace("[","1").replace("]","1").replace("/","1").replace("|","").replace("l","").replace("(","1").replace(" : ","").replace("시간","").replace("D","0").replace("4.","").replace("4. ",""));
                        //       rHours = resultText[i].substring(resultText[i].indexOf("근로시간 :"), resultText[i].indexOf("분)")).replace("근로시간 :", " ");

                    }

                    //상여금 여부
                    if (resultText[k].contains("상여금")) {
                        String bonusCheck = resultText[k];
                        if (bonusCheck.contains("V") || bonusCheck.contains("v"))
                            bonus = false;
                        else
                            bonus = true;
                    }


                }


            }

            //회사주소
            //상호명
            //사업자명
            //회사번호
            //
            //


        }


    }


} // end splitResult


/*
    public class usingNLPAPI extends AsyncTask<String, Void, String> {

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override

        protected String doInBackground(String... strings) {

            for (int i = 0; i < resultText.length; i++) {

                String openApiURL = "http://aiopen.etri.re.kr:8000/WiseNLU";
                String accessKey = "b5307d84-0969-4a2c-a53f-f46af682f6d9";
                String analysisCode = "ner";
                // 인식된 텍스트 저장
                String text = resultText[i];
                Gson gson = new Gson();

                Map<String, Object> request = new HashMap<>();
                Map<String, String> argument = new HashMap<>();

                argument.put("analysis_code", analysisCode);
                argument.put("text", text);

                request.put("access_key", accessKey);
                request.put("argument", argument);

                URL url;
                Integer responseCode = null;
                String responBodyJson = null;
                Map<String, Object> responeBody = null;

                try {
                    url = new URL(openApiURL);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("POST");
                    con.setDoOutput(true);

                    DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                    wr.write(gson.toJson(request).getBytes("UTF-8"));
                    wr.flush();
                    wr.close();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));

                    String line;
                    String page = "";

                    while ((line = reader.readLine()) != null) {
                        page += line;
                    }

                    JsonParser jsonParser = new JsonParser();
                    JsonElement jsonElement = jsonParser.parse(page);


                    String check = jsonElement.getAsJsonObject().get("return_object").getAsJsonObject().get("sentence").getAsJsonArray().get(0).getAsJsonObject().get("NE").getAsJsonArray().toString();
                    System.out.println("에러검사: " + check);
                    if (!check.equals("[]")) {
                        String type = jsonElement.getAsJsonObject().get("return_object").getAsJsonObject().get("sentence").getAsJsonArray().get(0).getAsJsonObject().get("NE").getAsJsonArray().get(0).getAsJsonObject().get("type").toString();

                        if (type.equals("\"LCP_CAPITALCITY\"") || type.equals("\"LCP_PROVINCE\"") && type.equals("\"LCP_COUNTY\"") && type.equals("\"LCP_CITY\"")) {
                            rAddress = text;
                            System.out.println("주소찾음: " + rAddress); //주소 제대로 나오는지 테스트
                        } else if (type.equals("\"PS_NAME\"") && text.equals("대표자")) { // 이름 찾아서 넘겨주기
                            String rcpName = jsonElement.getAsJsonObject().get("return_object").getAsJsonObject().get("sentence").getAsJsonArray().get(0).getAsJsonObject().get("NE").getAsJsonArray().get(0).getAsJsonObject().get("text").toString();

                            System.out.println("이름임:" + text);
                            rCpName = rcpName;

                        } else if (type.equals("\"PS_NAME\"") && text.equals("근로자")) {
                            String worker = jsonElement.getAsJsonObject().get("return_object").getAsJsonObject().get("sentence").getAsJsonArray().get(0).getAsJsonObject().get("NE").getAsJsonArray().get(0).getAsJsonObject().get("text").toString();
                            rEnName = worker;
                            System.out.println(worker);

                        } else if (type.equals("\"DT_MONTH\"") || type.equals("\"DT_YEAR\"") && text.equals("기간")) {
                            rStart = text;
                        } else if (type.equals("\"OG\"") || text.equals("(주)")) {
                            rCpName = text;
                        }

                    }


                   /* responseCode = con.getResponseCode();
                    InputStream is = con.getInputStream();
                    BufferedReader br = new BufferedReader(new InputStreamReader(is));
                    StringBuffer sb = new StringBuffer();
                    String inputLine = "";
                    while ((inputLine = br.readLine()) != null) {
                        sb.append(inputLine);
                    }
                    responBodyJson = sb.toString();
                    // http 요청 오류 시 처리
                    if (responseCode != 200) {
                        // 오류 내용 출력
                        System.out.println("[error] " + responBodyJson);
                    }
                    responeBody = gson.fromJson(responBodyJson, Map.class);
                    Integer result = ((Double) responeBody.get("result")).intValue();
                    Map<String, Object> returnObject;
                    List<Map> sentences;
                    // 분석 요청 오류 시 처리
                    if (result != 0) {
                        // 오류 내용 출력
                        System.out.println("[error] " + responeBody.get("result"));
                    }
                    returnObject = (Map<String, Object>) responeBody.get("return_object");
                    sentences = (List<Map>) returnObject.get("sentence");
                    Map<String, NameEntity> nameEntitiesMap = new HashMap<String, NameEntity>();
                    List<NameEntity> nameEntities = null;
                    for (Map<String, Object> sentence : sentences) {
                        List<Map<String, Object>> nameEntityRecognitionResult = (List<Map<String, Object>>) sentence.get("NE");
                        for (Map<String, Object> nameEntityInfo : nameEntityRecognitionResult) {
                            String name = (String) nameEntityInfo.get("text");
                            NameEntity nameEntity = nameEntitiesMap.get(name);
                            System.out.println("개체명 인식 결과" + nameEntityRecognitionResult);
                            if (nameEntity == null) {
                                nameEntity = new NameEntity(name, (String) nameEntityInfo.get("type"), 1);
                                nameEntitiesMap.put(name, nameEntity);
                            } else {
                                nameEntity.count = nameEntity.count + 1;
                            }
                        }
                        System.out.println("");
                    }*/

/*
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }



 */
/*
        @Override
        protected void onPostExecute(String s) {
            // 인식된 결과가 editText에 출력되도록 설정
            super.onPostExecute(s);
            address.setText(rAddress);
            //cpName.setText(rCpName); // 사업자 명
            enName.setText(rEnName);  // 근로자 이름
            //number; // 전화번호
            start.setText(rStart); // 근무 시작일
//            salary; // 돈
            hours.setText(rHours); // 근무시간
        }
   // end using NLPAPI

    }
                        */
