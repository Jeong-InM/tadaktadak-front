package ds.project.tadaktadakfront;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import ds.project.tadaktadakfront.chatbot.ChatbotActivity;

public class setResultActivity extends AppCompatActivity {

    Button askChatbot;
    TextView employerName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_result);

        //버튼 누르면 챗봇으로 이동
        askChatbot = (Button) findViewById(R.id.askChatbot);
        askChatbot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ChatbotActivity.class);
                startActivity(intent);
            }
        });

        // set Image에서 넘어온 회사명 적용하기
        employerName = findViewById(R.id.employer_name);
        Intent result = getIntent();
        String resultName = result.getStringExtra("rEnName");
        employerName.setText(resultName);




    }
}