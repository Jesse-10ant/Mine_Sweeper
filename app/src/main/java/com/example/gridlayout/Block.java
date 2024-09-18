package com.example.gridlayout;

import android.content.Context;
import android.graphics.Color;
import android.widget.TextView;

import org.w3c.dom.Text;

public class Block {
    private int indx; //position of the block in the grid
    private boolean isBomb; //is the bomb
    private TextView textView; //the view image it is showing
    private Context context; //Connecter to the main
    private MainActivity main; //reference to main

    public Block(int indx, TextView textView, Context context, MainActivity main){
        this.indx = indx;
        this.textView = textView;
        this.context = context;
        this.main = main;
        this.isBomb = false;
    }

    //Set cube with bomb
    public void giveBomb(){
        this.isBomb = true;
    }

    //Checker for cube status
    public boolean isBomb(){
        return isBomb;
    }

    //Clck handler
    //If the cube is a bomb end game
    //Else tell how many sides of it share with a bomb
    public void onClick(){
        if(isBomb){
            textView.setText(getString(R.string.bomb));
            textView.setBackgroundColor(Color.red);
            main.stopTimer();
            main.results();
        }else{
            int adjacent_bombs = main.countAjacentBombs(indx);
            textView.setText(String.valueOf(adjacent_bombs));
            textView.setBackgroundColor(Color.GRAY);
            textView.setEnabled(false);
            if(adjacent_bombs == 0){
                main.revelSafeBlocks(indx);
            }

        }
    }
}

