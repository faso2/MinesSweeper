package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    TableLayout table;
    ToggleButton toggleButton;
    static BlockButton[][] buttons;
    public static int ROWS = 9;
    public static int COLS = 9;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BlockButton.mainActivity = this;
        buttons = new BlockButton[9][9];

        toggleButton = findViewById(R.id.toggleButton);
        toggleButton.setText("BREAK");
        toggleButton.setTextOff("BREAK");
        toggleButton.setTextOn("FLAG");

        initializeButtons();
        placeMines(buttons);
        calculateNeighborMines(buttons);
    }

    private void initializeButtons() {
        for (int i = 0; i < 9; i++) {
            table = findViewById(R.id.tableLayout);
            TableRow tableRow = new TableRow(this);
            table.addView(tableRow);

            for (int j = 0; j < 9; j++) {
                buttons[i][j] = new BlockButton(this, i, j);
                buttons[i][j].initialValue();
                BlockButton.blocks++;

                TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(
                        TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.WRAP_CONTENT,
                        1.0f);

                buttons[i][j].setLayoutParams(layoutParams);
                buttons[i][j].setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        BlockButton b = (BlockButton) view;
                        if ("BREAK".equals(toggleButton.getText().toString())) {
                            if (b.breakBlock(b) == true) {
                                TextView gameEnd = (TextView) findViewById(R.id.gameEnd);
                                gameEnd.setText("GAME OVER!");
                                disableAllBlocks();
                            }
                        } else if ("FLAG".equals(toggleButton.getText().toString())) {
                            b.toggleFlag(b);
                            TextView textView = findViewById(R.id.MinesTextView);
                            textView.setText(String.valueOf(BlockButton.mines));
                        }

                        if (BlockButton.blocks == 0 && BlockButton.flags == 10) {
                            TextView gameEnd = (TextView) findViewById(R.id.gameEnd);
                            gameEnd.setText("WIN!");
                            disableAllBlocks();
                        }
                    }
                });
                tableRow.addView(buttons[i][j]);
            }
        }
    }

    private void placeMines(BlockButton[][] buttons) {
        Random random = new Random();
        int mineCount = 0;

        while (mineCount < BlockButton.mines) {
            int randomX = random.nextInt(ROWS);
            int randomY = random.nextInt(COLS);

            BlockButton currentButton = buttons[randomX][randomY];
            if (!currentButton.mine) {
                currentButton.mine = true;
                mineCount++;
            }
        }
    }

    private void calculateNeighborMines(BlockButton[][] buttons) {
        for (int i=0; i<9; i++) {
            for (int j=0; j<9; j++) {
                BlockButton currentButton = buttons[i][j];

                int neighborMines = 0;

                for (int row = -1; row<=1; row++) {
                    for (int col = -1; col <= 1; col++) {
                        if (row != 0 || col != 0) {
                            int checkX = i + row;
                            int checkY = j + col;

                            if (checkX >= 0 && checkX < ROWS && checkY >= 0 && checkY < COLS) {
                                if (buttons[checkX][checkY].mine) {
                                  neighborMines++;
                                }
                            }
                        }
                    }
                }
                currentButton.neighborMines = neighborMines;
            }
        }
    }

    public void openSurroundBlocks(BlockButton blockButton) {
        int x = blockButton.x;
        int y = blockButton.y;

        for (int row = -1; row <= 1; row++) {
            for (int col = -1; col <= 1; col++) {
                int checkX = x + row;
                int checkY = y + col;

                if (checkX >= 0 && checkX < ROWS && checkY >= 0 && checkY < COLS) {
                    BlockButton neighborButton = buttons[checkX][checkY];

                    if (neighborButton.isClickable() && !neighborButton.flag) {
                        boolean isMine = neighborButton.breakBlock(neighborButton);
                        if (!isMine && neighborButton.neighborMines == 0) {
                            openSurroundBlocks(neighborButton);
                        }
                    }
                }
            }
        }
    }

    private void disableAllBlocks() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                buttons[i][j].setClickable(false);
            }
        }
    }
}