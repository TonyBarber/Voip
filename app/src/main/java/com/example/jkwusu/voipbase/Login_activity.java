package com.example.jkwusu.voipbase;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Message;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class Login_activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_activity);

        Intent intentservice=new Intent(this,SocketService.class);
        startService(intentservice);

        final EditText et_username= (EditText) findViewById(R.id.et_username);
        final EditText et_password= (EditText) findViewById(R.id.et_password);

        Button btn_log= (Button) findViewById(R.id.btn_log);

        btn_log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = et_username.getText().toString();
                final String password = et_password.getText().toString();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Socket s = SocketService.socket;
                            OutputStream os = s.getOutputStream();
                            OutputStreamWriter osw = new OutputStreamWriter(os);
                            PrintWriter pw = new PrintWriter(osw, true);
                            pw.println(username + "%" + password);

                            InputStream is = s.getInputStream();
                            InputStreamReader isr = new InputStreamReader(is);
                            BufferedReader br = new BufferedReader(isr);
                            String yorn = br.readLine();
                            if (yorn.equals("ok")) {
                                Username.username=username;
                                Message msg = new Message();
                                msg.obj = "success";
                                handler.sendMessage(msg);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
    });
    }

    public Handler handler=new Handler(){
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.e("msg",msg.obj.toString());
            String result=msg.obj.toString();
            Log.e("result", result);
            if(result.equals("success")){
                Toast.makeText(Login_activity.this, "success", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Login_activity.this, MainActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(Login_activity.this, "faliure", Toast.LENGTH_LONG).show();
            }
        }

    };


    @Override
    protected void onPause() {
        super.onPause();
        Login_activity.this.finish();
    }
}





//                AsyncTask<Void, String, Void> send = new AsyncTask<Void, String, Void>() {
//                    @Override
//                    protected Void doInBackground(Void... params) {
//                        try {
//                            Socket s = new Socket("192.168.191.1", 8000);
//                            OutputStream os = s.getOutputStream();
//                            OutputStreamWriter osw = new OutputStreamWriter(os);
//                            PrintWriter pw = new PrintWriter(osw, true);
//                            pw.println(username + "%" + password);
//
//                            InputStream is = s.getInputStream();
//                            InputStreamReader isr = new InputStreamReader(is);
//                            BufferedReader br = new BufferedReader(isr);
//                            String yorn = br.readLine();
//                            if (yorn.equals("ok")) {
//                                publishProgress("success");
//                            }
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//
//                        return null;
//                    }
//
//                    @Override
//                    protected void onProgressUpdate(String... values) {
//                        if(values[0].equals("success")){
//                            Toast.makeText(Login_activity.this, "success", Toast.LENGTH_LONG).show();
//                            Intent intent = new Intent(Login_activity.this, MainActivity.class);
//                            startActivity(intent);
//                        } else {
//                            Toast.makeText(Login_activity.this, "faliure", Toast.LENGTH_LONG).show();
//                        }
//                        super.onProgressUpdate(values);
//                    }
//                };
//                send.execute();
//
//            }
