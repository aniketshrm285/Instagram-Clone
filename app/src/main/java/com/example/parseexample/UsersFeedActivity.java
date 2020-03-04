package com.example.parseexample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.parseexample.adapter.RecyclerViewAdapter;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class UsersFeedActivity extends AppCompatActivity {

    private ArrayList<byte[]> myPhotos;

    private RecyclerView recyclerView;

    private RecyclerViewAdapter recyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_feed);

        myPhotos = new ArrayList<>();

        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //setup adapter

        recyclerViewAdapter = new RecyclerViewAdapter(this,myPhotos);

        recyclerView.setAdapter(recyclerViewAdapter);

        Intent intent = getIntent();

        String username = intent.getStringExtra("username");

        setTitle(username+"'s Photos");

        ParseQuery<ParseObject> query= new ParseQuery<>("Image");

        query.whereEqualTo("username",username);
        query.addDescendingOrder("createdAt");

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e == null && objects.size() > 0){
                    for(ParseObject object : objects){
                        ParseFile file = object.getParseFile("image");

                        file.getDataInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] data, ParseException e) {
                                if(e == null && data != null){
                                   // Bitmap bitmap = BitmapFactory.decodeByteArray(data,0,data.length);

                                    myPhotos.add(data);
                                    Log.i("PhotoInfo","Fetched");
                                    recyclerViewAdapter.notifyDataSetChanged();

                                }
                            }
                        });
                        recyclerViewAdapter.notifyDataSetChanged();

                    }
                    //notify data set changed
                    recyclerViewAdapter.notifyDataSetChanged();

                }
            }
        });





    }
}
