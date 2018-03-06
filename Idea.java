package com.example.jmc31.midea;

import android.view.View;
import android.widget.TextView;

public class Idea {

    private static int IDcount = 0;
    public TextView view;
    private int ID;
    private int color;
    private String textContent = "";
    //keeps list of all linked ideas
    private Ideas linkedIdeas;

    Idea() {

        System.out.println("Creating new idea with id: " + IDcount);
        this.ID = IDcount;
        IDcount++;
        this.textContent = "";

    }

    public int getColor() {
        return this.color;
    }

    public void setColor(int c) {
        this.color = c;
    }

    public int getID() {
        return this.ID;
    }

    public String getTextContent() {
        return this.textContent;
    }

    public void setTextContent(String s) {
        this.textContent = s;
    }

    public View getView() {
        return this.view;
    }

}