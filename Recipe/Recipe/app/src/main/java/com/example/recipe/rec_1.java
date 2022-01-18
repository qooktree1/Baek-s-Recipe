package com.example.recipe;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.content.Intent;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.TextView;

import com.example.recipe.database.MeterialDBAdapter;

import java.io.Serializable;
import java.util.ArrayList;




public class rec_1 extends AppCompatActivity implements Serializable  {

    ArrayList<String> ar=new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rec_1);



            TextView textView1 = (TextView) findViewById(R.id.text_tag);
            textView1.setText("선택한 재료");




        // 제출하기
        final EditText edittext=(EditText)findViewById(R.id.edittext);
        Button button=(Button)findViewById(R.id.submitButton);
        final TextView textView=(TextView)findViewById(R.id.ings);



        button.setOnClickListener (new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                    String temp=edittext.getText().toString();
                    ar.add(temp);
                    textView.setText(ar.toString());
                    edittext.setText("");


            }
        });


        // 제출하기

        //  초기화 버튼
        Button btn_reset = (Button) findViewById(R.id.reset_btn);

        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ar.removeAll(ar);
                textView.setText(ar.toString());
            }
        });

        // 초기화 버튼
        
        
        
        


        //  결정하기 버튼
        Button btn_sub = (Button) findViewById(R.id.button_sub);

        btn_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),rec_2.class);
                intent.putExtra("ar",ar);
                startActivity(intent);


            }
        });

        // 결정하기 버튼
    }
}