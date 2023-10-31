package com.example.justtypeon;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class StartActivity extends AppCompatActivity {
    Button login,register;
    FirebaseUser firebaseUser;


    @Override
    protected void onStart(){
        super.onStart();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser !=null){
            Intent intent = new Intent(StartActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        login = findViewById(R.id.login);
        register = findViewById(R.id.reg);
        login.setOnClickListener(v -> startActivity(new Intent(StartActivity.this,LoginActivity.class)));
        register.setOnClickListener(v -> startActivity(new Intent(StartActivity.this,RegisterActivity.class)));


    }
}