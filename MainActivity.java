package com.example.jmc31.midea;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextThemeWrapper;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //list to store ideas
    public static Ideas ideas;
    public static boolean editMode = false;
    public int colorCount = 0;
    public ArrayList<View> touchedViews = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // instantiate list of ideas
        ideas = new Ideas();

    }

    // when new button is clicked
    public void newIdea(View view) {

        System.out.println("newIdea called!");

        // creates new intent to set up new idea
        Intent i = new Intent(MainActivity.this, EditIdeaActivity.class);
        i.putExtra("newIdea", 0);

        startActivityForResult(i, 1);
        // returns new view or null if new view wasn't created

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        System.out.println("onActivityResult called!");

        // when intent returns, if data sent back contains an idea id, set up a new view for that idea
        super.onActivityResult(requestCode, resultCode, data);

        //if we created a new idea
        if (data.getExtras().containsKey("ideaID")) {
            newIdeaView(data.getIntExtra("ideaID", -1));

        } else if (data.getExtras().containsKey("del")) {

            System.out.println("Removing view with id: " + data.getIntExtra("del", -1));

            ViewGroup p = null;
            try {
                // remove idea view from content main
                View v = findViewById(data.getIntExtra("del", -1));
                p = findViewById(R.id.ideasLayout);
                p.removeView(v);

            } catch (Exception e) {

                System.out.println("Failed trying to remove idea from " + p);

            }

        } else if (data.getExtras().containsKey("viewID")) {

            //if we edited a view
            editIdeaView(data.getIntExtra("viewID", -1));

        }

    }

    public void newIdeaView(int ideaID) {

        System.out.println("newIdeaView called!");

        try {

            final ConstraintLayout layout = findViewById(R.id.ideasLayout);
            ConstraintSet set = new ConstraintSet();

            Idea i = ideas.getIdeaByID(ideaID);

            //create new view for new idea
            View ideaView = new Button(new ContextThemeWrapper(this, R.style.ButtonStyle), null, 0);

            // cast view to textview so can set text
            TextView t = (TextView) ideaView;

            t.setMaxWidth(layout.getWidth() / 3);
            t.setMaxWidth(layout.getHeight() / 3);
            t.setTextAppearance(R.style.AppTheme);

            t.setText(i.getTextContent());
            // set id
            ideaView.setId(i.getID());

            switch (colorCount) {

                case 0:
                    ideaView.setBackgroundResource(R.color.c0);

                    break;
                case 1:
                    ideaView.setBackgroundResource(R.color.c1);
                    break;
                case 2:
                    ideaView.setBackgroundResource(R.color.c2);
                    break;
                case 3:
                    ideaView.setBackgroundResource(R.color.c3);
                    break;
                case 4:
                    ideaView.setBackgroundResource(R.color.c4);
                    break;
                case 5:
                    ideaView.setBackgroundResource(R.color.c5);
                    break;
                default:
                    colorCount = 0;
                    ideaView.setBackgroundResource(R.color.c0);
            }
            i.setColor(colorCount);
            colorCount++;

            ideaView.setOnClickListener(new View.OnClickListener() {

                public void onClick(View view) {

                    ideaClicked(view);

                }
            });

            ideaView.setOnTouchListener(new View.OnTouchListener() {

                public boolean onTouch(View view, MotionEvent event) {

                    //todo multitouch this isn't working
                    //keeps track of which views are currently touched

                    if (!editMode) {

                        if (event.getAction() == MotionEvent.ACTION_DOWN) {

                            touchedViews.add(view);
                            System.out.println("Touched views: " + touchedViews.size());
                            if (touchedViews.size() > 1) {
                                matchColors(touchedViews);
                            }
                        }

                        if (event.getAction() == MotionEvent.ACTION_MOVE) {

                            float h = Resources.getSystem().getDisplayMetrics().heightPixels;
                            float w = Resources.getSystem().getDisplayMetrics().widthPixels;

                            float yDif = h - layout.getHeight() + (view.getHeight() / 2);
                            float xDif = w - layout.getWidth() + (view.getWidth() / 2);

                            if (event.getRawX() > 0 && event.getRawX() < w) {
                                view.setX(event.getRawX() - xDif);
                            }

                            if (event.getRawY() > yDif && event.getRawY() < h) {
                                view.setY(event.getRawY() - yDif);
                            }

                        }

                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            touchedViews.remove(view);
                        }

                        return true;
                    } else
                        return false;
                }

            });

            layout.addView(ideaView);

            set.clone(layout);

            //CENTER
            set.connect(ideaView.getId(), ConstraintSet.START, layout.getId(), ConstraintSet.START, 0);
            set.connect(ideaView.getId(), ConstraintSet.END, layout.getId(), ConstraintSet.END, 0);
            set.connect(ideaView.getId(), ConstraintSet.TOP, layout.getId(), ConstraintSet.TOP, 0);
            set.connect(ideaView.getId(), ConstraintSet.BOTTOM, layout.getId(), ConstraintSet.BOTTOM, 0);


            /*
            //if it is first idea

            if (ideas.getAllIdeas().size() == 1) {

               set.connect(ideaView.getId(), ConstraintSet.TOP, layout.getId(), ConstraintSet.TOP, 0);



            } else {

                //else if it is second idea or after, set constraint relative to previous idea
                ViewGroup container = (ViewGroup) findViewById(R.id.ideasLayout);

                View prevIdea = container.getChildAt(container.indexOfChild(ideaView)-1);

                while (prevIdea == null) {

                    prevIdea = container.getChildAt(container.indexOfChild(prevIdea)-1);
                }
               set.connect(ideaView.getId(), ConstraintSet.TOP, prevIdea.getId(), ConstraintSet.BOTTOM, 16);

            }
            layout.requestLayout();

            */
            set.applyTo(layout);

        } catch (Exception e) {
            System.out.println("Invalid Idea ID: " + ideaID);
        }
    }

    // adds new idea to view

    // update view for edited idea
    public void editIdeaView(int ideaID) {
        System.out.println("editIdeaView called!");

        try {
            //get idea
            Idea i = ideas.getIdeaByID(ideaID);

            //get view inside ideasLayout that has id ideaID
            TextView v = findViewById(ideaID);
            v.setText(i.getTextContent());

            //update the content from ideas list
        } catch (Exception e) {
            System.out.println("No idea ID.");
        }
    }

    public void ideaClicked(View view) {
        // when any idea button is clicked
        System.out.println("ideaClicked called!");

        // see if editmode is true or not
        if (editMode) {

            // if true, then go to editidea activity,
            // creating new intent to pass the id of the idea that was clicked so it can be edited or deleted

            // creates new intent to edit idea
            Intent i = new Intent(MainActivity.this, EditIdeaActivity.class);
            i.putExtra("viewID", view.getId());

            startActivityForResult(i, 2);
            // returns new view or null if new view wasn't created
        }

    }

    public void matchColors(ArrayList<View> views) {

        System.out.println("matchColors called!");

        //get background colour of first view
        int v1id = views.get(0).getId();

        int col = ideas.getIdeaByID(v1id).getColor();

        System.out.println("col: " + col);

        //set background colour of all other views to same colour
        for (int i = 0; i < views.size(); i++) {
            //set color
            switch (col) {

                case 0:
                    views.get(i).setBackgroundResource(R.color.c0);

                    break;
                case 1:
                    views.get(i).setBackgroundResource(R.color.c1);
                    break;
                case 2:
                    views.get(i).setBackgroundResource(R.color.c2);
                    break;
                case 3:
                    views.get(i).setBackgroundResource(R.color.c3);
                    break;
                case 4:
                    views.get(i).setBackgroundResource(R.color.c4);
                    break;
                case 5:
                    views.get(i).setBackgroundResource(R.color.c5);
                    break;
                default:
                    break;
            }

            //update ideas list
            ideas.getIdeaByID(views.get(i).getId()).setColor(col);
            //update view
            views.get(i).invalidate();
        }

    }

    // when edit button is clicked
    public void editIdeas(View view) {

        System.out.println("editIdeas called!");
        View p = (View) view.getParent();

        if (editMode) {
            editMode = false;

            p.setBackgroundResource(R.color.none);

        } else {
            editMode = true;

            p.setBackgroundResource(R.color.edit);

        }
        System.out.println("editMode: " + editMode);

    }

}
