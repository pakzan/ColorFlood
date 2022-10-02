package com.github.pakzan.colorflood;

import android.content.Intent;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ColorFlood extends AppCompatActivity {
    static GameState gameState = GameState.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_flood);

        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.screenBrightness = gameState.getBrightness() / (float) 255;
        getWindow().setAttributes(lp);

        int[] rainbow = new int[]{
                Color.One.getColorCode(this),
                Color.Two.getColorCode(this),
                Color.Three.getColorCode(this),
                Color.Four.getColorCode(this),
                Color.Five.getColorCode(this),
                Color.Six.getColorCode(this),
                Color.Seven.getColorCode(this),
                Color.Eight.getColorCode(this),
                Color.Nine.getColorCode(this),
                Color.Ten.getColorCode(this),
        };

        GradientDrawable gradientDrawable = new GradientDrawable(
                GradientDrawable.Orientation.TL_BR,
                rainbow);
        gradientDrawable.setAlpha(110);
        findViewById(R.id.ll_screen).setBackground(gradientDrawable);

        findViewById(R.id.btn_play).setOnClickListener(v ->
                startActivity(new Intent(getApplicationContext(), GamePanelActivity.class))
        );
        findViewById(R.id.btn_settings).setOnClickListener(v ->
                startActivity(new Intent(getApplicationContext(), SettingsActivity.class))
        );
        findViewById(R.id.btn_help).setOnClickListener(v ->
                startActivity(new Intent(getApplicationContext(), HelpActivity.class))
        );
    }
}
