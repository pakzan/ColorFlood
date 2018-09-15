package com.example.dell.colorflood1;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.zip.Inflater;


public class ColorFlood extends ActionBarActivity {

    public static int width;
    public static int height;
    public static boolean viewChanged = false;
    private GamePanel gamePanel;
    public static String type = "Player vs AI";
    public static String mode = "Easy";
    public static int colorNum = 6;
    public static int boxNumX = 10;
    public static int boxNumY = 10;
    public static int brightnessValue = 60;
    public static int tempBrightnessValue = 60;
    RadioButton player;
    RadioButton twoPlayer;
    RadioButton fourPlayer;
    RadioButton easy;
    RadioButton medium;
    RadioButton hard;
    RadioButton six;
    RadioButton ten;
    SeekBar brightness;
    EditText BoxNumX;
    EditText BoxNumY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_flood);
        width = getWindowManager().getDefaultDisplay().getWidth();
        height = getWindowManager().getDefaultDisplay().getHeight();
        gamePanel = new GamePanel(getApplicationContext(), width, height);

        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.screenBrightness = brightnessValue / (float)255;
        getWindow().setAttributes(lp);

        Play();
        Option();
        Help();
    }

    public void Play(){

        Button play = (Button)findViewById(R.id.button);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
                setContentView(gamePanel);
            }
        });
    }

    public void Option(){
        Button option = (Button)findViewById(R.id.button2);
        option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            setContentView(R.layout.option);

                BoxNumX = (EditText) findViewById(R.id.BoxNumX);
                BoxNumY = (EditText) findViewById(R.id.BoxNumY);
                player = (RadioButton)findViewById(R.id.player);
                twoPlayer = (RadioButton)findViewById(R.id.twoPlayer);
                fourPlayer = (RadioButton)findViewById(R.id.fourPlayer);
                easy = (RadioButton)findViewById(R.id.easy);
                medium = (RadioButton)findViewById(R.id.medium);
                hard = (RadioButton)findViewById(R.id.hard);
                six = (RadioButton)findViewById(R.id.six);
                ten = (RadioButton)findViewById(R.id.ten);
                brightness = (SeekBar)findViewById(R.id.brightness);

                switch (type){
                    case "Player vs AI":
                        player.setChecked(true);
                        break;
                    case "2 Players":
                        twoPlayer.setChecked(true);
                        easy.setEnabled(false);
                        medium.setEnabled(false);
                        hard.setEnabled(false);
                        break;
                    case "4 Players":
                        fourPlayer.setChecked(true);
                        ten.setChecked(true);
                        easy.setEnabled(false);
                        medium.setEnabled(false);
                        hard.setEnabled(false);
                        six.setEnabled(false);
                        ten.setEnabled(false);
                        break;
                }
                switch (mode){
                    case "Easy":
                        easy.setChecked(true);
                        break;
                    case "Medium":
                        medium.setChecked(true);
                        break;
                    case "Hard":
                        hard.setChecked(true);
                        break;
                }
                switch (colorNum){
                    case 6:
                        six.setChecked(true);
                        break;
                    case 10:
                        ten.setChecked(true);
                        break;
                }
                BoxNumX.setText(Integer.toString(boxNumX));
                BoxNumY.setText(Integer.toString(boxNumY));

                BoxNumX.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (!hasFocus)
                            BoxNumY.setText(BoxNumX.getText());
                    }
                });

                player.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked){
                            easy.setEnabled(true);
                            medium.setEnabled(true);
                            hard.setEnabled(true);
                        }
                        else{
                            easy.setEnabled(false);
                            medium.setEnabled(false);
                            hard.setEnabled(false);
                        }
                    }
                });

                fourPlayer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked){
                            ten.setChecked(true);
                            six.setEnabled(false);
                            ten.setEnabled(false);
                        }
                        else{
                            six.setEnabled(true);
                            ten.setEnabled(true);
                        }
                    }
                });

                brightness.setProgress(brightnessValue);
                brightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        tempBrightnessValue = progress;

                        WindowManager.LayoutParams lp = getWindow().getAttributes();
                        lp.screenBrightness = tempBrightnessValue / (float)255;
                        getWindow().setAttributes(lp);
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });

            Button ok = (Button)findViewById(R.id.ok);
            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int X, Y;
                    if (!BoxNumX.getText().toString().equals("") && !BoxNumY.getText().toString().equals("")) {

                        X = Integer.parseInt(BoxNumX.getText().toString());
                        Y = Integer.parseInt(BoxNumY.getText().toString());
                        if (X < 3 || X > 50 || Y < 3 || Y > 50) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(ColorFlood.this);
                            builder.setCancelable(false)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    })
                                    .setMessage("No. of boxes should be within 3 x 3 and 50 x 50")
                                    .setTitle("Error")
                                    .show();
                        }
                        else {
                            RadioGroup rg = (RadioGroup)findViewById(R.id.playType);
                            int choose = rg.getCheckedRadioButtonId();
                            RadioButton button = (RadioButton)findViewById(choose);
                            type = button.getText().toString();

                            rg = (RadioGroup)findViewById(R.id.mode);
                            choose = rg.getCheckedRadioButtonId();
                            button = (RadioButton)findViewById(choose);
                            mode = button.getText().toString();

                            rg = (RadioGroup)findViewById(R.id.numColor);
                            choose = rg.getCheckedRadioButtonId();
                            button = (RadioButton)findViewById(choose);
                            colorNum = Integer.parseInt(button.getText().toString());

                            boxNumX = Integer.parseInt(BoxNumX.getText().toString());
                            boxNumY = Integer.parseInt(BoxNumY.getText().toString());

                            brightnessValue = tempBrightnessValue;

                            GamePanel.reset();
                            onBackPressed();
                        }
                    }
                    else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ColorFlood.this);
                        builder.setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                })
                                .setMessage("Please input no. of boxes!")
                                .setTitle("Error")
                                .show();
                    }
                }
            });

            Button cancel = (Button)findViewById(R.id.cancel);
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
            viewChanged = true;
            }
        });


    }

    public void Help(){
        Button help = (Button)findViewById(R.id.help);
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Help.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {

        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.screenBrightness = brightnessValue / (float)255;
        getWindow().setAttributes(lp);

        if(viewChanged){
            setContentView(R.layout.activity_color_flood);
            Play();
            Option();
            Help();
            viewChanged = false;
        }
        else
            finish();

        /*Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);*/

    }

    @Override
    public void onDestroy()
    {
        android.os.Process.killProcess(android.os.Process.myPid());
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_color_flood, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
