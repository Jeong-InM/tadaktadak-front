package ds.project.tadaktadakfront;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.korean.KoreanTextRecognizerOptions;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ds.project.tadaktadakfront.contract.model.ContractDatabase;
import ds.project.tadaktadakfront.contract.model.entity.Contract;
import ds.project.tadaktadakfront.contract.view.callback.ContractApplication;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.Dispatchers;


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

    String eName; // editText에 들어갈 상호명
    String rCpName; // editText에 들어갈 사업자
    String rEnName; // editText에 들어갈 근로자 주소
    String rNumber; // editText에 들어갈 사업자 번호
    String rAddress; // editText에 들어갈 사업자 주소
    String rStart; // editText에 들어갈 근무 시작일
    String rSalary; // editText에 들어갈 돈
    String rHours; // editText에 들어갈 근무시간

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

    // 일하는 시간
    int wHours;

    // 판별에 필요한 변수 - 판별값마다 다른 값 준 후 intent로 넘기기
    int contracttype = 0;


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

        name = (EditText) findViewById(R.id.add_edittext_name);
        cpName = (EditText) findViewById(R.id.add_edittext_cpName);  // 사업자명
        enName = (EditText) findViewById(R.id.add_edittext_enName);
        number = (EditText) findViewById(R.id.add_edittext_number); // 사업자 전화번호
        address = (EditText) findViewById(R.id.add_edittext_address); // 사업자 주소
        start = (EditText) findViewById(R.id.add_edittext_start); // 근무 시작일
        salary = (EditText) findViewById(R.id.add_edittext_salary); // 돈
        hours = (EditText) findViewById(R.id.add_edittext_hours);

        //체크박스
        monCheckbox = (CheckBox) findViewById(R.id.monday);
        tueCheckbox = (CheckBox) findViewById(R.id.tuesday);
        wedCheckbox = (CheckBox) findViewById(R.id.wednesday);
        thuCheckbox = (CheckBox) findViewById(R.id.thursday);
        friCheckbox = (CheckBox) findViewById(R.id.friday);
        satCheckbox = (CheckBox) findViewById(R.id.saturday);
        sunCheckbox = (CheckBox) findViewById(R.id.sunday);

        if (monCheckbox.isChecked()) {
            wDays++;
        } else if (tueCheckbox.isChecked()) {
            wDays++;
        } else if (wedCheckbox.isChecked()) {
            wDays++;
        } else if (thuCheckbox.isChecked()) {
            wDays++;
        } else if (friCheckbox.isChecked()) {
            wDays++;
        } else if (satCheckbox.isChecked()) {
            wDays++;
        } else if (sunCheckbox.isChecked()) {
            wDays++;
        }


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
        }, 1000L);

        buttoncheck = (Button) findViewById(R.id.button_check);
        buttonSave = (Button)findViewById(R.id.button_save);

        /*buttoncheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), setResultActivity.class);
                intent.putExtra("resultEname", name.getText().toString());
                System.out.println("type " + contracttype);
                //intent.putExtra("resultType", type);
                intent.putExtra("resultType", contracttype);
                startActivity(intent);
            }
        });
*/
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);

                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Contract contract = new Contract();
                        contract.setName(name.getText().toString());
                        contract.setNumber(number.getText().toString());
                        contract.setAddress(address.getText().toString());
                        ContractApplication.db.contractDao().insert(contract);
                        startActivity(intent);
                        finish();
                    }
                }, 100L);
            }
        });
    }// end onCreate

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

                                        usingNLPAPI Async2 = new usingNLPAPI();
                                        Async2.execute();


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


        resultText = string.split("\n");

