package com.example.jkwusu.voipbase;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class PayActivity extends AppCompatActivity {

    String usernameto=Username.username;
    TextView et_balance;
    final Socket s = SocketService.socket;
    send sent=new send();
    boolean exit=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

        final EditText et_money = (EditText) findViewById(R.id.et_money);
        Button btn_sum_money = (Button) findViewById(R.id.btn_sum_money);
        final EditText et_usernameto = (EditText) findViewById(R.id.et_usernameto);
        et_balance = (TextView) findViewById(R.id.et_showbalance);


        try {
            OutputStream os = s.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os);
            PrintWriter pw = new PrintWriter(osw, true);
            pw.println("checkbalance%" + Username.username + "%b%b%");
        } catch (IOException e) {
            e.printStackTrace();
        }


        btn_sum_money.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String money = et_money.getText().toString();
                usernameto = et_usernameto.getText().toString();
                try {
                    OutputStream os = s.getOutputStream();
                    OutputStreamWriter osw = new OutputStreamWriter(os);
                    PrintWriter pw = new PrintWriter(osw, true);
                    pw.println("pay%" + Username.username + "%" + usernameto + "%" + money);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        sent.execute();
    }


    class send extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] params) {
            try {

                InputStream is = s.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
                while(!exit){
                    String yorn = br.readLine();
                    String type=yorn.split("%")[0];
                    if (type.equals("ok")){
                        publishProgress(yorn.split("%")[1]);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Object[] values) {
            et_balance.setText("当前余额是："+values[0]);
            super.onProgressUpdate(values);
        }

    };


    @Override
    protected void onStop() {
        super.onStop();
        PayActivity.this.finish();
        exit=true;
    }




}


