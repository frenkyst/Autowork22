package com.example.autowork.owner;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.autowork.R;

public class DetailUserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_user);

        DetailuserBossFragment fragment = new DetailuserBossFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.framedetailuser, fragment);
        fragmentTransaction.commit();
    }
}

//==================================================================================================
// ACTIVITY UNTUK MENAMPILKAN FRAGMENT DetaiuserBossFragment
//==================================================================================================
