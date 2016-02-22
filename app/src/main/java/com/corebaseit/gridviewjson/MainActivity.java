package com.corebaseit.gridviewjson;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;

import com.google.gson.reflect.TypeToken;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import com.google.gson.GsonBuilder;


public class MainActivity extends AppCompatActivity {

    private GridView gridView;
    ImageAdapter imageAdapter;
    private static final String TAG_POETS_JSON = "Poets.en.json";
    private String TAG_ACTORS_JSON;

    private String TAG_NAME;
    private String TAG_BIO;
    private String TAG_BIO_EXTENDED;
    private String TAG_NUMBER;

    private String TAG_POEM_NAME;
    private String TAG_POEM_ACTOR_NUMBER;
    private String TAG_POEM_POET_NUMBER;
    private String TAG_POEM_PROLOGUE;
    private String TAG_POEM_NUMBER;
    private String TAG_FIRST_POEM;
    private String TAG_SECOND_POEM;
    private String TAG_THIRD_POEM;
    private String TAG_FIRST_ACTOR;
    private String TAG_SECOND_ACTOR;
    private String TAG_THIRD_ACTOR;

    static ArrayList<ElsPoetesJsonDataModel> modelJsonPoets; //Static so that I can use it in ElsActors!

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "come visit us at: www.corebaseit.com", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //My stuff:
        //new ProgressPoetsPoemsAsyncTask().execute();
        gridView = (GridView) findViewById(R.id.gridView);

        new poemaLeido().execute();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void refreshGridView() {

        DisplayMetrics displaymetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int screenWidth = displaymetrics.widthPixels;
        //int screenHeight = displaymetrics.heightPixels;

        int numColumns = (screenWidth) / (screenWidth/2);

        gridView.setNumColumns(numColumns);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        refreshGridView();
    }

    //Grab the POEM...and place it in Adapter!
    public class poemaLeido extends AsyncTask<String, Void, ArrayList<HashMap<String, String>>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected ArrayList<HashMap<String, String>> doInBackground(String... urls) {

            Type listType = new TypeToken<ArrayList<ElsPoetesJsonDataModel>>() { }.getType();

            modelJsonPoets = new GsonBuilder().create().fromJson(loadJSONPoemFromAsset(TAG_POETS_JSON), listType);

            //Hashmap for ListView
            ArrayList<HashMap<String, String>> songsList = new ArrayList<>();

            for (ElsPoetesJsonDataModel post : modelJsonPoets) {

                String jsonPoemLineNumber = post.getBio();
                String jsonPoemText = post.getBioExtended();
                String jsonPoemStartTime = post.getName();
                String jsonFirstPoem = post.getNameFirstPoem();
                String jsonSecondPoem = post.getNameSecondtPoem();
                String jsonThirdPoem = post.getNameThirdPoem();
                String jsonFirstActor = post.getNameFirstActor();
                String jsonSecondActor = post.getNameSecondActor();
                String jsonThirdActor = post.getNameThirdActor();
                Integer jsonPoemEndTime = post.getNumber();
                String intToStringNumber = Integer.toString(jsonPoemEndTime);

                HashMap<String, String> map = new HashMap<>();

                map.put(TAG_NAME, jsonPoemLineNumber);
                map.put(TAG_BIO, jsonPoemText);
                map.put(TAG_BIO_EXTENDED, jsonPoemStartTime);
                map.put(TAG_FIRST_POEM, jsonFirstPoem);
                map.put(TAG_SECOND_POEM,jsonSecondPoem);
                map.put(TAG_THIRD_POEM,jsonThirdPoem);
                map.put(TAG_FIRST_ACTOR, jsonFirstActor);
                map.put(TAG_SECOND_ACTOR,jsonSecondActor);
                map.put(TAG_THIRD_ACTOR,jsonThirdActor);
                map.put(TAG_NUMBER, intToStringNumber);

                songsList.add(map);

            }
            return (songsList);
        }

        @Override
        protected void onPostExecute(ArrayList<HashMap<String, String>> result) {
            super.onPostExecute(result);

            imageAdapter = new ImageAdapter(MainActivity.this, R.layout.list_layout, modelJsonPoets);
            gridView.setAdapter(imageAdapter);

        }
    }

    public String loadJSONPoemFromAsset(String jsonName) {
        String json = null;
        try {
            InputStream is = getAssets().open(jsonName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public static Drawable getAssetImage(Context context, String filename) throws IOException {
        AssetManager assets = context.getResources().getAssets();
        InputStream buffer = new BufferedInputStream((assets.open(filename)));
        Bitmap bitmap = BitmapFactory.decodeStream(buffer);
        return new BitmapDrawable(context.getResources(), bitmap);
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshGridView();
    }
}