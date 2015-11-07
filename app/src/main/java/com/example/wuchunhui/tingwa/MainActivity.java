package com.example.wuchunhui.tingwa;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.List;

import retrofit.Call;
import retrofit.Retrofit;
import retrofit.http.GET;
import retrofit.http.Path;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView mTextView;
    private Button mBtnVolley,mBtnRetrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextView = (TextView) findViewById(R.id.text);
        mBtnRetrofit = (Button) findViewById(R.id.retofit);
        mBtnVolley = (Button) findViewById(R.id.volley);

        mBtnRetrofit.setOnClickListener(this);
        mBtnVolley.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.volley:
                VolleyTest();
                break;
            case R.id.retofit:
                RetrofitTest();
                break;
        }
    }

    private void VolleyTest(){
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://www.itingwa.com";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        mTextView.setText("Response is: " + response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mTextView.setText("That didn't work!");
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private void RetrofitTest(){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://www.itingwa.com/")
//                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public interface GitHubService {
        @GET("/users/{user}/repos")
        Call<List<Response>> listRepos(@Path("user") String user);
    }

}
