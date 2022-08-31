package ds.project.tadaktadakfront.chatbot;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import ds.project.tadaktadakfront.chatbot.adapters.ChatAdapter;
import ds.project.tadaktadakfront.chatbot.helpers.SendMessageInBg;
import ds.project.tadaktadakfront.chatbot.interfaces.BotReply;
import ds.project.tadaktadakfront.chatbot.models.Message;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.dialogflow.v2.DetectIntentResponse;
import com.google.cloud.dialogflow.v2.QueryInput;
import com.google.cloud.dialogflow.v2.SessionName;
import com.google.cloud.dialogflow.v2.SessionsClient;
import com.google.cloud.dialogflow.v2.SessionsSettings;
import com.google.cloud.dialogflow.v2.TextInput;
import com.google.common.collect.Lists;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import ds.project.tadaktadakfront.R;


public class NaviChatbot extends Fragment implements BotReply {

  RecyclerView chatView;
  ChatAdapter chatAdapter;
  List<Message> messageList = new ArrayList<>();
  EditText editMessage;
  ImageButton btnSend;

  //dialogFlow
  private SessionsClient sessionsClient;
  private SessionName sessionName;
  private String uuid = UUID.randomUUID().toString();
  private String TAG = "navichatbot";

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {

    View view = inflater.inflate(R.layout.fragment_navi_chat_bot, container, false);
    chatView = view.findViewById(R.id.chatView);
    editMessage = view.findViewById(R.id.editMessage);
    btnSend = view.findViewById(R.id.btnSend);

    chatAdapter = new ChatAdapter(messageList, getActivity());
    chatView.setAdapter(chatAdapter);

    if(btnSend != null) {
      btnSend.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          String message = editMessage.getText().toString();
          if (!message.isEmpty()) {
            messageList.add(new Message(message, false));
            editMessage.setText("");
            sendMessageToBot(message);
            Objects.requireNonNull(chatView.getAdapter()).notifyDataSetChanged();
            Objects.requireNonNull(chatView.getLayoutManager())
                    .scrollToPosition(messageList.size() - 1);
          } else {
            Toast.makeText(getActivity(), "Please enter text!", Toast.LENGTH_SHORT).show();
          }
        }
      });
    }

    setUpBot();

    return view;
  }


  private void setUpBot() {
    try {
      InputStream stream = getActivity().getResources().openRawResource(R.raw.credential);
      GoogleCredentials credentials = GoogleCredentials.fromStream(stream)
          .createScoped(Lists.newArrayList("https://www.googleapis.com/auth/cloud-platform"));
      String projectId = ((ServiceAccountCredentials) credentials).getProjectId();

      SessionsSettings.Builder settingsBuilder = SessionsSettings.newBuilder();
      SessionsSettings sessionsSettings = settingsBuilder.setCredentialsProvider(
          FixedCredentialsProvider.create(credentials)).build();
      sessionsClient = SessionsClient.create(sessionsSettings);
      sessionName = SessionName.of(projectId, uuid);

      Log.d(TAG, "projectId : " + projectId);
    } catch (Exception e) {
      Log.d(TAG, "setUpBot: " + e.getMessage());
    }
  }

  private void sendMessageToBot(String message) {
    QueryInput input = QueryInput.newBuilder()
        .setText(TextInput.newBuilder().setText(message).setLanguageCode("en-US")).build();
    new SendMessageInBg(this, sessionName, sessionsClient, input).execute();
  }

  @Override
  public void callback(DetectIntentResponse returnResponse) {
     if(returnResponse!=null) {
       String botReply = returnResponse.getQueryResult().getFulfillmentText();
       if(!botReply.isEmpty()){
         messageList.add(new Message(botReply, true));
         chatAdapter.notifyDataSetChanged();
         Objects.requireNonNull(chatView.getLayoutManager()).scrollToPosition(messageList.size() - 1);
       }else {
         Toast.makeText(getActivity(), "something went wrong", Toast.LENGTH_SHORT).show();
       }
     } else {
       Toast.makeText(getActivity(), "failed to connect!", Toast.LENGTH_SHORT).show();
     }
  }
}
