package com.example.wuchunhui.tingwa;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import retrofit.Call;
import retrofit.Retrofit;
import retrofit.http.GET;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView mTextView;
    private Button mBtnRetrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextView = (TextView) findViewById(R.id.text);
        mBtnRetrofit = (Button) findViewById(R.id.retofit);

        mBtnRetrofit.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.retofit:
                RetrofitTest();
                break;
        }
    }

    private void RetrofitTest(){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://www.itingwa.com/")
//                .addConverterFactory(GsonConverterFactory.create())
                .build();

        TingWaValue service = retrofit.create(TingWaValue.class);

        Call<String> str = service.listRepos();
        mTextView.setText(str.toString());

    }

    public interface TingWaValue {
        @GET("/u/103559")
        Call<String> listRepos();
    }

}
