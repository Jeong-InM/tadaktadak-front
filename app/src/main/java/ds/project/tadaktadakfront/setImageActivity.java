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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

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


public class setImageActivity extends AppCompatActivity{

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
        String rNumber ; // editText에 들어갈 사업자 번호
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

        // 근로요일을 저장할 변수
        int wDays;

        // 일하는 시간
        int wHours;

        Intent intent = new Intent();

    public class MainList{
        public String mName;
        public String mCategory;
        public MainList(String name, String category){
            this.mName = name;
            this.mCategory = category;
        }
        public String getmName() { return mName; }
        public String getmCategory() { return mCategory; }
    }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_set_image);

            name = (EditText)findViewById(R.id.add_edittext_name);
            cpName = (EditText)findViewById(R.id.add_edittext_cpName);  // 사업자명
            enName = (EditText)findViewById(R.id.add_edittext_enName);
            number = (EditText)findViewById(R.id.add_edittext_number); // 사업자 전화번호
            address = (EditText)findViewById(R.id.add_edittext_address); // 사업자 주소
            start = (EditText)findViewById(R.id.add_edittext_start); // 근무 시작일
            salary = (EditText)findViewById(R.id.add_edittext_salary); // 돈
            hours = (EditText)findViewById(R.id.add_edittext_hours);

            //체크박스
            monCheckbox = (CheckBox)findViewById(R.id.monday);
            tueCheckbox =  (CheckBox)findViewById(R.id.tuesday);
            wedCheckbox =  (CheckBox)findViewById(R.id.wednesday);
            thuCheckbox =  (CheckBox)findViewById(R.id.thursday);
            friCheckbox =  (CheckBox)findViewById(R.id.friday);
            satCheckbox =  (CheckBox)findViewById(R.id.saturday);
            sunCheckbox =  (CheckBox)findViewById(R.id.sunday);

            if(monCheckbox.isChecked()){
                wDays++;
            }
            else if (tueCheckbox.isChecked()){
                wDays++;
            }
            else if (wedCheckbox.isChecked()){
                wDays++;
            }
            else if (thuCheckbox.isChecked()){
                wDays++;
            }
            else if (friCheckbox.isChecked()){
                wDays++;
            }
            else if (satCheckbox.isChecked()){
                wDays++;
            }
            else if (sunCheckbox.isChecked()){
                wDays++;
            }


            String currentPhotoPath = this.getIntent().getStringExtra("path");
            final Uri uriSelected = Uri.parse(this.getIntent().getStringExtra("path"));
            File file = new File(currentPhotoPath);
            Uri uri = Uri.fromFile(file);

            Log.v("tag", "successI");

            ImageView imageView = (ImageView)findViewById(R.id.set_iv);
            Glide.with(this)
                    .load(currentPhotoPath)
                    .error(new ColorDrawable(Color.RED))
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .into(imageView);

            // 2초후 다음 액티비티로 넘김
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    setImageActivity.this.convertImagetoText(uriSelected);
                    setImageActivity.this.convertImagetoText(uri);
                    Log.v("tag", "successT");
                }
            },1000L);

            buttonSave = (Button)findViewById(R.id.button_save);
            buttonSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent result = new Intent();

                    //editText에서 수정된 값을 넘겨줌
                    eName = name.getText().toString();
                    //System.out.println("값: "+eName);
                   result.putExtra("eName", eName);
                    rCpName = cpName.getText().toString();
                    result.putExtra("rCpName", rCpName);
                    rEnName = enName.getText().toString();
                    result.putExtra("rEnName", rEnName);
                    rNumber = number.getText().toString();
                    result.putExtra("rNumber", rNumber);
                    rAddress = address.getText().toString();
                    result.putExtra("rAddress", rAddress);
                    rStart = start.getText().toString();
                    result.putExtra("rStart", rStart);
                    rSalary = salary.getText().toString();
                    result.putExtra("rSalary", rSalary);
                    rHours = hours.getText().toString();
                    result.putExtra("rHours", rHours);
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

        public void splitResult(String string){
            resultText = string.split("\n");
            for (int i = 0; i< resultText.length; i++) {
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

                System.out.println("나눠진 값"+resultText[i]);
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
                    System.out.println("에러검사: "+check);
                    if(!check.equals("[]"))
                    {
                        String type = jsonElement.getAsJsonObject().get("return_object").getAsJsonObject().get("sentence").getAsJsonArray().get(0).getAsJsonObject().get("NE").getAsJsonArray().get(0).getAsJsonObject().get("type").toString();

                        if(type.equals("\"LCP_CAPITALCITY\"") || type.equals("\"LCP_PROVINCE\"") && type.equals("\"LCP_COUNTY\"") && type.equals("\"LCP_CITY\""))

                        {

                            rAddress = text;
                            System.out.println("주소찾음: "+ rAddress); //주소 제대로 나오는지 테스트

                        }
                        else if (type.equals("\"PS_NAME\"") && text.equals("대표자")){ // 이름 찾아서 넘겨주기
                            String rcpName = jsonElement.getAsJsonObject().get("return_object").getAsJsonObject().get("sentence").getAsJsonArray().get(0).getAsJsonObject().get("NE").getAsJsonArray().get(0).getAsJsonObject().get("text").toString();

                            System.out.println("이름임:"+text);
                            rCpName = rcpName;

                        }
                        else if (type.equals("\"PS_NAME\"") && text.equals("근로자")){
                            String worker = jsonElement.getAsJsonObject().get("return_object").getAsJsonObject().get("sentence").getAsJsonArray().get(0).getAsJsonObject().get("NE").getAsJsonArray().get(0).getAsJsonObject().get("text").toString();
                            rEnName = worker;
                            System.out.println(worker);

                        }
                        else if (type.equals("\"DT_MONTH\"") || type.equals("\"DT_YEAR\"") && text.equals("기간")){
                            rStart = text;
                        }
                        else if(type.equals("\"OG\"") || text.equals("(주)")){
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

    } // end using NLPAPI

    }
