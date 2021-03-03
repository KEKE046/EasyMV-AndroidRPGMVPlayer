package com.keke.easymv;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.FileObserver;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        String settingFile = getIntent().getStringExtra("settingfile");
        Log.d("SettingsActivity", "settingFile:" + settingFile);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, SettingsFragment.newInstance(settingFile))
                .commit();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public static class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {
        private EditTextPreference editTitle;
        private SwitchPreference switchLocalStorage, switchBootstrap, switchShowFPS;
        private SwitchPreference switchBackButtonQuit, switchForceCanvas, switchForceNoAudio;
        private SwitchPreference switchGamepad, switchManuallyStart;
        private ListPreference listForceAudioExt;
        private File settingFile;
        private PlayerConfig config;

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            switchShowFPS = findPreference("switch_show_fps");
            switchBackButtonQuit = findPreference("switch_back_button_quit");
            switchBootstrap = findPreference("switch_bootstrap");
            switchForceCanvas = findPreference("switch_force_canvas");
            switchForceNoAudio = findPreference("switch_force_no_audio");
            switchLocalStorage = findPreference("switch_local_storage");
            switchGamepad = findPreference("switch_add_gamepad");
            switchManuallyStart = findPreference("switch_mannully_start");
            listForceAudioExt = findPreference("list_force_audio_ext");
            editTitle = findPreference("edit_title");
            getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
            loadSettings();
        }

        void saveSettings() {
            Log.d("SettingActivity", "save");
            if(config == null)
                loadSettings();
            config.SHOW_FPS = switchShowFPS.isChecked();
            config.BACK_BUTTON_QUITS = switchBackButtonQuit.isChecked();
            config.BOOTSTRAP_INTERFACE = switchBootstrap.isChecked();
            config.FORCE_CANVAS = switchForceCanvas.isChecked();
            config.FORCE_NO_AUDIO = switchForceNoAudio.isChecked();
            config.FIX_LOCALSTORAGE = switchLocalStorage.isChecked();
            config.ADD_GAMEPAD = switchGamepad.isChecked();
            config.MANUALLY_START = switchManuallyStart.isChecked();
            config.FORCE_AUDIO_EXT = listForceAudioExt.getValue().equals("null") ? "" : listForceAudioExt.getValue();
            config.title = editTitle.getText();
            if(!config.store(settingFile)) {
                Toast.makeText(getContext(), R.string.unable_to_save, Toast.LENGTH_SHORT).show();
            }
        }

        void loadSettings() {
            Log.d("SettingActivity", "load");
            Bundle argument = getArguments();
            if(argument == null) {
                Toast.makeText(getContext(), R.string.unable_to_load, Toast.LENGTH_SHORT).show();
                return;
            }
            String savefile = argument.getString("settingfile");
            if(savefile == null) {
                Toast.makeText(getContext(), R.string.unable_to_load, Toast.LENGTH_SHORT).show();
                return;
            }
            settingFile = new File(savefile);
            config = PlayerConfig.fromFile(settingFile);
            switchShowFPS.setChecked(config.SHOW_FPS);
            switchBackButtonQuit.setChecked(config.BACK_BUTTON_QUITS);
            switchBootstrap.setChecked(config.BOOTSTRAP_INTERFACE);
            switchForceCanvas.setChecked(config.FORCE_CANVAS);
            switchForceNoAudio.setChecked(config.FORCE_NO_AUDIO);
            switchLocalStorage.setChecked(config.FIX_LOCALSTORAGE);
            switchGamepad.setChecked(config.ADD_GAMEPAD);
            switchManuallyStart.setChecked(config.MANUALLY_START);
            listForceAudioExt.setValue(config.FORCE_AUDIO_EXT.equals("") ? "null" : config.FORCE_AUDIO_EXT);
            editTitle.setText(config.title);
        }

        static SettingsFragment newInstance(String settingFile) {
            SettingsFragment result = new SettingsFragment();
            Bundle bundle = new Bundle();
            bundle.putString("settingfile", settingFile);
            result.setArguments(bundle);
            return result;
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if(isResumed()) {
                saveSettings();
            }
        }
    }
}