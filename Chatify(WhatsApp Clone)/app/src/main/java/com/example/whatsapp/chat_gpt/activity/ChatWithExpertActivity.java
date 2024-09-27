package com.example.whatsapp.chat_gpt.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.Toast;
import com.example.whatsapp.R;
import com.example.whatsapp.chat_gpt.adapter.MyAdapter;
import com.example.whatsapp.chat_gpt.models.MessageModel;
import com.example.whatsapp.databinding.ActivityChatWithExpertBinding;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ChatWithExpertActivity extends AppCompatActivity {

    ActivityChatWithExpertBinding chatBinding;
    ArrayList<MessageModel> messageModels;
    MyAdapter myAdapter;
   final private String USER_KEY="user";
   final private String BOT_KEY="bot";
    public static final MediaType JSON = MediaType.get("application/json");

    OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        chatBinding= ActivityChatWithExpertBinding.inflate(getLayoutInflater());

        setContentView(chatBinding.getRoot());
        Objects.requireNonNull(getSupportActionBar()).hide();

        messageModels=new ArrayList<>();
        myAdapter=new MyAdapter(messageModels, getApplicationContext());
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        chatBinding.chatRecycle.setLayoutManager(layoutManager);
        chatBinding.chatRecycle.setAdapter(myAdapter);

        chatBinding.edMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(TextUtils.isEmpty(chatBinding.edMessage.getText().toString()))
                {
                    chatBinding.sendMessage.setImageDrawable(getDrawable(R.drawable.ic_baseline_keyboard_voice_24));
                }
                else
                {
                    chatBinding.sendMessage.setImageDrawable(getDrawable(R.drawable.ic_baseline_send_24));

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        chatBinding.sendMessage.setOnClickListener(v -> {
            if(chatBinding.edMessage.getText().toString().isEmpty())
            {
                Toast.makeText(ChatWithExpertActivity.this, "Please enter your message..", Toast.LENGTH_SHORT).show();
            }
            else {
                String userMessage=chatBinding.edMessage.getText().toString();
                addMessage(userMessage,USER_KEY);
                chatBinding.edMessage.setText("");
                aiProcess(userMessage);

            }

        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void addMessage(String userMessage,String key) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                messageModels.add(new MessageModel(userMessage, key));
                myAdapter.notifyDataSetChanged();
                chatBinding.chatRecycle.smoothScrollToPosition(myAdapter.getItemCount());}
        }); }


    @SuppressLint("NotifyDataSetChanged")
    private void responseMessage(String response){
       addMessage(response,BOT_KEY);
    }

    private void aiProcess(String userMessage) {

        JSONObject requestBody=new JSONObject();
        try {
            requestBody.put("model", "gpt-3.5-turbo-instruct");
            requestBody.put("prompt", userMessage);
            requestBody.put("max_tokens", 4000);
            requestBody.put("temperature", 0);

        }catch (Exception ex)
        {
            ex.printStackTrace();
        }

        RequestBody body = RequestBody.create(requestBody.toString(),JSON);
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url("https://api.openai.com/v1/completions")
                .header("Authorization","Bearer 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")
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
               responseMessage("Something went wrong "+e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                if (response.isSuccessful()){
                    JSONObject jsonObject = null;

                try {

                    assert response.body() != null;
                    jsonObject = new JSONObject(response.body().string());
                    JSONArray js = jsonObject.getJSONArray("choices");
                    String text = js.getJSONObject(0).getString("text");

                    responseMessage(text.trim());
                }
                    catch (Exception ex) {
                     ex.printStackTrace();}
                }

                else {
                    assert response.body() != null;
                   responseMessage(response.body().string());
                }
            }
        });

    }






}