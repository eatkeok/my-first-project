package com.lscl.game;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Home extends AppCompatActivity {

    Button btn_stg8, btn_try_slope;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        btn_stg8 = findViewById(R.id.btn_stg8);
        btn_stg8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent stg8 = new Intent(Home.this, game_stg8.class);
                startActivity(stg8);
            }
        });

        btn_try_slope = findViewById(R.id.btn_try_slope);
        btn_try_slope.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent try_slope = new Intent(Home.this, try_slope_function.class);
                startActivity(try_slope);
            }
        });
    }

    @Override
    protected void onPause(){
        super.onPause();
        Toast.makeText(this,"App is paused", Toast.LENGTH_LONG).show();
    }
    @Override
    protected void onResume(){
        super.onResume();
        Toast.makeText(this,"App is resumed", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        Toast.makeText(this,"App is destroyed", Toast.LENGTH_LONG).show();

    }



}
