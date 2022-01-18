package com.example.recipe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

import com.example.recipe.adapter.ReciepeAdapter;
import com.example.recipe.database.MeterialDBAdapter;
import com.example.recipe.database.OrderDBAdapter;
import com.example.recipe.database.ReceipeDBAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchReceipeActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;

    private ArrayList<ReceipeData> alReceipe =  new ArrayList<>();

    RecyclerView recyclerView;

    SearchView searchView;

    ReciepeAdapter adapter;

    ReceipeDBAdapter dbAdapter;
    OrderDBAdapter orderDBAdapter;
    MeterialDBAdapter meterialDBAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_receipe);


        recyclerView = findViewById(R.id.recycler);

        adapter = new ReciepeAdapter(this,alReceipe);
        //    recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter.setClickListener(new ReciepeAdapter.ClickListener() {
            @Override
            public void setOnClick(ReceipeData data) {
                Log.e("title",data.getTitle());
                Log.e("img",data.getImg());
                for(String order :data.getOrder()) {
                    Log.e("order", order);
                }
                for(String meterial :data.getMaterial()) {
                    Log.e("meterial", meterial);
                }

                Intent intent = new Intent(SearchReceipeActivity.this,DetailActivity.class);
                intent.putExtra("receipe",data);
                startActivity(intent);


            }
        });
      /*  DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(SearchReceipeActivity.this,LinearLayoutManager.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.divider));
        recyclerView.addItemDecoration(dividerItemDecoration);*/





        recyclerView.setAdapter(adapter);


        searchView = findViewById(R.id.search);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });


        dbAdapter = new ReceipeDBAdapter(this);
        dbAdapter.open();

        orderDBAdapter = new OrderDBAdapter(this);
        orderDBAdapter.open();

        meterialDBAdapter = new MeterialDBAdapter(this);
        meterialDBAdapter.open();

        initReceipe();
    }

    private void initReceipe() {
        alReceipe.clear();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.e("dataSnapshot1",dataSnapshot.getChildrenCount()+"");
                Cursor cursor = dbAdapter.fetchAllEntry();
                if(cursor.getCount()!=dataSnapshot.getChildrenCount()) {
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
                                orderDBAdapter.createEntry(reci.getValue().toString(),alOrder.size()+"",""+i);
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            String strM = "";
                            for (DataSnapshot reci : snapshot.child("재료").getChildren()) {
                                alMeterial.add(reci.getValue().toString());
                                strM +=reci.getValue().toString()+" ";
                            }


                            meterialDBAdapter.createEntry(strM,""+i);


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        alReceipe.add(new ReceipeData(title, img, alOrder, alMeterial));
                        // break;
                        i++;
                    }
                }else{
                    while(cursor.moveToNext()){
                        ArrayList<String> alOrder = new ArrayList<>();
                        ArrayList<String> alMeterial = new ArrayList<>();

                        String title = cursor.getString(1);
                        String img = cursor.getString(2);
                        //순서
                        Cursor orderCursor = orderDBAdapter.selectIDXEntry(cursor.getString(0));
                        while(orderCursor.moveToNext()){
                            alOrder.add(orderCursor.getString(1));
                        }
                        orderCursor.close();
                        //재료
                        Cursor meterialCursor = meterialDBAdapter.selectIDXEntry(cursor.getString(0));
                        while(meterialCursor.moveToNext()){
                            String[] str = meterialCursor.getString(1).split(" ");
                            for(String item : str) {
                                alMeterial.add(item);
                            }
                        }
                        meterialCursor.close();

                        alReceipe.add(new ReceipeData(title, img, alOrder, alMeterial));

                    }
                    cursor.close();
                }

                adapter.dataSetChanged(alReceipe);
                adapter.notifyDataSetChanged();



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}