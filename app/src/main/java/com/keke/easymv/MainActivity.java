package com.keke.easymv;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class GameItem {
    public String title;
    public File rootDir;
    public File indexPage;
    public Bitmap icon;
    public File settingFile;

    public GameItem() { }

    public GameItem(String title) {
        this.title = title;
    }

    @Nullable
    public static GameItem fromDir(File path) {
        File iconFile = new File(path ,"icon/icon.png");
        File indexPage = new File(path ,"index.html");
        if(!indexPage.exists())
            return null;
        GameItem gameItem = new GameItem();
        gameItem.rootDir = path;
        gameItem.indexPage = indexPage;
        gameItem.title = path.getName();
        gameItem.settingFile = new File(path, "EasyMV.properties");
        if(gameItem.settingFile.exists())
            gameItem.title = PlayerConfig.fromFile(gameItem.settingFile).title;
        if(iconFile.exists())
            gameItem.icon = BitmapFactory.decodeFile(iconFile.getPath());
        else
            gameItem.icon = null;
        return gameItem;
    }
}

class GameItemAdapter extends ArrayAdapter<GameItem> {
    private int viewId = 0;
    MainActivity activity;
    public GameItemAdapter(MainActivity context, int textViewResourceId, List<GameItem> objects){
        super(context, textViewResourceId, objects);
        activity = context;
        viewId = textViewResourceId;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        GameItem gameItem = getItem(position);
        assert gameItem != null;
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(viewId, parent, false);
        }
        TextView textView = convertView.findViewById(R.id.game_item_title);
        textView.setText(gameItem.title);
        ImageView imageView = convertView.findViewById(R.id.game_item_icon);
        imageView.setImageBitmap(gameItem.icon);
        ImageButton imageButton = convertView.findViewById(R.id.button_setting);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.settingGame(position);
            }
        });
        return convertView;
    }
}

public class MainActivity extends AppCompatActivity {
    private ArrayList<GameItem> gameList = new ArrayList<>();
    private GameItemAdapter gameItemAdapter;
    private ArrayList<String> searchDir = new ArrayList<>();
    public final int ACTION_PICK_PATH = 1234;
    TinyDB tinyDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        verifyPermission(this);
        tinyDB = new TinyDB(this);
        searchDir = tinyDB.getListString("searchDir");
        if(searchDir == null) {
            searchDir = new ArrayList<>();
        }
        gameItemAdapter = new GameItemAdapter(MainActivity.this, R.layout.game_item, gameList);
        ListView listView = findViewById(R.id.game_list);
        listView.setAdapter(gameItemAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startGame(position);
            }
        });
        refresh();
    }

    public void startGame(int position) {
        GameItem game = gameList.get(position);
        Intent intent = new Intent(this, PlayerActivity.class);
        intent.putExtra("path", game.indexPage.toString());
        startActivity(intent);
    }

    public void settingGame(int position) {
        GameItem game = gameList.get(position);
        Intent intent = new Intent(this, SettingsActivity.class);
        intent.putExtra("settingfile", game.settingFile.toString());
        startActivity(intent);
        refresh();
    }

    static public void verifyPermission(Activity activity) {
        int REQUEST_EXTERNAL_STORAGE = 1;
        String[] PERMISSIONS_STORAGE = {
                "android.permission.READ_EXTERNAL_STORAGE",
                "android.permission.WRITE_EXTERNAL_STORAGE" };
        try {
            int permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity,
                        PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void refresh() {
        gameList.clear();
        for(String dirString: searchDir) {
            File dir = new File(dirString);
            String [] subdir = dir.list();
            if(subdir == null) continue;
            for(String title: subdir) {
                GameItem gameItem = GameItem.fromDir(new File(dir, title));
                if(gameItem != null) {
                    gameList.add(gameItem);
                }
            }
        }
        gameItemAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_game_list, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case ACTION_PICK_PATH:
                if(resultCode == RESULT_OK) {
                    searchDir = data.getStringArrayListExtra("paths");
                    tinyDB.putListString("searchDir", searchDir);
                    refresh();
                }
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_refresh:
                refresh();
                return true;
            case R.id.menu_settings:
                Intent intent = new Intent(this, PathPicker.class);
                intent.putStringArrayListExtra("paths", searchDir);
                startActivityForResult(intent, ACTION_PICK_PATH);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
