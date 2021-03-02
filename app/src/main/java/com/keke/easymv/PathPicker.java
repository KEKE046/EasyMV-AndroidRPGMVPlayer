package com.keke.easymv;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
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

import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PathPicker extends AppCompatActivity {

    public static final int ACTION_SELECT_DIRECTORY = 2333;

    private ArrayList<String> pathList = new ArrayList<>();
    PathItemAdapter pathListAdapter;

    class PathItemAdapter extends ArrayAdapter<String> {
        private int viewId = 0;
        public PathItemAdapter(Context context, int textViewResourceId, List<String> objects){
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
        switch(requestCode) {
            case ACTION_SELECT_DIRECTORY:
                if(resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    String path = FileUtil.getFullPathFromTreeUri(uri, this);
                    pathList.add(path);
                    pathListAdapter.notifyDataSetChanged();
                }
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.path_add) {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            startActivityForResult(Intent.createChooser(intent, getString(R.string.select_folder)), ACTION_SELECT_DIRECTORY);
        }
        if(item.getItemId() == R.id.menu_ok) {
            Intent intent = new Intent();
            intent.putStringArrayListExtra("paths", pathList);
            setResult(RESULT_OK, intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
