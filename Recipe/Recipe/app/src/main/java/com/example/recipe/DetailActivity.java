package com.example.recipe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

public class DetailActivity extends AppCompatActivity {

    TextView tv_title,tv_meterial,tv_order;
    ImageView iv_img;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        iv_img = findViewById(R.id.iv_img);

        tv_title = findViewById(R.id.tv_title);

        tv_meterial = findViewById(R.id.tv_meterial);

        tv_order = findViewById(R.id.tv_order);




        Intent intent = getIntent();

        ReceipeData data = (ReceipeData)intent.getSerializableExtra("receipe");

        //이미지
        if(data.getImg()!=null && !data.getImg().equals("")){

            MultiTransformation option = new MultiTransformation(new CenterCrop(), new RoundedCorners(8));
            Glide.with(DetailActivity.this).load(data.getImg())
                    .apply(RequestOptions.bitmapTransform(option))
                    .into(iv_img);
        }

        tv_title.setText(data.getTitle());

        String meterial ="";
        int n = 0;
        for(int i = 0 ; i < data.getMaterial().size();i++){
            if(i== data.getMaterial().size()-1) {

                if(n+data.getMaterial().get(i).length()>=42){
                    meterial += "\n";
                    Log.e("data1",n+" "+data.getMaterial().get(i) );
                    n=0;
                }
                meterial += data.getMaterial().get(i);
                n += data.getMaterial().get(i).length() +  3;
                Log.e("data2",n+" "+data.getMaterial().get(i) );
            }else{
                if(n+data.getMaterial().get(i).length()>=42){
                    meterial += "\n";
                    Log.e("data3",n+" "+data.getMaterial().get(i) );
                    n=0;
                }
                meterial +=  data.getMaterial().get(i) + ", ";
                n += data.getMaterial().get(i).length() +  3;
                Log.e("data4",n+" "+data.getMaterial().get(i) );

            }
        }
        tv_meterial.setText(meterial);

        String order ="";
        for(int i = 0 ; i < data.getOrder().size();i++){
            if(i== data.getOrder().size()-1) {
                order += (i+1) + ". " + data.getOrder().get(i);
            }else{
                order += (i+1) + ". " + data.getOrder().get(i) + "\n\n";
            }
        }
        tv_order.setText(order);


    }
}