package com.keke.easymv;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Locale;

public class HelpActivity extends AppCompatActivity {

    public String getRawString(int id) {
        InputStream is = getResources().openRawResource(id);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            while((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (Exception e) {
            return null;
        }
        return sb.toString();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        WebView webView = findViewById(R.id.help_view);
        if(isZh()) {
            webView.loadData(getRawString(R.raw.help_zh), "text/html", "");
        }
        else {
            webView.loadData(getRawString(R.raw.help_en), "text/html", "");
        }
    }

    public boolean isZh() {
        Locale locale = getResources().getConfiguration().locale;
        String language = locale.getLanguage();
        return language.endsWith("zh");
    }
}
