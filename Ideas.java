package com.example.jmc31.midea;

import java.util.ArrayList;

//holds array of user's ideas.
public class Ideas {

    private ArrayList<Idea> list;

    Ideas() {
        this.list = new ArrayList<Idea>();
    }

    public void addIdea(Idea i) {
        this.list.add(i);
    }

    public ArrayList<Idea> getAllIdeas() {

        return this.list;
    }

    public void removeIdea(int id) {

        printIdeas();
        System.out.println("Removing idea");

        this.list.remove(getIdeaByID(id));
        printIdeas();
    }

    public void printIdeas() {

        String s = "";

        for (int i = 0; i < this.list.size(); i++) {

            s += "\n" + this.list.get(i).getID();

            s += "\n" + this.list.get(i).getTextContent();

        }
        System.out.println(s);
    }

    public Idea getIdeaByID(int id) {

        //printIdeas();

        System.out.println("getIdeaByID was called! List size: " + this.list.size());

        for (int i = 0; i < this.list.size(); i++) {

            if (this.list.get(i).getID() == id) {

                System.out.println("found idea with id" + id);

                return this.list.get(i);

            }
        }

        System.out.println("getIdeaByID returning null");
        return null;

    }

}
