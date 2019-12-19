package com.example.autowork.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.autowork.GlobalVariabel;
import com.example.autowork.R;
import com.example.autowork.model.UserMan;
import com.example.autowork.owner.BossActivity;
import com.example.autowork.owner.DetailUserActivity;

import java.util.List;

public class MemintaUserManboss extends RecyclerView.Adapter<MemintaUserManboss.MyViewHolder> {

    private List<UserMan> moviesList;
    private Activity mActivity;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout rl_layoutuseman;
        public TextView tv_nama, tv_email, tv_tap;

        public MyViewHolder(View view) {
            super(view);
            rl_layoutuseman = view.findViewById(R.id.rl_layout);
//            tv_barkod = view.findViewById(R.id.tv_barkod);
            tv_nama = view.findViewById(R.id.tv_nama);
            tv_email = view.findViewById(R.id.tv_email);
            tv_tap = view.findViewById(R.id.tv_tap);
        }
    }

    public MemintaUserManboss(List<UserMan> moviesList, Activity activity) {
        this.moviesList = moviesList;
        this.mActivity = activity;
    }

    @Override
    public MemintaUserManboss.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.meminta_userman, parent, false);

        return new MemintaUserManboss.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MemintaUserManboss.MyViewHolder holder, final int position) {
        final UserMan movie = moviesList.get(position);

//        holder.tv_barkod.setText(movie.getBarkod());
        holder.tv_nama.setText(movie.getNama());
        holder.tv_email.setText(movie.getEmail());
        holder.tv_tap.setText(" ");
//        holder.ll_tapuserman.movie



        holder.tv_tap.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                BossActivity.uid = movie.getUid();
//
//                BossActivity.FragmentVar="DetailuserBossFragment";

                GlobalVariabel.uid = movie.getUid();
//                GlobalVariabel.VarFragmen="DetailuserBossFragment";

                Intent intent = new Intent(mActivity, DetailUserActivity.class);

                mActivity.startActivity(intent);
//                mActivity.finish();


            }
        });

//        holder.tv_nama.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Intent goDetail = new Intent(mActivity, MainActivity.class);
////                goDetail.putExtra("id", movie.getKey());
//                goDetail.putExtra("title", movie.getNama());
//                goDetail.putExtra("email", movie.getEmail());
//
//                mActivity.startActivity(goDetail);
//
//
//            }
//        });



    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

}