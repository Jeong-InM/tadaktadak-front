package ds.project.tadaktadakfront;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
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
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.korean.KoreanTextRecognizerOptions;

import java.io.File;


public class setImageActivity extends AppCompatActivity{

        TextRecognizer recognizer =
                TextRecognition.getClient(new KoreanTextRecognizerOptions.Builder().build());

        Handler handler = new Handler();
        public InputImage inputImage;
        String tempText; // 인식된 결과를 넣을 String
        String[] resultText;// 인식된 결과를 parsing후 분석할 String Array

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

                    //editText에서 수정된 값을 넘겨줌
                    eName = name.getText().toString();
                    //System.out.println("값: "+eName);
                    rCpName = cpName.getText().toString();
                    rEnName = enName.getText().toString();
                    rNumber = number.getText().toString();
                    rAddress = address.getText().toString();
                    rStart = start.getText().toString();
                    rSalary = salary.getText().toString();
                    rHours = hours.getText().toString();
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

//                                        usingNLPAPI Async2 = new usingNLPAPI();
//                                        Async2.execute();

                                    }
                                })
                                .addOnFailureListener(
                                        new OnFailureListener() {
                                            @Override
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
                if(resultText[i].contains("CS")){
                    eName = resultText[i];
                    System.out.println("값"+eName);
                    name.setText(resultText[i]);
                }else if(resultText[i].contains("김덕성")){
                    rCpName = resultText[i];
                    cpName.setText(resultText[i]);
                }else if(resultText[i].contains("민혜")){
                    rEnName = resultText[i];
                    enName.setText(resultText[i]);
                }else if(resultText[i].contains("연락처")){
                    rNumber = resultText[i];
                    number.setText(resultText[i]);
                }else if(resultText[i].contains("서울")){
                    rAddress = resultText[i];
                    address.setText(resultText[i]);
                }else if(resultText[i].contains("계약기간")){
                    rStart = resultText[i];
                    start.setText(resultText[i]);
                }else if(resultText[i].contains("임 금")){
                    rSalary = resultText[i];
                    salary.setText(resultText[i]);
                    hours.setText("8"); //일하는 시간
                }

                System.out.println("나눠진 값"+resultText[i]);
            }

        } // end splitResult

    }
