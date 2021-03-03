package com.keke.easymv;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.ConsoleMessage;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

class PlayerView extends WebView {

    public PlayerView(Context context) {
        super(context);
        init();
    }

    public PlayerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PlayerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @SuppressLint("SetJavaScriptEnabled")
    @SuppressWarnings("deprecation")
    public void init() {
        setBackgroundColor(Color.BLACK);
        WebSettings webSettings = getSettings();
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setSupportMultipleWindows(true);
        webSettings.setMediaPlaybackRequiresUserGesture(false);
        webSettings.setAllowFileAccessFromFileURLs(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);
    }

    @Override
    public boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
        return false;
    }

    @Override
    public void scrollTo(int x, int y) {}

    @Override
    public void computeScroll() {}

}

public class PlayerActivity extends AppCompatActivity {

    PlayerView playerView;
    PlayerConfig playerConfig;
    LocalStorageJavaScriptInterface localStorageFixer;
    private AlertDialog mQuitDialog;
    private File saveFile;

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
    public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory, DatabaseErrorHandler errorHandler) {
        return openOrCreateDatabase(name,mode, factory);
    }

    @Override
    public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory) {
        return SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name), null);
    }

    private void createQuitDialog() {
        String appName = playerConfig.title;
        String[] quitLines = getResources().getStringArray(R.array.quit_message);
        StringBuilder quitMessage = new StringBuilder();
        for (int ii = 0; ii < quitLines.length; ii++) {
            quitMessage.append(quitLines[ii].replace("$1", appName));
            if (ii < quitLines.length - 1) {
                quitMessage.append("\n");
            }
        }
        if (quitMessage.length() > 0) {
            mQuitDialog = new AlertDialog.Builder(this)
                    .setPositiveButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton(R.string.quit, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            PlayerActivity.super.onBackPressed();
                        }
                    })
                    .setMessage(quitMessage.toString())
                    .create();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        String indexPage = getIntent().getStringExtra("path");
        assert indexPage != null;
        playerConfig = PlayerConfig.fromFile(new File(new File(indexPage).getParentFile(), "EasyMV.properties"));
        playerConfig.indexPage = indexPage;
        saveFile = new File(new File(indexPage).getParentFile(), "save/EasyMV.save");

        if(playerConfig.BACK_BUTTON_QUITS) {
            createQuitDialog();
        }

        playerView = findViewById(R.id.playerview);
        playerView.setWebChromeClient(new ChromeClient());
        playerView.setWebViewClient(new ViewClient());
        if(playerConfig.FIX_LOCALSTORAGE) {
            localStorageFixer = new LocalStorageJavaScriptInterface();
            playerView.addJavascriptInterface(localStorageFixer, getString(R.string.fix_local_storage_objcet));
        }
        if(playerConfig.BOOTSTRAP_INTERFACE){
            playerView.addJavascriptInterface(new Bootstrapper(), getString(R.string.bootstrap_object));
            playerView.loadData(getRawString(R.raw.bootstrap),"text/html", "");
//            playerView.loadData(getRawString(R.raw.bootstrap), "text/html", "");
        }
        else {
            String uri = buildUri(true, true);
            playerView.loadUrl(uri);
        }
    }

    public String buildUri(boolean webgl, boolean webaudio) {
        String uri = Uri.fromFile(new File(playerConfig.indexPage)).toString();
        if(webgl) {
            uri += "?" + getString(R.string.query_webgl);
        }
        else {
            uri += "?" + getString(R.string.query_canvas);
        }
        if(!webaudio || playerConfig.FORCE_NO_AUDIO) {
            uri += "&" + getString(R.string.query_noaudio);
        }
        if(playerConfig.SHOW_FPS) {
            uri += "&" + getString(R.string.query_showfps);
        }
        Log.d("PlayerActivity", "build uri: " + uri);
        return uri;
    }

    @Override
    public void onBackPressed() {
        if (playerConfig.BACK_BUTTON_QUITS) {
            if (mQuitDialog != null) {
                mQuitDialog.show();
            } else {
                super.onBackPressed();
            }
        } else {
            playerView.evaluateJavascript("TouchInput._onCancel();", null);
        }
    }

    @Override
    protected void onPause() {
        playerView.pauseTimers();
        playerView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        playerView.resumeTimers();
        playerView.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        playerView.destroy();
    }

    private class Bootstrapper {
        @JavascriptInterface
        public void boot(boolean webgl, boolean webaudio) {
            final String uri = buildUri(webgl, webaudio);
            playerView.post(new Runnable() {
                @Override
                public void run() {
                    playerView.removeJavascriptInterface(getString(R.string.bootstrap_object));
                    playerView.loadUrl(uri);
                }
            });
        }
    }

    private class LocalStorageJavaScriptInterface {
        private Properties properties;

        LocalStorageJavaScriptInterface() {
            properties = new Properties();
            load();
        }

        void load() {
            try {
                if(!saveFile.exists()) save();
                properties.load(new FileInputStream(saveFile));
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), R.string.unable_to_load, Toast.LENGTH_SHORT).show();
            }
        }

        void save() {
            try {
                properties.store(new FileOutputStream(saveFile), "");
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), R.string.unable_to_save, Toast.LENGTH_SHORT).show();
            }
        }

        @JavascriptInterface
        public String getItem(String key) {
            return properties.getProperty(key);
        }

        @JavascriptInterface
        public void setItem(String key,String value) {
            properties.setProperty(key, value);
            save();
        }

        @JavascriptInterface
        public void removeItem(String key)
        {
            properties.remove(key);
            save();
        }

        @JavascriptInterface
        public void clear()
        {
            properties.clear();
            save();
        }
    }

    private class ChromeClient extends WebChromeClient {
        @Override
        public boolean onConsoleMessage(ConsoleMessage consoleMessage){
            if ("Scripts may close only the windows that were opened by it.".equals(consoleMessage.message())) {
                finish();
            }
            return super.onConsoleMessage(consoleMessage);
        }

        @Override
        public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
            WebView dumbWV = new WebView(view.getContext());
            dumbWV.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    view.getContext().startActivity(browserIntent);
                }
            });
            ((WebView.WebViewTransport) resultMsg.obj).setWebView(dumbWV);
            resultMsg.sendToTarget();
            return true;
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            if(newProgress == 100) {
                if(playerConfig.FIX_LOCALSTORAGE) {
                    String code = getRawString(R.raw.fix_local_storage);
                    view.evaluateJavascript(code, null);
                }
                if(!playerConfig.FORCE_AUDIO_EXT.equals("")) {
                    String code = getString(R.string.force_audio_ext_js);
                    code = code.replace("$1", "\"" + playerConfig.FORCE_AUDIO_EXT + "\"");
                    view.evaluateJavascript(code, null);
                }
                if(playerConfig.ADD_GAMEPAD) {
                    String code = getRawString(R.raw.gamepad);
                    view.evaluateJavascript(code, null);
                }
            }
        }
    }

    private class ViewClient extends WebViewClient {

        @Override
        @TargetApi(Build.VERSION_CODES.M)
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            view.setBackgroundColor(Color.WHITE);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            view.setBackgroundColor(Color.WHITE);
        }

        @Nullable
        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
            String req = request.getUrl().toString();
            if(playerConfig.MANUALLY_START && req.endsWith("js/main.js")) {
                InputStream is = getResources().openRawResource(R.raw.manually_start);
                return new WebResourceResponse("text/javascript","utf-8",is);
            }
            if(playerConfig.ADD_GAMEPAD && req.endsWith("EasyMV/DirPad.png")) {
                InputStream is = getResources().openRawResource(R.raw.dir_pad);
                return new WebResourceResponse("image/png","",is);
            }
            if(playerConfig.ADD_GAMEPAD && req.endsWith("EasyMV/ActionBtn.png")) {
                InputStream is = getResources().openRawResource(R.raw.action_button);
                return new WebResourceResponse("image/png","",is);
            }
            if(playerConfig.ADD_GAMEPAD && req.endsWith("EasyMV/CancelButton.png")) {
                InputStream is = getResources().openRawResource(R.raw.cancel_button);
                return new WebResourceResponse("image/png","",is);
            }
            return super.shouldInterceptRequest(view, request);
        }
    }

}