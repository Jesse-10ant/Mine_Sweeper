package com.example.gridlayout;

import android.graphics.Color;
import android.widget.TextView;

public class Block {
    private final int index; //position of the block in the grid
    private boolean isBomb; //is the bomb
    private final TextView text_view; //the view image it is showing
    private final MainActivity main; //reference to main

    public Block(int index, TextView textView, MainActivity main){
        this.index = index;
        this.text_view = textView;
        this.main = main;
        this.isBomb = false;
        text_view.setBackgroundColor(Color.parseColor("#46FA00"));
    }

    //Set cube with bomb
    public void giveBomb(){
        this.isBomb = true;
    }
    public TextView getTextView(){return text_view;}
    //Checker for cube status
    public boolean isBomb(){
        return isBomb;
    }

    //Click handler
    //If the cube is a bomb end game
    //Else tell how many sides of it share with a bomb
    public void onClick(){
        if(isBomb){
            text_view.setText(R.string.mine);
            text_view.setBackgroundColor(Color.RED);
            main.stopTimer();
            main.explodeAllBombs();
        }else{
            int adjacent_bombs = main.countAdjacentBombs(index);
            text_view.setText(String.valueOf(adjacent_bombs));
            text_view.setBackgroundColor(Color.GRAY);
            if(adjacent_bombs == 0){
                text_view.setText("");
                main.revelSafeBlocks(index);
            }

        }
        main.updateRevealCount();
    }
}

