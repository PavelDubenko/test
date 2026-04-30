package com.example.clickergame;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    
    private int score = 0;
    private int clickPower = 1;
    private int autoClickerCount = 0;
    private int upgradeCost = 10;
    private int autoClickerCost = 50;
    
    private TextView scoreText;
    private TextView clickPowerText;
    private TextView autoClickerText;
    private Button upgradeButton;
    private Button autoClickerButton;
    
    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable autoClickRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        scoreText = findViewById(R.id.scoreText);
        clickPowerText = findViewById(R.id.clickPowerText);
        autoClickerText = findViewById(R.id.autoClickerText);
        upgradeButton = findViewById(R.id.upgradeButton);
        autoClickerButton = findViewById(R.id.autoClickerButton);
        
        updateUI();
        
        // Основной клик
        Button clickButton = findViewById(R.id.clickButton);
        clickButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                score += clickPower;
                updateUI();
            }
        });
        
        // Улучшение клика
        upgradeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (score >= upgradeCost) {
                    score -= upgradeCost;
                    clickPower++;
                    upgradeCost = (int)(upgradeCost * 1.5);
                    updateUI();
                }
            }
        });
        
        // Покупка авто-кликера
        autoClickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (score >= autoClickerCost) {
                    score -= autoClickerCost;
                    autoClickerCount++;
                    autoClickerCost = (int)(autoClickerCost * 1.3);
                    updateUI();
                    startAutoClicker();
                }
            }
        });
        
        startAutoClicker();
    }
    
    private void startAutoClicker() {
        if (autoClickRunnable != null) {
            handler.removeCallbacks(autoClickRunnable);
        }
        
        autoClickRunnable = new Runnable() {
            @Override
            public void run() {
                if (autoClickerCount > 0) {
                    score += autoClickerCount;
                    updateUI();
                }
                handler.postDelayed(this, 1000);
            }
        };
        handler.postDelayed(autoClickRunnable, 1000);
    }
    
    private void updateUI() {
        scoreText.setText("Счёт: " + score);
        clickPowerText.setText("Сила клика: " + clickPower);
        autoClickerText.setText("Авто-кликеры: " + autoClickerCount);
        upgradeButton.setText("Улучшить клик (" + upgradeCost + ")");
        autoClickerButton.setText("Купить авто-кликер (" + autoClickerCost + ")");
        
        upgradeButton.setEnabled(score >= upgradeCost);
        autoClickerButton.setEnabled(score >= autoClickerCost);
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null && autoClickRunnable != null) {
            handler.removeCallbacks(autoClickRunnable);
        }
    }
}
