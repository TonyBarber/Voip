package com.example.jkwusu.voipbase;

import android.content.Intent;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

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

import android.os.Handler;
import android.widget.TextView;

public class Voip extends AppCompatActivity {

    BaseAdapter adapter=null;
    List<String> list=new ArrayList<String>();
    TextView textView;
    String usernameto=Username.username;
    boolean exit=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voip);

        textView= (TextView) findViewById(R.id.message_get);
        final Socket s=SocketService.socket;
        final ListView listView = (ListView) findViewById(R.id.listView);
        adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);

        final EditText et_input= (EditText) findViewById(R.id.et_input);
        Button btn_sumbit= (Button) findViewById(R.id.btn_submit);
        btn_sumbit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String text = et_input.getText().toString();
                try {
                    OutputStream os = s.getOutputStream();
                    OutputStreamWriter osw = new OutputStreamWriter(os);
                    PrintWriter pw = new PrintWriter(osw, true);
                    pw.println("msg%" + Username.username + "%"+usernameto+"%"+text);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                usernameto = (String) listView.getAdapter().getItem(position);
            }
        });



        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    InputStream is = s.getInputStream();
                    InputStreamReader isr=new InputStreamReader(is);
                    BufferedReader br=new BufferedReader(isr);

                    while (!exit){
                        String message=br.readLine();
                        String type=message.split("%")[0];
                        String mess=message.split("%")[1];
                        Message msg=new Message();
                        if(type.equals("add")){
                            msg.what=1;
                            msg.obj=mess;
                            handler.sendMessage(msg);
                        }
                        if(type.equals("exit")){
                            msg.what=2;
                            msg.obj=mess;
                            handler.sendMessage(msg);
                        }
                        if(type.equals("mess")){
                            msg.what=3;
                            msg.obj=mess;
                            handler.sendMessage(msg);
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    Handler handler=new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    list.add(msg.obj.toString());
                    adapter.notifyDataSetChanged();
                    break;
                case 2:
                    list.remove(msg.obj.toString());
                    adapter.notifyDataSetChanged();
                    break;
                case 3:
                    textView.setText(msg.obj.toString());
                    break;
            }

        }
    };

    @Override
    protected void onStop() {
        super.onStop();
        Voip.this.finish();
        exit=true;
    }

}
