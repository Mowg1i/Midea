package com.example.jmc31.midea;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import static com.example.jmc31.midea.MainActivity.ideas;

public class EditIdeaActivity extends AppCompatActivity {

    static EditText ideaTextInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newidea);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        ideaTextInput = findViewById(R.id.ideaTextInput);
    }

    public void setIdeaTextInput(int ideaID) {

        System.out.println("setIdeaTextInput called!");

        String txt = ideas.getIdeaByID(ideaID).getTextContent();
        System.out.println("trying to set text" + txt);
        //todo
        ideaTextInput.setText(txt);

    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent i = getIntent();

        if (i.getExtras().containsKey("viewID")) {

            System.out.println("Resuming, editing view");
            int id = i.getIntExtra("viewID", -1);
            System.out.println("view id is: " + id);

            String txt = ideas.getIdeaByID(id).getTextContent();

            ideaTextInput.setText(txt);

        }

    }

    // when saveIdea button is clicked
    public void saveIdea(View view) {

        System.out.println("saveIdea called!");

        Intent i = getIntent();

        // save text input
        String t = ideaTextInput.getText().toString();

        // if we have got to the edit page from clicking an idea view in edit mode
        if (i.getExtras().containsKey("viewID")) {

            System.out.println("In SaveIdea, editing view");
            int id = i.getIntExtra("viewID", -1);
            System.out.println("view id is: " + id);

            ideas.getIdeaByID(id).setTextContent(t);
            ideas.printIdeas();

            i.putExtra("viewID", id);

        } else {

            // we are creating a new idea
            // when save is clicked, new idea is saved to ideas list

            Idea idea = new Idea();
            idea.setTextContent(t);
            ideas.addIdea(idea);
            ideas.printIdeas();

            ideas.printIdeas();

            i.putExtra("ideaID", idea.getID());

        }

        //if we send viewID then mainactivity knows to update the view that has given id
        // if ideaID then it will create a new view with given id

        setResult(RESULT_OK, i);
        // return to previous activity
        finish();

    }

    // when delete button is clicked
    public void deleteIdea(View view) {

        Intent i = getIntent();

        System.out.println("deleteIdea called! Ideas: ");
        ideas.printIdeas();

        // if we are editing an existing idea instead of creating a new one
        if (i.getExtras().containsKey("viewID")) {

            int id = i.getIntExtra("viewID", -1);

            // remove idea from list
            ideas.removeIdea(id);

            ideas.printIdeas();

            i.putExtra("del", id);

            setResult(RESULT_OK, i);
            // return to previous activity
            finish();
        } else {

            System.out.println("new idea aborted");

            i.putExtra("-", 0);

            setResult(RESULT_OK, i);
            // return to previous activity
            finish();
        }
    }

}
