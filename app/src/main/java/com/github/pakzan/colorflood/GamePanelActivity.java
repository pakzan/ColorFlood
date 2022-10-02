package com.github.pakzan.colorflood;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;
import java.util.Locale;

public class GamePanelActivity extends AppCompatActivity {
    TextView tvScore;
    TextView tvStatus;
    ImageButton ibReset;
    Button btnColor1;
    Button btnColor2;
    Button btnColor3;
    Button btnColor4;
    Button btnColor5;
    Button btnColor6;
    Button btnColor7;
    Button btnColor8;
    Button btnColor9;
    Button btnColor10;
    LinearLayoutPanel llPanel;
    LinearLayout llColorSelection2;

    public static int turn = 1;
    private static ColorBlock[][] colorPanel;
    static GameState gameState = GameState.getInstance();
    private WinState winState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_panel);

        ibReset = findViewById(R.id.ib_reset);
        tvScore = findViewById(R.id.tv_score);
        tvStatus = findViewById(R.id.tv_status);
        btnColor1 = findViewById(R.id.btn_color1);
        btnColor2 = findViewById(R.id.btn_color2);
        btnColor3 = findViewById(R.id.btn_color3);
        btnColor4 = findViewById(R.id.btn_color4);
        btnColor5 = findViewById(R.id.btn_color5);
        btnColor6 = findViewById(R.id.btn_color6);
        btnColor7 = findViewById(R.id.btn_color7);
        btnColor8 = findViewById(R.id.btn_color8);
        btnColor9 = findViewById(R.id.btn_color9);
        btnColor10 = findViewById(R.id.btn_color10);
        llPanel = findViewById(R.id.ll_panel);
        llColorSelection2 = findViewById(R.id.ll_color_selection_2);

        if (gameState.getColorNum() == ColorNum.SIX) {
            llColorSelection2.setVisibility(View.GONE);
        } else {
            llColorSelection2.setVisibility(View.VISIBLE);
        }

        ibReset.setOnClickListener(view -> reset());

        reset();
    }

    @SuppressLint("NonConstantResourceId")
    public void onColorBtnClick(View view) {
        if (winState.hasGameOver()) return;

        Color curColor = Color.getColor(view.getId());
        updateColor(curColor);
        updateWinState();
        turn += 1;

        if (!winState.hasGameOver() && gameState.getType() == PlayType.WITH_AI) {
            curColor = AI.chooseColor(colorPanel);
            updateColor(curColor);
            updateWinState();
            turn += 1;
        }

        llPanel.invalidate();
    }

    private void updateColor(Color curColor) {
        int rowNum = colorPanel.length;
        int colNum = colorPanel[0].length;

        Owner nextPlayer = null;
        Owner owner = null;
        Color prevColor = null;
        switch (gameState.getType()) {
            case WITH_AI:
                switch (turn % 2) {
                    case 1:
                        owner = Owner.PLAYER_1;
                        nextPlayer = Owner.AI;
                        prevColor = colorPanel[0][0].getColor();
                        break;
                    case 0:
                        owner = Owner.AI;
                        nextPlayer = Owner.PLAYER_1;
                        prevColor = colorPanel[rowNum - 1][colNum - 1].getColor();
                        break;
                }
                break;
            case TWO_PLAYERS:
                switch (turn % 2) {
                    case 1:
                        owner = Owner.PLAYER_1;
                        nextPlayer = Owner.PLAYER_2;
                        prevColor = colorPanel[0][0].getColor();
                        break;
                    case 0:
                        owner = Owner.PLAYER_2;
                        nextPlayer = Owner.PLAYER_1;
                        prevColor = colorPanel[rowNum - 1][colNum - 1].getColor();
                        break;
                }
                break;
            case FOUR_PLAYERS:
                switch (turn % 4) {
                    case 1:
                        owner = Owner.PLAYER_1;
                        nextPlayer = Owner.PLAYER_2;
                        prevColor = colorPanel[0][0].getColor();
                        break;
                    case 2:
                        owner = Owner.PLAYER_2;
                        nextPlayer = Owner.PLAYER_3;
                        prevColor = colorPanel[0][colNum - 1].getColor();
                        break;
                    case 3:
                        owner = Owner.PLAYER_3;
                        nextPlayer = Owner.PLAYER_4;
                        prevColor = colorPanel[rowNum - 1][0].getColor();
                        break;
                    case 0:
                        owner = Owner.PLAYER_4;
                        nextPlayer = Owner.PLAYER_1;
                        prevColor = colorPanel[rowNum - 1][colNum - 1].getColor();
                        break;
                }
                break;
        }
        assert owner != null && prevColor != null;

        tvStatus.setText(String.format("%s's Turn", nextPlayer.getString()));
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

        switch (gameState.getType()) {
            case WITH_AI:
                tvScore.setText(String.format(Locale.ENGLISH,
                        "Player Score: %d\nAI Score: %d",
                        winState.getScore(Owner.PLAYER_1),
                        winState.getScore(Owner.AI)
                ));
                break;
            case TWO_PLAYERS:
                tvScore.setText(String.format(Locale.ENGLISH,
                        "Player 1 Score: %d\nPlayer 2 Score: %d",
                        winState.getScore(Owner.PLAYER_1),
                        winState.getScore(Owner.PLAYER_2)
                ));
                break;
            case FOUR_PLAYERS:
                tvScore.setText(String.format(Locale.ENGLISH,
                        "Player 1 Score: %d\nPlayer 2 Score: %d\nPlayer 3 Score: %d\nPlayer 4 Score: %d",
                        winState.getScore(Owner.PLAYER_1),
                        winState.getScore(Owner.PLAYER_2),
                        winState.getScore(Owner.PLAYER_3),
                        winState.getScore(Owner.PLAYER_4)
                ));
                break;
        }

        if (!winState.hasGameOver()) return;

        List<String> winners = winState.getWinners();
        switch (gameState.getType()) {
            case WITH_AI:
            case TWO_PLAYERS:
                if (winners.size() == 2) {
                    tvStatus.setText("It's a Draw!");
                } else {
                    tvStatus.setText(String.format(
                            "%s win!",
                            String.join(", ", winners)
                    ));
                }
                break;
            case FOUR_PLAYERS:
                if (winners.size() == 4) {
                    tvStatus.setText("It's a Draw!");
                } else {
                    tvStatus.setText(String.format(
                            "%s win!",
                            String.join(", ", winners)
                    ));
                }
                break;
        }
    }

    public void reset() {
        turn = 1;
        int colNum = gameState.getBoxNumX();
        int rowNum = gameState.getBoxNumY();

        colorPanel = new ColorBlock[rowNum][colNum];
        for (int j = 0; j < rowNum; j++) {
            for (int i = 0; i < colNum; i++) {
                colorPanel[j][i] = new ColorBlock();
            }
        }

        // set all players color
        colorPanel[0][0].setColor(Color.Neutral);
        colorPanel[rowNum - 1][colNum - 1].setColor(Color.Neutral);
        if (gameState.getType() == PlayType.FOUR_PLAYERS) {
            colorPanel[rowNum - 1][0].setColor(Color.Neutral);
            colorPanel[0][colNum - 1].setColor(Color.Neutral);
        }

        // set box owner
        colorPanel[0][0].setOwner(Owner.PLAYER_1);
        switch (gameState.getType()) {
            case WITH_AI:
                colorPanel[rowNum - 1][colNum - 1].setOwner(Owner.AI);
                break;
            case TWO_PLAYERS:
                colorPanel[rowNum - 1][colNum - 1].setOwner(Owner.PLAYER_2);
                break;
            case FOUR_PLAYERS:
                colorPanel[0][colNum - 1].setOwner(Owner.PLAYER_2);
                colorPanel[rowNum - 1][0].setOwner(Owner.PLAYER_3);
                colorPanel[rowNum - 1][colNum - 1].setOwner(Owner.PLAYER_4);
                break;
        }
        tvStatus.setText(String.format("%s's Turn", Owner.PLAYER_1.getString()));

        llPanel.setColorPanel(colorPanel);
        llPanel.invalidate();
        updateWinState();

        btnColor1.setAlpha(1);
        btnColor2.setAlpha(1);
        btnColor3.setAlpha(1);
        btnColor4.setAlpha(1);
        btnColor5.setAlpha(1);
        btnColor6.setAlpha(1);
        btnColor7.setAlpha(1);
        btnColor8.setAlpha(1);
        btnColor9.setAlpha(1);
        btnColor10.setAlpha(1);

        btnColor1.setEnabled(true);
        btnColor2.setEnabled(true);
        btnColor3.setEnabled(true);
        btnColor4.setEnabled(true);
        btnColor5.setEnabled(true);
        btnColor6.setEnabled(true);
        btnColor7.setEnabled(true);
        btnColor8.setEnabled(true);
        btnColor9.setEnabled(true);
        btnColor10.setEnabled(true);
    }
}