package com.example.vignesh.crunchtime;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class calorie_converter extends AppCompatActivity {

    public HashMap<String, Integer> reps_per_exercise = new HashMap<String, Integer>();
    public HashSet<String> reps_exercises = new HashSet<String>();
    public HashSet<String> minutes_exercises = new HashSet<String>();
    public List<String> exercises_with_reps = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        reps_per_exercise.put("Pushup", 350);
        reps_per_exercise.put("Situp", 200);
        reps_per_exercise.put("Squats", 225);
        reps_per_exercise.put("Leg-lift", 25);
        reps_per_exercise.put("Plank", 25);
        reps_per_exercise.put("Jumping Jacks", 10);
        reps_per_exercise.put("Pullup", 100);
        reps_per_exercise.put("Cycling", 12);
        reps_per_exercise.put("Walking", 20);
        reps_per_exercise.put("Jogging", 12);
        reps_per_exercise.put("Swimming", 13);
        reps_per_exercise.put("Stair-Climbing", 15);

        reps_exercises.add("Pushup");
        reps_exercises.add("Situp");
        reps_exercises.add("Squats");
        reps_exercises.add("Pullup");

        minutes_exercises.add("Leg-lift");
        minutes_exercises.add("Plank");
        minutes_exercises.add("Jumping Jacks");
        minutes_exercises.add("Cycling");
        minutes_exercises.add("Walking");
        minutes_exercises.add("Jogging");
        minutes_exercises.add("Swimming");
        minutes_exercises.add("Stair-Climbing");

        for (String s : reps_per_exercise.keySet()) {
            exercises_with_reps.add(s + ": ");
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calorie_converter);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final EditText repsInput = (EditText) findViewById(R.id.input_field);
        final EditText weightInput = (EditText) findViewById(R.id.editText);
        final TextView outputText = (TextView) findViewById(R.id.textView);
        final TextView showCalories = (TextView) findViewById(R.id.textView2);
        final TextView input_type = (TextView) findViewById(R.id.textView3);
        final Spinner spinner = (Spinner) findViewById(R.id.spinner);
        final Switch goal_mode = (Switch) findViewById(R.id.switch1);
        final ListView lv = (ListView) findViewById(R.id.listView);
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                exercises_with_reps );

        lv.setAdapter(arrayAdapter);

        goal_mode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String exercise = spinner.getSelectedItem().toString();
                if (isChecked) {
                    input_type.setText("Caloric Goal");
                    if (reps_exercises.contains(spinner.getSelectedItem().toString())) {
                        outputText.setText("Reps Needed:");
                    } else if (minutes_exercises.contains(spinner.getSelectedItem().toString())) {
                        outputText.setText("Minutes Needed:");
                    }
                } else if (reps_exercises.contains(exercise)) {
                    input_type.setText("Number of Reps");
                    outputText.setText("Calories Burned:");
                } else if (minutes_exercises.contains(exercise)) {
                    input_type.setText("Number of Minutes");
                    outputText.setText("Calories Burned:");
                }
            }

        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
                if (!goal_mode.isChecked()) {
                    String exercise = spinner.getSelectedItem().toString();
                    if (reps_exercises.contains(exercise)) {
                        input_type.setText("Number of Reps");
                    } else if (minutes_exercises.contains(exercise)) {
                        input_type.setText("Number of Minutes");
                    } else {
                        input_type.setText("Not a valid exercise");
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }

        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String exercise = spinner.getSelectedItem().toString();
                if (repsInput.getText().toString().equals("") || weightInput.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Please enter all fields." , Toast.LENGTH_SHORT).show();
                } else {
                    if (goal_mode.isChecked()) {
                        int inputAmount = Integer.parseInt(repsInput.getText().toString());
                        int inputWeight = Integer.parseInt(weightInput.getText().toString());
                        double repsNeeded = 150.0*inputAmount/(inputWeight*(100.0/reps_per_exercise.get(exercise)));
                        showCalories.setText(String.format("%.2f", repsNeeded));
                        arrayAdapter.clear();
                        for (String s : reps_per_exercise.keySet()) {
                            double calorie_equivalent = 150*inputAmount/(inputWeight*(100.0/reps_per_exercise.get(s)));
                            if (reps_exercises.contains(s)) {
                                arrayAdapter.add(s + ": " + String.format("%.2f", calorie_equivalent) + " reps");
                            } else if (minutes_exercises.contains(s)) {
                                arrayAdapter.add(s + ": " + String.format("%.2f", calorie_equivalent) + " minutes");
                            } else {
                                arrayAdapter.add(s + ": " + String.format("%.2f", calorie_equivalent));
                            }
                        }
                    } else {
                        int inputAmount = Integer.parseInt(repsInput.getText().toString());
                        int inputWeight = Integer.parseInt(weightInput.getText().toString());
                        double numCalories = (100.0*inputAmount * inputWeight) / (150.0*reps_per_exercise.get(exercise));
                        showCalories.setText(String.format("%.2f", numCalories));
                        arrayAdapter.clear();
                        for (String s : reps_per_exercise.keySet()) {
                            double calorie_equivalent = 150*numCalories/(inputWeight*(100.0/reps_per_exercise.get(s)));
                            if (reps_exercises.contains(s)) {
                                arrayAdapter.add(s + ": " + String.format("%.2f", calorie_equivalent) + " reps");
                            } else if (minutes_exercises.contains(s)) {
                                arrayAdapter.add(s + ": " + String.format("%.2f", calorie_equivalent) + " minutes");
                            } else {
                                arrayAdapter.add(s + ": " + String.format("%.2f", calorie_equivalent));
                            }
                        }
                    }
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_calorie_converter, menu);
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
