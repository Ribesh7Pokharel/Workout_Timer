package com.example.workouttimer;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private EditText editTextWorkoutTime, editTextRestTime;
    private TextView textViewTimer;
    private ProgressBar progressBar;
    private Button buttonStartStop;
    private CountDownTimer countDownTimer;
    private boolean isTimerRunning = false;
    private boolean isWorkoutPhase = true;
    private long timeLeftInMillis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextWorkoutTime = findViewById(R.id.editTextWorkoutTime);
        editTextRestTime = findViewById(R.id.editTextRestTime);
        textViewTimer = findViewById(R.id.textViewTimer);
        progressBar = findViewById(R.id.progressBar);
        buttonStartStop = findViewById(R.id.buttonStartStop);

        buttonStartStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isTimerRunning) {
                    startTimer();
                } else {
                    pauseTimer();
                }
            }
        });

    }

    private void startTimer() {
        if (timeLeftInMillis == 0) {  // Timer is starting for the first time or after completion
            if (isWorkoutPhase) {
                long workoutTime = Long.parseLong(editTextWorkoutTime.getText().toString()) * 1000;
                timeLeftInMillis = workoutTime;
                progressBar.setMax((int) workoutTime);
            } else {
                long restTime = Long.parseLong(editTextRestTime.getText().toString()) * 1000;
                timeLeftInMillis = restTime;
                progressBar.setMax((int) restTime);
            }
        }

        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateTimer();
            }

            @Override
            public void onFinish() {
                isTimerRunning = false;
                isWorkoutPhase = !isWorkoutPhase;  // Toggle phase
                timeLeftInMillis = 0;  // Reset timeLeftInMillis for the next phase
                updateButtons();
                if (!isWorkoutPhase) {
                    startTimer();  // Start the rest phase automatically
                }
            }
        }.start();

        isTimerRunning = true;
        updateButtons();
    }


    private void pauseTimer() {
        countDownTimer.cancel();
        isTimerRunning = false;
        updateButtons();
    }

    private void updateTimer() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;
        String timeLeftFormatted = String.format("%02d:%02d", minutes, seconds);
        textViewTimer.setText(timeLeftFormatted);
        progressBar.setProgress((int) (progressBar.getMax() - timeLeftInMillis));
    }

    private void updateButtons() {
        if (isTimerRunning) {
            buttonStartStop.setText("Pause");
        } else {
            buttonStartStop.setText("Start");
        }
    }
}