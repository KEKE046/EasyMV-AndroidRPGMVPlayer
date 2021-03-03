package com.keke.easymv;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class PathPicker extends AppCompatActivity {

    public static final int ACTION_SELECT_DIRECTORY = 2333;

    private ArrayList<String> pathList = new ArrayList<>();
    PathItemAdapter pathListAdapter;

    class PathItemAdapter extends ArrayAdapter<String> {
        private int viewId;
        PathItemAdapter(Context context, int textViewResourceId, List<String> objects){
            super(context, textViewResourceId, objects);
            viewId = textViewResourceId;
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if(convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(viewId, parent, false);
            }
            TextView textView = convertView.findViewById(R.id.text_path);
            textView.setText(getItem(position));
            ImageButton button = convertView.findViewById(R.id.button_delete);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pathList.remove(position);
                    notifyDataSetChanged();
                }
            });
            return convertView;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_path_picker);

        pathList = getIntent().getStringArrayListExtra("paths");
        Log.d("PathPicker", "" + (pathList == null));

        pathListAdapter = new PathItemAdapter(PathPicker.this, R.layout.path_item, pathList);
        ListView listView = findViewById(R.id.path_list);
        listView.setAdapter(pathListAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_path_list, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ACTION_SELECT_DIRECTORY){
            if(resultCode == RESULT_OK && data != null) {
                Uri uri = data.getData();
                String path = FileUtil.getFullPathFromTreeUri(uri, this);
                pathList.add(path);
                pathListAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.path_add:
                intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                startActivityForResult(Intent.createChooser(intent, getString(R.string.select_folder)), ACTION_SELECT_DIRECTORY);
                break;
            case R.id.menu_ok:
                intent = new Intent();
                intent.putStringArrayListExtra("paths", pathList);
                setResult(RESULT_OK, intent);
                finish();
                break;
            case R.id.menu_help:
                intent = new Intent(this, HelpActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