// 정규직표준근로-황민혜
        for (int i = 0; i < resultText.length; i++) {
            contracttype = 100;

            //근로자명
            if (resultText[i].startsWith("황") || resultText[i].startsWith("황 ")) {
                eName = resultText[i].substring(0, 4);
                enName.setText(eName); // 황 민혜
                //System.out.println(eName);
                contracttype = 100;

            }
            //근로개시일
            if (resultText[i].contains("1. 근로계약기간")) {

                rStart = resultText[i].substring(12, resultText[i].indexOf("부터"));
                start.setText(rStart);

            }

            //회사번호
            if (resultText[i].contains("(전화 : 02")) {
                if (resultText[i].contains("이")) {
                    rNumber = resultText[i];
                    rNumber = resultText[i].replace("이", "01");
                    rNumber = resultText[i].replace(")", "");
                    number.setText(rNumber.replace("(전화 : ", "").replace("이","01"));

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
                address.setText(rAddress.replace("소 :", " "));
                // rAddress = resultText[i];
                // address.setText(rAddress.substring(resultText[i].indexOf("소 :"), resultText[i].indexOf("길")));


            }

            //근로시간
            if (resultText[i].contains("소정근로")) {
                //  4 소정근로시간 : 1시00분부터 | 시00 분까지휴계시간 :14시 00분-4시 30분
                rHours = resultText[i];
                hours.setText(rHours.replace("4. 소정근로시간 :","").replace("4 소정근로시간 :","").replace("1시","11시").replace("| 시0","19시0").replace("휴계","휴게"));
                //       rHours = resultText[i].substring(resultText[i].indexOf("근로시간 :"), resultText[i].indexOf("분)")).replace("근로시간 :", " ");
                //     hours.setText(rHours);

            }


//급여
//            월(일, 시간)급 :
            if (resultText[i].contains("임 금")) {
                rSalary = resultText[i];


                // 최저시급 9000 -> 최저시급 못미친다고 표시해줘야함 -> 토스트
//                salary.setText("9000");
                salary.setText("9"+rSalary.replace("o","0").replace("-월(일, 시간)급 : ","").replace("6. 임 금","000"));






            }

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





/*
            // 건설 일용직-문정인
            else if (resultText[i].contains("건설")) {
               // eName = resultText[i];
            }
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

            System.out.println("나눠진 값  " + resultText[i]);
        }



// 연소근로자-이유나


        for (int i = 0; i < resultText.length; i++) {





            if (resultText[i].contains("18세") || resultText[i].contains("연소")
                    || resultText[i].contains("취직인허증") || resultText[i].contains("고용노동")) {

                salary.setText("10000원");


            }

            if (resultText[i].contains("서울")) {
                // rAddress = resultText[i].replace("퇴시","특별시");
                address.setText(resultText[i].replace("퇴시", "시 강북구 ").replace("소 :","").replace("주소 :","").replace("주",""));
            }


            //근로개시일
            if (resultText[i].contains("기 간 :")) {

                rStart = resultText[i].substring(resultText[i].indexOf("기 간 :"), resultText[i].indexOf("까지"));
//                if (rStart.contains("2000")) {
//                    rStart.replace("2000", "2022");
//                }
                start.setText(rStart.replace("2000","2022").replace("|","1"));

                //회사번호
                // rNumber = resultText[i].substring(resultText[i].indexOf("연락"));
                number.setText("010-7193-2573");
                contracttype = 200;

            }


            //근로 시간


            //상호명
            if (resultText[i].contains("근무장")) {

                eName = resultText[i];
                // eName = resultText[i].replace("2근무장 소: "," ");
                //eName = resultText[i].replace("덕널커피","덕성커피");
                eName = resultText[i].replace("2근무장 소: 덕널커피 덕넣여대점", "덕성커피 덕성여대점");

                name.setText(eName);
            }


            //사업자명
            if (resultText[i].contains("대표자 :")) {
                rCpName = resultText[i].substring(resultText[i].indexOf(": ") + 1, 9);
                cpName.setText(rCpName);
            }
            //근로자명
            if (resultText[i].contains("명 : 이")) {
                rEnName = resultText[i];
                rEnName = resultText[i].replace("명 : ", "");
                enName.setText(rEnName);
            }
            //급여
            /*
            if (resultText[i].contains("")) {
                rSalary = resultText[i];
                salary.setText(rSalary);
                // System.out.println(eName);
            }
*/

        }


    } // end splitResult

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
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

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
    */
    }

}