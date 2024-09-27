package com.example.whatsapp.chat_gpt.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.example.whatsapp.activity.MainActivity;
import com.example.whatsapp.databinding.ActivityGenerateImageBinding;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class GenerateImageActivity extends AppCompatActivity {

    ActivityGenerateImageBinding imageBinding;
    String accessToken="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";
    private String apiUrl= "https://api.openai.com/v1/images/generations";

    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");

    OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageBinding= ActivityGenerateImageBinding.inflate(getLayoutInflater());

        setContentView(imageBinding.getRoot());
        Objects.requireNonNull(getSupportActionBar()).hide();

        imageBinding.generateImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String input= imageBinding.inputToGenerateImg.getText().toString().trim();

               if (input.isEmpty())
               {
                   Toast.makeText(GenerateImageActivity.this, "Text can't be empty", Toast.LENGTH_SHORT).show();
               }
               else {
                   callApi(input);
               }
            }
        });
    }

    private void callApi(String input) {

        setProgressbar(true);
        JSONObject jsonBody=new JSONObject();
        try {
            jsonBody.put("prompt",input);
            jsonBody.put("size","256x256");

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        RequestBody requestBody=RequestBody.create(jsonBody.toString(),JSON);
        Request request=new Request.Builder()
                        .url(apiUrl)
                        .header("Authorization","Bearer "+accessToken)
                        .post(requestBody)
                        .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Toast.makeText(GenerateImageActivity.this, "Failed to generate image", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
               // Log.i("Response :",response.body().string());

                try {
                    assert response.body() != null;
                    JSONObject  jsonObject=new JSONObject(response.body().string());
                    String imageUrl=jsonObject.getJSONArray("data").getJSONObject(0).getString("url");
                    loadImageUrl(imageUrl);
                    setProgressbar(false);
                    imageBinding.inputToGenerateImg.setText("");
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });

    }

    private void loadImageUrl(String imageUrl) {
        runOnUiThread(()->{
            Picasso.get().load(imageUrl).into(imageBinding.generatedImg);
        });


    }

    private void setProgressbar(boolean inProgress) {
        runOnUiThread(()->{

            if(inProgress)
            {
                imageBinding.progressbar.setVisibility(View.VISIBLE);
                imageBinding.generateImgBtn.setVisibility(View.GONE);

            }
            else
            {
                imageBinding.progressbar.setVisibility(View.GONE);
                imageBinding.generateImgBtn.setVisibility(View.VISIBLE);
            }
        });


        imageBinding.backArrow.setOnClickListener(v -> startActivity(new Intent(GenerateImageActivity.this, MainActivity.class)));
    }


}