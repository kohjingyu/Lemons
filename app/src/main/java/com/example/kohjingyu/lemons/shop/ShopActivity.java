package com.example.kohjingyu.lemons.shop;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.kohjingyu.lemons.R;

public class ShopActivity extends AppCompatActivity
        implements EquipmentFragment.OnEquipmentSelectedListener {
    TextView shopTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shop_activity);

        EquipmentFragment equipmentFragment = new EquipmentFragment();

        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();

        android.support.v4.app.FragmentTransaction fragmentTransaction = fm.beginTransaction();

        fragmentTransaction.add(R.id.shop_container_1, equipmentFragment, "Equipment fragment");

        fragmentTransaction.commit();

    }

    @Override
    public void addEquipmentToAvatar(String equipmentId) {
        shopTextView = findViewById(R.id.shop_title);
        shopTextView.setText(equipmentId);
    }
}
