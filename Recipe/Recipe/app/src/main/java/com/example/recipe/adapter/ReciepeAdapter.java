package com.example.recipe.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.recipe.R;
import com.example.recipe.ReceipeData;


import java.util.ArrayList;
import java.util.List;


public class ReciepeAdapter extends RecyclerView.Adapter<ReciepeAdapter.MyViewHolder> implements View.OnClickListener, Filterable {

    public ArrayList<ReceipeData> outAllDataList = new ArrayList<>();
    public ArrayList<ReceipeData> outDataList =  new ArrayList<>();
    Context context;

    ClickListener clickListener;

    public ReciepeAdapter(Context context, ArrayList<ReceipeData> outDataList) {
        this.context = context;
        this.outAllDataList.addAll(outDataList);
        this.outDataList.addAll(outDataList);
    }

    //data set changed
    public void dataSetChanged(ArrayList<ReceipeData> outDataList) {
        this.outDataList.clear();
        this.outAllDataList.clear();
        this.outAllDataList.addAll(outDataList);
        this.outDataList.addAll(outDataList);

        Log.e("outDataList.size()",outDataList.size()+" "+this.outAllDataList.size());
        notifyDataSetChanged();
    }



    @Override
    public ReciepeAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_receipe,parent,false);
        MyViewHolder viewHolder = new MyViewHolder(v);
        return viewHolder;
    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(ReciepeAdapter.MyViewHolder holder, int position) {

        ReceipeData data = outDataList.get(position);

        MultiTransformation option = new MultiTransformation(new CenterCrop(), new RoundedCorners(8));


        //제목
        holder.tv_title.setText(data.getTitle());
        //이미지
        if(data.getImg()!=null && !data.getImg().equals("")){
            Glide.with(context).load(data.getImg())
                    .apply(RequestOptions.bitmapTransform(option))
                    .into(holder.iv_img);
        }
    //    holder.tv_writer.setText(data.getWriter());

        holder.myLayout.setTag(position);
        holder.myLayout.setOnClickListener(this);


    }
    @Override
    public void onClick(View view) {
        //아이템 클릭시 세부정보 확인
        int position  = (int) view.getTag();
        if(clickListener!=null){
            clickListener.setOnClick(outDataList.get(position));
        }
    }

    public interface ClickListener {
        void setOnClick(ReceipeData data);
    }


    @Override
    public int getItemCount() {
        return outDataList.size();
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        //Automatic on background thread
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<ReceipeData> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(outAllDataList);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (ReceipeData item : outAllDataList) {
                    //TODO filter 대상 setting
                    if (item.getTitle().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        //Automatic on UI thread
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            outDataList.clear();
            outDataList.addAll((List) results.values);
            Log.e("outDataList",outDataList.size()+"");
            Log.e("outAllDataList",outAllDataList.size()+"");
            notifyDataSetChanged();
        }
    };

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        //제목
        public TextView tv_title;
        //작성자
        public ImageView iv_img;



        LinearLayout myLayout;

        Context context;

        public MyViewHolder(View itemView) {
            super(itemView);
            myLayout = (LinearLayout) itemView;

            //myLayout.setBackgroundColor(Color.RED);


            tv_title = itemView.findViewById(R.id.tv_title);
            iv_img = itemView.findViewById(R.id.iv_img);

            context = itemView.getContext();

        }


    }

}
