package com.example.recipe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.os.Bundle;

import com.example.recipe.database.MeterialDBAdapter;
import com.example.recipe.database.OrderDBAdapter;
import com.example.recipe.database.ReceipeDBAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    DatabaseReference mDatabase;

    ReceipeDBAdapter dbAdapter;
    OrderDBAdapter orderDBAdapter;
    MeterialDBAdapter meterialDBAdapter;

    LodingDialog lodingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lodingDialog = new LodingDialog(this, "데이터 가져오는중입니다.");

        initData();

        // 레시피 추천 버튼 1 클릭 이벤트
        Button btn_main1 = (Button) findViewById(R.id.button_main1);
        btn_main1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), rec_1.class);
                startActivity(intent);
            }
        });

        Button btn_main2 = (Button) findViewById(R.id.button_main2);
        btn_main2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SearchReceipeActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initData() {

        lodingDialog.show();

        dbAdapter = new ReceipeDBAdapter(this);
        dbAdapter.open();

        orderDBAdapter = new OrderDBAdapter(this);
        orderDBAdapter.open();

        meterialDBAdapter = new MeterialDBAdapter(this);
        meterialDBAdapter.open();

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.e("dataSnapshot1", dataSnapshot.getChildrenCount() + "");
                Cursor cursor = dbAdapter.fetchAllEntry();
                if (cursor.getCount() != dataSnapshot.getChildrenCount()) {
                    dbAdapter.delAllEntry();
                    int i = 0;
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        Log.d("MainActivity", "ValueEventListener : " + snapshot);

                        String title = snapshot.getKey().toString();
                        String img = snapshot.child("이미지").getValue().toString();

                        dbAdapter.createEntry(i + "", title, img);
                        ArrayList<String> alOrder = new ArrayList<>();
                        ArrayList<String> alMeterial = new ArrayList<>();


                        try {
                            for (DataSnapshot reci : snapshot.child("순서").getChildren()) {
                                alOrder.add(reci.getValue().toString());
                                Log.e("Str", reci.getValue().toString());
                                orderDBAdapter.createEntry(reci.getValue().toString(), alOrder.size() + "", "" + i);
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            String strM = "";
                            for (DataSnapshot reci : snapshot.child("재료").getChildren()) {
                                alMeterial.add(reci.getValue().toString());
                                strM += reci.getValue().toString() + " ";
                            }


                            meterialDBAdapter.createEntry(strM, "" + i);


                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        // break;
                        i++;
                    }
                }

                lodingDialog.dismiss();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}