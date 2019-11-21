package com.example.remote_ros;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import io.github.controlwear.virtual.joystick.android.JoystickView;

public class MainActivity extends AppCompatActivity {
    private SharedPreferences mPrefs;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPrefs = getPreferences(Context.MODE_PRIVATE);
        setContentView(R.layout.activity_main);
        initPanel();
    }
    
    public void connect(View view) {
        final TextView ip = findViewById(R.id.rosip);
        final TextView inform = findViewById(R.id.inform);
        AsyncTask<Void, Void, Void> asyncTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
    
                if (TwistPublisher.getInstance().init(ip.getText().toString())) {
                    MainActivity.this.runOnUiThread(() -> {
                        inform.setText("success");
                    });
        
                } else {
                    MainActivity.this.runOnUiThread(() -> {
                        inform.setText("failure");
                    });
                }
                return null;
            }
        };
        asyncTask.execute();
        
    }
    
    public void save(View view) {
        SharedPreferences.Editor ed = mPrefs.edit();
        final TextView ip = findViewById(R.id.rosip);
        ed.putString("ip", ip.getText().toString());
        ed.apply();
    }
    
    private void initPanel() {
        final TextView ip = findViewById(R.id.rosip);
        ip.setText(mPrefs.getString("ip", ""));
        final JoystickView joystick = findViewById(R.id.velocityCon);
        joystick.setOnMoveListener((int angle, int strength) -> {
            final TwistPublisher twistPub = TwistPublisher.getInstance();
            twistPub.setVx(Math.cos(Math.toRadians(angle)) * strength * 5);
            twistPub.setVy(Math.sin(Math.toRadians(angle)) * strength * 5);
            twistPub.update();
        }, 100);
        final SeekBar seekBar = findViewById(R.id.angularCon);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                final TwistPublisher twistPub = TwistPublisher.getInstance();
                twistPub.setAngular(2 * Math.PI * progress / 60);
                twistPub.update();
                runOnUiThread(() -> {
                    TextView displayView = findViewById(R.id.angularview);
                    displayView.setText(String.valueOf(progress));
                });
            }
            
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            
            }
            
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            
            }
        });
    }
}
