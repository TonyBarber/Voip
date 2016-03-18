package com.example.jkwusu.voipbase;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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
import java.util.ArrayList;
import java.util.List;

public class FriendAddActivity extends AppCompatActivity {


    BaseAdapter adapter=null;
    List<String> list2=new ArrayList<String>();
    String userto2=null;
    final Socket s = SocketService.socket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_add);


        Button btn_sumbit2= (Button) findViewById(R.id.btn_submit2);
        final EditText et_input2= (EditText) findViewById(R.id.et_input2);
        ListView listView2 = (ListView) findViewById(R.id.listView2);
        adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, list2);
        listView2.setAdapter(adapter);


        try {
            OutputStream os = s.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os);
            PrintWriter pw = new PrintWriter(osw, true);
            pw.println("getfriend%" + Username.username+"%d%d");
        } catch (IOException e) {
            e.printStackTrace();
        }

        MyThread1 myThread1=new MyThread1();
        new Thread(myThread1).start();



        btn_sumbit2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userto=et_input2.getText().toString();
                userto2=userto;
                try {
                    Log.e("hahah",userto2);
                    OutputStream os = s.getOutputStream();
                    OutputStreamWriter osw = new OutputStreamWriter(os);
                    PrintWriter pw = new PrintWriter(osw, true);
                    pw.println("addfriend%" + Username.username+"%"+userto+"%d");
                } catch (IOException e) {
                    e.printStackTrace();
                }

                MyThread1 myThread2=new MyThread1();
                new Thread(myThread2).start();

            }
        });

    }

    class MyThread1 implements Runnable {
        @Override
        public void run() {
            try {
                InputStream is = s.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);

                String message = br.readLine();
                String[] type = message.split("%");
                Message msg = new Message();
                if (type[0].equals("friendlist")) {
                    msg.what = 1;
                    msg.obj = type;
                    handler.sendMessage(msg);
                } else if(type[0].equals("success")) {
                    msg.what = 3;
                    handler.sendMessage(msg);
                }
                else {
                    msg.what = 2;
                    msg.obj = "error";
                    handler.sendMessage(msg);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }





    Handler handler=new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            String[] a;
            switch (msg.what){
                case 1:
                    a= (String[]) msg.obj;
                    Log.e("the result is","r");
                    int count=1;
                    while(count<a.length){
                    list2.add(a[count]);
                    count++;
                    }
                    adapter.notifyDataSetChanged();
                    break;

                case 2:
                    Toast.makeText(FriendAddActivity.this,"出错啦",Toast.LENGTH_LONG).show();
                    break;

                case 3:
                    list2.add(userto2);
                    adapter.notifyDataSetChanged();
                    break;
            }

        }
    };


    protected void onStop() {
        super.onStop();
        FriendAddActivity.this.finish();
    }

}



