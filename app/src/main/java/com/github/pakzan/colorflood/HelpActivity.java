package com.github.pakzan.colorflood;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

public class HelpActivity extends Activity {

    TextView tvInstruction;
    Button btnColor1;
    Button btnColor2;
    Button btnColor3;
    Button btnColor4;
    Button btnColor5;
    Button btnColor6;
    LinearLayout llScreen;
    LinearLayoutPanel llPanel;
    FrameLayout llColorSelection;
    LinearLayout llColorSelectionStroke;

    public int step = 1;
    public int turn = 1;
    private WinState winState;
    private final ColorBlock[][] colorPanel = new ColorBlock[3][3];
    static GameState gameState = GameState.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.screenBrightness = gameState.getBrightness() / (float) 255;
        getWindow().setAttributes(lp);

        tvInstruction = findViewById(R.id.tv_instruction);
        btnColor1 = findViewById(R.id.btn_color1);
        btnColor2 = findViewById(R.id.btn_color2);
        btnColor3 = findViewById(R.id.btn_color3);
        btnColor4 = findViewById(R.id.btn_color4);
        btnColor5 = findViewById(R.id.btn_color5);
        btnColor6 = findViewById(R.id.btn_color6);
        llScreen = findViewById(R.id.ll_screen);
        llPanel = findViewById(R.id.ll_panel);
        llColorSelection = findViewById(R.id.ll_color_selection);
        llColorSelectionStroke = findViewById(R.id.ll_color_selection_stroke);

        tvInstruction.setText("The purpose of ColorFlood is to flood all colors by changing color.");
        llScreen.setOnClickListener(view -> {
            switch (step) {
                case 1:
                    step += 1;
                    tvInstruction.setText("Player starts at the top left corner which is represented by the grey block.");
                    llPanel.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    step += 1;
                    tvInstruction.setText("Tap on the color list below to change your color.");
                    llColorSelection.setVisibility(View.VISIBLE);
                    blinkView(llColorSelectionStroke, 500);
                    break;
                case 6:
                    finish();
                    break;
            }
        });

        llPanel.setVisibility(View.GONE);
        llColorSelection.setVisibility(View.GONE);
        reset();
    }

    @SuppressLint("NonConstantResourceId")
    public void onColorBtnClick(View view) {
        if (winState.hasGameOver()) return;
        switch (step) {
            case 3:
                step += 1;
                llColorSelectionStroke.clearAnimation();
                llColorSelectionStroke.setVisibility(View.GONE);
                tvInstruction.setText("Player cannot choose the colors same with player or other opponent/s.");
                break;
            case 4:
                step += 1;
                tvInstruction.setText("Player will earn score for each color flooded. \nTry to flood all the color!");
                break;
            case 5:
                break;
            default:
                return;
        }

        Color curColor = Color.getColor(view.getId());
        updateColor(curColor);
        updateWinState();
        turn += 1;

        llPanel.invalidate();
    }

    private void updateColor(Color curColor) {
        Owner owner = Owner.PLAYER_1;
        Color prevColor = colorPanel[0][0].getColor();

        if (prevColor.getViewId() != -1) {
            Button btnPrevColor = findViewById(prevColor.getViewId());
            btnPrevColor.setEnabled(true);
            btnPrevColor.setAlpha(1);
        }
        Button btnCurColor = findViewById(curColor.getViewId());
        btnCurColor.setEnabled(false);
        btnCurColor.setAlpha(0.2f);

        ColorUtils.spreadColor(colorPanel, owner, curColor);
    }

    private void updateWinState() {
        winState = new WinState(colorPanel);
        if (step == 5 && winState.hasGameOver()) {
            tvInstruction.setText("Congratulation! You have flooded all the color! \nTap again to return to main menu.");
            step += 1;
        }
    }

    public void reset() {
        turn = 1;
        for (int j = 0; j < colorPanel.length; j++) {
            for (int i = 0; i < colorPanel[j].length; i++) {
                colorPanel[j][i] = new ColorBlock();
            }
        }

        colorPanel[0][0].setColor(Color.Neutral);
        colorPanel[0][0].setOwner(Owner.PLAYER_1);

        llPanel.setColorPanel(colorPanel);
        llPanel.invalidate();
        updateWinState();
    }

    public static void blinkView(View view, int duration) {
        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(duration);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        view.startAnimation(anim);
    }

}
