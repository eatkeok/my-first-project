package com.lscl.game;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.widget.Button;
import android.widget.Toast;

public class try_slope_function extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Sensor magnetometer;
    private TextView slopeTextView;
    private ImageView imgHole; // ImageView to rotate inversely based on roll

    private float[] gravity;
    private float[] geomagnetic;

    private float currentPitch = 0; // Stores the latest pitch value
    private float currentRoll = 0; // Stores the latest roll value
    private int touchCount = 0; // Counter for touch events

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_try_slope_function);

        // Handle window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Find the TextView and ImageView
        slopeTextView = findViewById(R.id.slopeTextView);
        imgHole = findViewById(R.id.img_hole2); // ImageView to animate

        // Add an OnTouchListener to detect touches
        View mainView = findViewById(R.id.main);
        mainView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    // Increment touch count only if both conditions are satisfied
                    if (currentRoll >= -40 && currentRoll <= -30 && currentPitch >= -70 && currentPitch <= -40) {
                        touchCount++;
                        if (touchCount == 3) { // Trigger after 3 valid touches
                            showSuccessDialog(); // Call method to display the pop-up window
                            touchCount = 0; // Reset touch count
                        }
                    }
                }
                return true; // Consume the touch event
            }
        });

        // Initialize sensors
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        // Register sensor listeners
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
        sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            gravity = event.values;
        } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            geomagnetic = event.values;
        }

        if (gravity != null && geomagnetic != null) {
            float[] R = new float[9];
            float[] I = new float[9];
            if (SensorManager.getRotationMatrix(R, I, gravity, geomagnetic)) {
                float[] orientation = new float[3];
                SensorManager.getOrientation(R, orientation);

                // Convert radians to degrees
                currentPitch = (float) Math.toDegrees(orientation[1]); // Tilt forward/backward
                currentRoll = (float) Math.toDegrees(orientation[2]);  // Tilt left/right

                // Update TextView only if Pitch is within -40° to -70°
                if (currentPitch >= -70 && currentPitch <= -40) {
                    slopeTextView.setText("Pitch: " + currentPitch + "°\nRoll: " + currentRoll + "°\nTouch Count: " + touchCount);

                    // Rotate ImageView inversely to the Roll value
                    imgHole.setRotation(-currentRoll); // Negate the Roll for inverse rotation
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Not used in this example
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Unregister the sensors to avoid memory leaks
        sensorManager.unregisterListener(this);
    }

    private void showSuccessDialog() {
        // Inflate the custom layout for the dialog
        View view = LayoutInflater.from(this).inflate(R.layout.success_popup, null);
        Button successDone = view.findViewById(R.id.successDone);

        // Build and show the AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);

        final AlertDialog alertDialog = builder.create();

        successDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                Toast.makeText(try_slope_function.this, "Back To the Menu", Toast.LENGTH_SHORT).show();
                Intent home = new Intent(try_slope_function.this, Home.class);
                startActivity(home);
            }
        });

        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
    }
}