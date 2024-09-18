package com.example.gridlayout;

import android.content.Context;
import android.graphics.Color;
import android.widget.TextView;

import org.w3c.dom.Text;

public class Block {
    private int indx; //position of the block in the grid
    private boolean isBomb; //is the bomb
    private TextView text_view; //the view image it is showing
    private Context context; //Connecter to the main
    private MainActivity main; //reference to main

    public Block(int indx, TextView textView, Context context, MainActivity main){
        this.indx = indx;
        this.text_view = textView;
        this.context = context;
        this.main = main;
        this.isBomb = false;
        text_view.setBackgroundColor(Color.GREEN);
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

    //Clck handler
    //If the cube is a bomb end game
    //Else tell how many sides of it share with a bomb
    public void onClick(){
        if(isBomb){
            text_view.setText(R.string.mine);
            text_view.setBackgroundColor(Color.RED);
            main.stopTimer();
            //main.results();
        }else{
            int adjacent_bombs = main.countAdjacentBombs(indx);
            text_view.setText(String.valueOf(adjacent_bombs));
            text_view.setBackgroundColor(Color.GRAY);
            text_view.setEnabled(false);
            if(adjacent_bombs == 0){
                main.revelSafeBlocks(indx);
            }

        }
    }
}

