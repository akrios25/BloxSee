package com.example.franciscoandrade.bloxsee.views;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.franciscoandrade.bloxsee.R;
import com.example.franciscoandrade.bloxsee.model.Teacher;
import com.example.franciscoandrade.bloxsee.views.teacher.TeacherMainPageActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.content.ContentValues.TAG;

public class TeacherSignInFragment extends Fragment {
    private View view;
    private Button signInButton, signUp_Btn;
    private EditText email_ET, name_ET, password_ET, email_edittext, password_edittext;
    private Button signUp;
    private LinearLayout signUp_container, signIn_container;
    private String nameText, emailText, passwordText;
    //1.Firebase
    private FirebaseAuth mAuth;
    private ProgressBar progress;
    private DatabaseReference ref;
    private FirebaseDatabase database;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_teacher_sign_in, container, false);
        database = FirebaseDatabase.getInstance();
        ref = database.getReference();
        setUpViews();
        nameText = name_ET.getText().toString();
        emailText = email_ET.getText().toString();
        passwordText = password_ET.getText().toString();
        login();
        signUp();
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRegister();
            }
        });
        return view;
    }


    //setting all views
    private void setUpViews() {
        signInButton = view.findViewById(R.id.SignIn);
        signUp_Btn = view.findViewById(R.id.signUp_Btn);
        email_ET = view.findViewById(R.id.email_ET);
        name_ET = view.findViewById(R.id.name_ET);
        password_ET = view.findViewById(R.id.password_ET);
        signUp = view.findViewById(R.id.signUp);
        progress = view.findViewById(R.id.progress);
        signIn_container = view.findViewById(R.id.signIn_container);
        signUp_container = view.findViewById(R.id.signUp_container);
        mAuth = FirebaseAuth.getInstance();
    }

    /**
     * Hides Sign in container and shows Sign UP container
     * gets instance of firebase to authenticate
     */
    private void signUp() {
        signUp_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn_container.setVisibility(View.GONE);
                signUp_container.setVisibility(View.VISIBLE);
                progress.setVisibility(View.GONE);
            }
        });
    }

    /**
     * Handles User Creation if not exist
     * Adds User to Db if successfully added as user
     */
    private void startRegister() {
        progress.setVisibility(View.VISIBLE);
//        nameText = name_ET.getText().toString();
//        emailText = email_ET.getText().toString();
//        passwordText = password_ET.getText().toString();
        if (!TextUtils.isEmpty(nameText) && !TextUtils.isEmpty(emailText) && !TextUtils.isEmpty(passwordText)) {
            mAuth.createUserWithEmailAndPassword(emailText, passwordText)
                    .addOnCompleteListener(new OnCompleteListener <AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task <AuthResult> task) {
                            if (task.isSuccessful()) {
                                progress.setVisibility(View.GONE);
                                String user_id = mAuth.getCurrentUser().getUid();
                                Toast.makeText(getActivity(), user_id, Toast.LENGTH_SHORT).show();
                                signIn_container.setVisibility(View.VISIBLE);
                                signUp_container.setVisibility(View.GONE);
                                createNewUser(nameText, emailText, user_id);
                                emptyEditText();
                            }

                            task.addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progress.setVisibility(View.GONE);
                                    Toast.makeText(getActivity(), "Login Failed User exist already", Toast.LENGTH_SHORT).show();
                                    emptyEditText();
                                }
                            });
                        }
                    });
        }
    }

    /**
     * @param name
     * @param email
     * @param user_id Method Runs if user succesfully created a new account
     *                Creates a new instance of Teacher which is added to the FireBase DataBase to teacher section
     */
    private void createNewUser(String name, String email, String user_id) {
        Teacher teacher = new Teacher(name, email, user_id);
        ref.child("teacher").child(name).setValue(teacher);
    }


    /**
     * Method checks if user has valid credentials
     * If user approved, user can access their account
     */
    private void login() {
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "login: HELLLLLOOOOOOOO");
                if (!TextUtils.isEmpty(emailText) && !TextUtils.isEmpty(passwordText)) {
                    mAuth.signInWithEmailAndPassword(emailText, passwordText)
                            .addOnCompleteListener(new OnCompleteListener <AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task <AuthResult> task) {
                                    if (!task.isSuccessful()) {
                                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                                        Toast.makeText(getActivity(), "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                        emptyEditText();
                                    } else {
                                        Log.d(TAG, "onComplete: It is working");
                                        intentToTeacherMainPageActivity();
                                        emptyEditText();
                                    }
                                }
                            });
                }
            }
        });
    }

    private void intentToTeacherMainPageActivity() {
        Intent intent = new Intent(view.getContext(), TeacherMainPageActivity.class);
        view.getContext().startActivity(intent);
    }

    /**
     * Empty Editext Text
     */
    private void emptyEditText() {
        name_ET.setText("");
        email_ET.setText("");
        password_ET.setText("");
    }

}
