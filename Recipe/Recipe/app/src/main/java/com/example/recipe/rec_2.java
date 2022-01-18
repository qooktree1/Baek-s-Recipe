package com.example.recipe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import android.content.Intent;
import android.widget.ListAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.example.recipe.adapter.ReciepeAdapter;
import com.example.recipe.database.MeterialDBAdapter;
import com.example.recipe.database.OrderDBAdapter;
import com.example.recipe.database.ReceipeDBAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.database.Query;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class rec_2 extends AppCompatActivity{

    private ArrayList<ReceipeData> alReceipe =  new ArrayList<>();

    RecyclerView recyclerView;

    ReciepeAdapter adapter;

    ReceipeDBAdapter dbAdapter;
    OrderDBAdapter orderDBAdapter;
    MeterialDBAdapter meterialDBAdapter;

    ArrayList<String> idxList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rec_2);
        ArrayList<String> ar =  getIntent().getStringArrayListExtra("ar");



        recyclerView = findViewById(R.id.recycler);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ReciepeAdapter(this,alReceipe);

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

                Intent intent = new Intent(rec_2.this,DetailActivity.class);
                intent.putExtra("receipe",data);
                startActivity(intent);


            }
        });
        /*DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rec_2.this,LinearLayoutManager.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.divider));
        recyclerView.addItemDecoration(dividerItemDecoration);*/

        recyclerView.setAdapter(adapter);

        getData(ar);


    }

    private void getData(ArrayList<String> ar) {
        dbAdapter =  new ReceipeDBAdapter(this);
        dbAdapter.open();

        orderDBAdapter = new OrderDBAdapter(this);
        orderDBAdapter.open();

        meterialDBAdapter =  new MeterialDBAdapter(rec_2.this);
        meterialDBAdapter.open();


        Cursor c = meterialDBAdapter.joinAllEntry(ar);
        while(c.moveToNext()){
            Log.e("Meterial",c.getString(0)+" "+c.getString(1)+" "+c.getString(2));
            idxList.add(c.getString(3));
        }


        for(String idx : idxList) {
            String title= "";
            String img= "";
            ArrayList<String> alOrder =  new ArrayList<>();
            ArrayList<String> alMeterial =  new ArrayList<>();
            Cursor c1 = dbAdapter.selectIDXEntry(idx);
            if(c1.moveToNext()) {
                title = c1.getString(1);
                img = c1.getString(2);
            }
            c1.close();
            Cursor c2 = orderDBAdapter.selectIDXEntry(idx);
            while(c2.moveToNext()){
                alOrder.add(c2.getString(1));
            }
            c2.close();

            Cursor c3 = meterialDBAdapter.selectIDXEntry(idx);
            while(c3.moveToNext()){
                alMeterial.add(c3.getString(1));
            }
            c3.close();
            alReceipe.add(new ReceipeData(title,img,alOrder,alMeterial));
        }

        adapter.dataSetChanged(alReceipe);
        Log.e("alReceipe",alReceipe.size()+"");
        meterialDBAdapter.close();
        orderDBAdapter.close();
        dbAdapter.close();

    }


}

