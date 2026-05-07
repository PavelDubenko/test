package com.example.clickergame;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Main activity for the Clicker Game.
 * Manages game state, user interactions, and UI updates.
 */
public class MainActivity extends AppCompatActivity {
    
    // Upgrade cost multipliers
    private static final double CLICK_UPGRADE_MULTIPLIER = 1.5;
    private static final double AUTO_CLICKER_MULTIPLIER = 1.3;
    private static final long AUTO_CLICK_INTERVAL_MS = 1000L;
    
    // Game state
    private int score = 0;
    private int clickPower = 1;
    private int autoClickerCount = 0;
    private int upgradeCost = 10;
    private int autoClickerCost = 50;
    
    // UI components
    private TextView scoreText;
    private TextView clickPowerText;
    private TextView autoClickerText;
    private Button upgradeButton;
    private Button autoClickerButton;
    
    private final Handler handler = new Handler(Looper.getMainLooper());
    private Runnable autoClickRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        initializeViews();
        setupClickListeners();
        updateUI();
        startAutoClicker();
    }
    
    /**
     * Initialize all view references.
     */
    private void initializeViews() {
        scoreText = findViewById(R.id.scoreText);
        clickPowerText = findViewById(R.id.clickPowerText);
        autoClickerText = findViewById(R.id.autoClickerText);
        upgradeButton = findViewById(R.id.upgradeButton);
        autoClickerButton = findViewById(R.id.autoClickerButton);
    }
    
    /**
     * Set up all button click listeners.
     */
    private void setupClickListeners() {
        // Main click button
        Button clickButton = findViewById(R.id.clickButton);
        clickButton.setOnClickListener(v -> handleMainClick());
        
        // Click upgrade button
        upgradeButton.setOnClickListener(v -> handleUpgradeClick());
        
        // Auto-clicker purchase button
        autoClickerButton.setOnClickListener(v -> handlePurchaseAutoClicker());
    }
    
    /**
     * Handle main click action - adds click power to score.
     */
    private void handleMainClick() {
        score += clickPower;
        updateUI();
    }
    
    /**
     * Handle click power upgrade purchase.
     */
    private void handleUpgradeClick() {
        if (score >= upgradeCost) {
            score -= upgradeCost;
            clickPower++;
            upgradeCost = (int)(upgradeCost * CLICK_UPGRADE_MULTIPLIER);
            updateUI();
        }
    }
    
    /**
     * Handle auto-clicker purchase.
     */
    private void handlePurchaseAutoClicker() {
        if (score >= autoClickerCost) {
            score -= autoClickerCost;
            autoClickerCount++;
            autoClickerCost = (int)(autoClickerCost * AUTO_CLICKER_MULTIPLIER);
            updateUI();
            startAutoClicker();
        }
    }
    
    /**
     * Start or restart the auto-clicker mechanism.
     * Adds score based on number of auto-clickers every second.
     */
    private void startAutoClicker() {
        if (autoClickRunnable != null) {
            handler.removeCallbacks(autoClickRunnable);
        }
        
        autoClickRunnable = () -> {
            if (autoClickerCount > 0) {
                score += autoClickerCount;
                updateUI();
            }
            handler.postDelayed(autoClickRunnable, AUTO_CLICK_INTERVAL_MS);
        };
        handler.postDelayed(autoClickRunnable, AUTO_CLICK_INTERVAL_MS);
    }
    
    /**
     * Update all UI elements with current game state.
     */
    private void updateUI() {
        scoreText.setText(getString(R.string.score_label, score));
        clickPowerText.setText(getString(R.string.click_power_label, clickPower));
        autoClickerText.setText(getString(R.string.auto_clickers_label, autoClickerCount));
        upgradeButton.setText(getString(R.string.upgrade_click_button, upgradeCost));
        autoClickerButton.setText(getString(R.string.buy_auto_clicker_button, autoClickerCost));
        
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
