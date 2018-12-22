package com.example.josee.codingcafe;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.josee.codingcafe.client.MessagingClient;
import com.example.josee.codingcafe.client.MessagingInterface;
import com.example.josee.codingcafe.client.RequestBody;
import com.example.josee.codingcafe.client.ResponseBody;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityRetrofit extends AppCompatActivity {

    private Button sendBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit);

        getSupportActionBar().setTitle("R E T R O F I T");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sendBtn = this.findViewById(R.id.sendBtn);

        sendBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                sendMessage();
            }
        });
    }

    private void sendMessage()
    {
        JSONObject jsobObj = new JSONObject();
        try
        {
            jsobObj.accumulate("message", new String[]{"hello my world", "1234"});
        }
        catch(Exception e){}

        RequestBody req = new RequestBody(jsobObj.toString());
        MessagingInterface messagingInterface = MessagingClient.getClient().create(MessagingInterface.class);
        Call<ResponseBody> sendMessage = messagingInterface.sendMessage(req);
        sendMessage.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try{
                    JSONObject responseMessage = new JSONObject(response.body().getResponse());
                    Log.e("RESPONSE", responseMessage.getString("message"));
                }
                catch(Exception err){
                    Log.e("JSONERROR", err.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("ERROR", t.getMessage());
            }
        });
    }
}
