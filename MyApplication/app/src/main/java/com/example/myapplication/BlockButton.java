package com.example.myapplication;

import android.content.Context;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class BlockButton extends Button {

    int x, y;
    boolean mine; //지뢰 o,x
    boolean flag; //깃발 o,x
    int neighborMines; //블록 주변의 지뢰 수
    static int flags = 0; // 깃발이 꽂힌 블록 수
    static int blocks = 0; //남은 블록 수
    static int mines = 10; //총 지뢰 수
    static MainActivity mainActivity;

    public BlockButton(Context context, int x, int y) {
        super(context);
        this.x = x;
        this.y = y;
    }

    public void initialValue() {
        mine = false;
        flag = false;
        neighborMines = 0;
    }

    public void toggleFlag(BlockButton blockButton) {
        if (!flag) {
            this.flag = true;
            flags++;
            mines--;
            blocks--;
            blockButton.setText("+");
        } else if (flag) {
            this.flag = false;
            flags--;
            mines++;
            blocks++;
            blockButton.setText("");
        }
        System.out.println(blocks);
    }

    public boolean breakBlock(BlockButton blockButton) {
        blockButton.setClickable(false);
        blocks--;

        if (mine) {
            blockButton.setText("\uD83E\uDDE8");
            return true;
        } else {
            blockButton.setText(String.valueOf(neighborMines));
            if (neighborMines == 0) {
                mainActivity.openSurroundBlocks(blockButton);
            }
        }

        return false;
    }
}
