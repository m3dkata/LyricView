package com.kidney.breznitsasuri2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

public class SuriListAdapter extends RecyclerView.Adapter<SuriListAdapter.ViewHolder>{

    static ArrayList<Suri> suriList;
    Context context;

    public SuriListAdapter(ArrayList<Suri> suriList, Context context) {
        this.suriList = suriList;
        this.context = context;
    }

    public SuriListAdapter(Context applicationContext, RecyclerView recyclerView, ArrayList<Suri> searchSuri) {
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_item, parent, false);
        return new SuriListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SuriListAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        Suri suriData = suriList.get(position);
        holder.titleTextView.setText(suriData.name);
        holder.iconImageView.setImageResource(suriData.getSuraimage());
        if(MyMediaPlayer.currentIndex==position)
            holder.titleTextView.setTextColor(Color.parseColor("#FF0000"));
        else{
            holder.titleTextView.setTextColor(Color.parseColor("#FFFFFF"));
        }
        //Uri mediaPath =  Uri.parse(String.format("android.resource://%s/%s/%s",context.getPackageName(),"raw", suriData.getSurafile()));
        //final Suri mediaPath = suriData;
        //String mediaPath = "android.resource://" + context.getPackageName() + "/" + suriData.suraimage;




        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MyMediaPlayer.getInstance().reset();
                MyMediaPlayer.currentIndex = position;
                Intent intent = new Intent(context,MusicPlayerActivity.class);
                intent.putExtra("LIST",suriList);

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return suriList != null ? suriList.size(): 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView titleTextView;
        ImageView iconImageView;
        CardView cardView ;
        public ViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.music_title_text);
            iconImageView = itemView.findViewById(R.id.icon_view);

        }
    }

}
