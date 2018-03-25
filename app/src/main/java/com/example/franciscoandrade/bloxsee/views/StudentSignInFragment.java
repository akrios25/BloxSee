package com.example.franciscoandrade.bloxsee.views;


import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.franciscoandrade.bloxsee.R;
import com.example.franciscoandrade.bloxsee.model.Student;
import com.example.franciscoandrade.bloxsee.views.student.BlocklyActivity;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class StudentSignInFragment extends Fragment implements View.OnClickListener {

    private View v;
    private Spinner spinner;

    private ArrayList<String> listStudents;
    private ImageView penguin, ghost, dog, cat, dragon, octopus;
    private Button studentLogInBtn;
    //Firebase Setup
    private DatabaseReference ref;
    private FirebaseDatabase database;
    DatabaseReference messageRef;
    Student student;
    String animalPicked;
    String pass;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_student_sign_in, container, false);
        spinner = v.findViewById(R.id.spinnerNames);
        penguin = v.findViewById(R.id.penguin);
        ghost = v.findViewById(R.id.ghost);
        dog = v.findViewById(R.id.dog);
        cat = v.findViewById(R.id.cat);
        dragon = v.findViewById(R.id.dragon);
        octopus = v.findViewById(R.id.octopus);
        studentLogInBtn = v.findViewById(R.id.studentLogInBtn);
        imageSetClicks();
        //getStudentsList();
        listStudents = new ArrayList<>();
        listStudents.add("Student Name");

        new AsyncClass().execute();

        return v;
    }

    private void imageSetClicks() {
        penguin.setOnClickListener(this);
        ghost.setOnClickListener(this);
        dog.setOnClickListener(this);
        cat.setOnClickListener(this);
        dragon.setOnClickListener(this);
        octopus.setOnClickListener(this);
        studentLogInBtn.setOnClickListener(this);
    }


    private void getStudentsList() {
//        listStudents = new ArrayList <>();
//        database= FirebaseDatabase.getInstance();
//        ref= database.getReference();
//        student= new Student();
//
//        ChildEventListener childEventListener= new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                student=dataSnapshot.getValue(Student.class);
//                Log.d("CHILD", "onChildAdded: "+ student.getName());
//                listStudents.add(student.getName());
//                ArrayAdapter <String> adapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_itemtwo, listStudents);
//                // adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
//                spinner.setAdapter(adapter);
//
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        };
//
//
//        ref.child("students").addChildEventListener(childEventListener);

        //setSpinner();
    }


    @Override
    public void onClick(View v) {
        clearBackground();
        switch (v.getId()) {
            case R.id.penguin:
                Toast.makeText(getActivity(), "Penguin", Toast.LENGTH_SHORT).show();
                animalPicked = "Penguin";
                penguin.setBackgroundResource(R.color.colorAccent);
                break;

            case R.id.ghost:
                ghost.setBackgroundResource(R.color.colorAccent);
                animalPicked = "Ghost";
                Toast.makeText(getActivity(), "ghost", Toast.LENGTH_SHORT).show();
                break;

            case R.id.dog:
                dog.setBackgroundResource(R.color.colorAccent);
                animalPicked = "Dog";
                Toast.makeText(getActivity(), "dog", Toast.LENGTH_SHORT).show();
                break;

            case R.id.cat:
                cat.setBackgroundResource(R.color.colorAccent);
                animalPicked = "Cat";
                Toast.makeText(getActivity(), "Cat", Toast.LENGTH_SHORT).show();
                break;


            case R.id.dragon:
                dragon.setBackgroundResource(R.color.colorAccent);
                animalPicked = "Dragon";
                Toast.makeText(getActivity(), "dragon", Toast.LENGTH_SHORT).show();
                break;

            case R.id.octopus:
                octopus.setBackgroundResource(R.color.colorAccent);
                animalPicked = "Octopus";
                Toast.makeText(getActivity(), "Octopus", Toast.LENGTH_SHORT).show();
                break;

            case R.id.studentLogInBtn:
                loginStudent();
                Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT).show();
                break;
        }


    }

    private void loginStudent() {
        String nameStudent = spinner.getSelectedItem().toString();


        if (!TextUtils.isEmpty(animalPicked) && !nameStudent.equals(listStudents.get(0)) && spinner.getSelectedItem().toString() != null) {
            Log.d("SPINNER==", "loginStudent: Animal: READY TO LOG IN");
            String result = getPasswordOfUser(nameStudent, animalPicked);

            Log.d("LOGIN====", "loginStudent: " + result);
            Log.d("LOGIN====", "loginStudent: " + animalPicked);


            Log.d("LOGIIN==", "loginStudent: " + ref.child("students").child(nameStudent).getKey());

        }

    }

    private String getPasswordOfUser(String student, final String password) {

        messageRef = ref.child("students").child(student).child("password");
        ref = database.getReference();
        pass = "";
        messageRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("LOGIIN==", "loginStudent: 111" + dataSnapshot.getValue());
                pass = dataSnapshot.getValue().toString();
                Log.d("LOGIIN==", "loginStudent: 2222 " + dataSnapshot.getValue());

                checkLoginCredentials(pass, password);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Log.d("LOGIIN==", "loginStudent: 2222111 " + pass);

        return pass;
    }

    private void checkLoginCredentials(String pass, String password) {
        Log.d("CHECKING", "checkLoginCredentials: " + pass);
        Log.d("CHECKING", "checkLoginCredentials: " + password);

        if (pass.equals(password)) {
            String studentName = spinner.getSelectedItem().toString();
            Intent intent = new Intent(getActivity(), StudentActivity.class);
            intent.putExtra("studentName", studentName);
            startActivity(intent);
            Log.d("CHECKING", "checkLoginCredentials: PASSEED");
            Toast.makeText(getActivity(), "PASSED", Toast.LENGTH_SHORT).show();

        } else {
            Log.d("STUDENT", "loginStudent: password Doesnt match");
            Toast.makeText(getActivity(), "loginStudent: password Doesnt match", Toast.LENGTH_SHORT).show();
        }


    }

    private void clearBackground() {
        penguin.setBackgroundResource(R.color.colorWhite);
        ghost.setBackgroundResource(R.color.colorWhite);
        dog.setBackgroundResource(R.color.colorWhite);
        cat.setBackgroundResource(R.color.colorWhite);
        dragon.setBackgroundResource(R.color.colorWhite);
        octopus.setBackgroundResource(R.color.colorWhite);


    }

    private class AsyncClass extends AsyncTask<Void, Void, Void> {
        ArrayAdapter<String> adapter;

        @Override
        protected Void doInBackground(Void... voids) {
            database = FirebaseDatabase.getInstance();
            ref = database.getReference();
            student = new Student();

            ChildEventListener childEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    student = dataSnapshot.getValue(Student.class);
                    listStudents.add(dataSnapshot.getKey());
                    Log.d("CHILDren==", "onChildAdded: " + student.getName());
                    Log.d("CHILD==", "onChildAdded: " + student.getName());
                    // adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);


                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };


            ref.child("students").addChildEventListener(childEventListener);
            adapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item, listStudents);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            spinner.setAdapter(adapter);
        }

    }
}


