package com.jacksen.retrofitdemo;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {


    private EditText inputPhoneEt;
    private Button queryPhoneBtn;
    private TextView phoneTv;
    private TextView prefixTv;
    private TextView operatorTv;
    private TextView cityTv;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputPhoneEt = (EditText) findViewById(R.id.input_phone_et);
        queryPhoneBtn = (Button) findViewById(R.id.query_phone_btn);
        phoneTv = (TextView) findViewById(R.id.phone_tv);
        prefixTv = (TextView) findViewById(R.id.prefix_tv);
        operatorTv = (TextView) findViewById(R.id.operator_tv);
        cityTv = (TextView) findViewById(R.id.city_tv);


        queryPhoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone = inputPhoneEt.getText().toString();
                startQuery(phone);
            }
        });
    }


    /**
     * 查询手机号归属地信息
     *
     * @param phoneNum
     */
    private void startQuery(final String phoneNum) {
        progressDialog = ProgressDialog.show(this, null, "正在查询...");

        /*Interceptor headerInterceptor = new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                okhttp3.Response response = chain.proceed(chain.request());
                //


                return response;
            }
        };

        OkHttpClient client = new OkHttpClient();
        client.interceptors().add(headerInterceptor);*/

        // 1. 创建Retrofit对象
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)

                .addConverterFactory(GsonConverterFactory.create())
//                .client(client)
                .build();

        // 2. 创建访问API的请求
        ApiStore apiStore = retrofit.create(ApiStore.class);
        Call<MobileInfo> call = apiStore.getMobileInfo(phoneNum);

        // 3. 发送请求，在回调函数中处理结果
        call.enqueue(new Callback<MobileInfo>() {
            @Override
            public void onResponse(Call<MobileInfo> call, Response<MobileInfo> response) {

                if (response == null || response.body() == null) {
                    Toast.makeText(MainActivity.this, "解析失败", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    String errMsg = response.errorBody().string();
                    Toast.makeText(MainActivity.this, errMsg, Toast.LENGTH_SHORT).show();
                    return;
                } catch (IOException e) {
                    e.printStackTrace();
                }

                MobileInfo.RetDataEntity retData = response.body().getRetData();
                phoneTv.setText(retData.getPhone());
                prefixTv.setText(retData.getPrefix());
                operatorTv.setText(retData.getSupplier());
                cityTv.setText(retData.getCity());
                //
                dialogDismiss();
                Toast.makeText(MainActivity.this, "查询成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<MobileInfo> call, Throwable t) {

                //
                dialogDismiss();
                Toast.makeText(MainActivity.this, "查询失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 关闭进度对话框
     */
    private void dialogDismiss() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
