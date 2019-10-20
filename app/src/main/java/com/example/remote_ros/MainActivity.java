package com.example.remote_ros;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    
    public void connect(View view) {
        final TextView ip = findViewById(R.id.rosip);
        final TextView inform = findViewById(R.id.inform);
        if (TwistPublisher.getInstance().init(ip.getText().toString())) {
            inform.setText("success");
        } else {
            inform.setText("failure");
        }
    }
}
