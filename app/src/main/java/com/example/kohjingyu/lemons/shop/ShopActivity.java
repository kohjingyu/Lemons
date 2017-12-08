package com.example.kohjingyu.lemons.shop;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kohjingyu.lemons.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;

public class ShopActivity extends AppCompatActivity
        implements EquipmentFragment.OnEquipmentSelectedListener, ShopCloneFragment.OnFragmentInteractionListener {
    TextView shopTextView;
    HashMap<String, Integer> equipped;
    ImageView avatarImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shop_activity);



        EquipmentFragment equipmentFragment = new EquipmentFragment();

        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();

        android.support.v4.app.FragmentTransaction fragmentTransaction = fm.beginTransaction();

        fragmentTransaction.add(R.id.shop_container_1, equipmentFragment, "Equipment fragment");

        fragmentTransaction.commit();
        equipped = new HashMap<>();
    }

    //This function is called when an item is clicked
    @Override
    public void addEquipmentToAvatar(String type, int equipmentId) {
        //TODO link this function to url builder to change the image
        equipped.put(type, equipmentId);
        String requestURLString = urlEquipmentBuilder();
        try {
            URL imageURL = new URL(requestURLString);
            UpdateAvatarTask updateAvatarTask = new UpdateAvatarTask();
            updateAvatarTask.execute(imageURL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public String urlEquipmentBuilder(){
        //build url for maple story api
        String requestURL = "";
        String frontURL = "https://labs.maplestory.io/api/gms/latest/character/center/2000/";
        String backURL = "/stand1?showears=false&resize=1";
        Iterator<String> stringIterator = equipped.keySet().iterator();
        String keySet = "";
        while (stringIterator.hasNext()){
            keySet = stringIterator.next();
            frontURL += equipped.get(keySet);
            if (stringIterator.hasNext()){
                frontURL += ",";
            }
        }
        requestURL += frontURL += backURL;
        return requestURL;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        
    }

    public class UpdateAvatarTask extends AsyncTask<URL, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(URL... urls) {
            URL imageURL = urls[0];
            Bitmap avatarPic = null;

            try{
                InputStream in = imageURL.openStream();
                avatarPic = BitmapFactory.decodeStream(in);

            } catch (IOException e){
                e.printStackTrace();
            }

            return avatarPic;
        }

        @Override
        protected void onPostExecute(Bitmap s){
            //TO DO 3.1b (if you wish) display the JSON result in a text view widget for verification purpose

            //TO DO 3.5 Assign it to the text view widget
            avatarImageView = findViewById(R.id.avatar);
            avatarImageView.setImageBitmap(s);
        }
    }




}
