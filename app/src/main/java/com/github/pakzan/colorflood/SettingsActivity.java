package com.github.pakzan.colorflood;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class SettingsActivity extends AppCompatActivity {
    RadioGroup rgPlayType;
    RadioGroup rgPlayMode;
    RadioGroup rgColorNum;

    RadioButton rbTenColors;
    SeekBar sbBrightness;
    EditText etBoxNumX;
    EditText etBoxNumY;
    GameState gameState = GameState.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        etBoxNumX = findViewById(R.id.et_box_num_x);
        etBoxNumY = findViewById(R.id.et_box_num_y);

        rgPlayType = findViewById(R.id.rg_play_type);
        rgPlayMode = findViewById(R.id.rg_play_mode);
        rgColorNum = findViewById(R.id.rg_color_num);

        rbTenColors = findViewById(R.id.rb_ten_colors);
        sbBrightness = findViewById(R.id.sb_brightness);

        rgPlayType.setOnCheckedChangeListener((radioGroup, i) -> {
            switch (Objects.requireNonNull(PlayType.getPlayType(i))) {
                case WITH_AI:
                    setRadioGroupEnabled(rgPlayMode, true);
                    setRadioGroupEnabled(rgColorNum, true);
                    break;
                case TWO_PLAYERS:
                    setRadioGroupEnabled(rgPlayMode, false);
                    setRadioGroupEnabled(rgColorNum, true);
                    break;
                case FOUR_PLAYERS:
                    setRadioGroupEnabled(rgPlayMode, false);
                    setRadioGroupEnabled(rgColorNum, false);
                    // four player must have 10 colors
                    rbTenColors.setChecked(true);
                    break;
            }
        });

        etBoxNumX.setOnFocusChangeListener((v1, hasFocus) -> {
            if (!hasFocus) etBoxNumY.setText(etBoxNumX.getText());
        });

        sbBrightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.screenBrightness = progress / (float) 255;
                getWindow().setAttributes(lp);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        findViewById(R.id.btn_ok).setOnClickListener(v12 -> {
            if (TextUtils.isEmpty(etBoxNumX.getText().toString()) ||
                    TextUtils.isEmpty(etBoxNumY.getText().toString())) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setCancelable(false)
                        .setPositiveButton("OK", (dialog, which) -> dialog.cancel())
                        .setMessage("Please input no. of boxes!")
                        .setTitle("Error")
                        .show();
                return;
            }

            int numX = Integer.parseInt(etBoxNumX.getText().toString());
            int numY = Integer.parseInt(etBoxNumY.getText().toString());
            if (numX < 3 || numX > 30 || numY < 3 || numY > 30) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setCancelable(false)
                        .setPositiveButton("OK", (dialog, which) -> dialog.cancel())
                        .setMessage("No. of boxes should be within 3 x 3 and 30 x 30")
                        .setTitle("Error")
                        .show();
                return;
            }

            gameState.setType(PlayType.getPlayType(rgPlayType.getCheckedRadioButtonId()));
            gameState.setMode(PlayMode.getPlayMode(rgPlayMode.getCheckedRadioButtonId()));
            gameState.setColorNum(ColorNum.getColorNum(rgColorNum.getCheckedRadioButtonId()));
            gameState.setBoxNumX(numX);
            gameState.setBoxNumY(numY);
            gameState.setBrightness(sbBrightness.getProgress());

            finish();
        });

        findViewById(R.id.btn_cancel).setOnClickListener(v13 -> finish());

        rgPlayType.check(gameState.getType().getViewId());
        rgPlayMode.check(gameState.getMode().getViewId());
        rgColorNum.check(gameState.getColorNum().getViewId());

        etBoxNumX.setText(String.valueOf(gameState.getBoxNumX()));
        etBoxNumY.setText(String.valueOf(gameState.getBoxNumY()));

        sbBrightness.setProgress(gameState.getBrightness());
    }

    private void setRadioGroupEnabled(RadioGroup rg, boolean enabled) {
        for (int id = 0; id < rg.getChildCount(); id++) {
            rg.getChildAt(id).setEnabled(enabled);
        }
    }
}