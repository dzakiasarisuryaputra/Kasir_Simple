package com.example.Kasim;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private EditText edtEmail,edtPassword,edtNama;
    private TextView login, register;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        login = (TextView) findViewById(R.id.txtLoginfReg);
        login.setOnClickListener(this);

        register = (Button) findViewById(R.id.btnReg);
        register.setOnClickListener(this);

        edtNama = (EditText) findViewById(R.id.edtNama);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txtLoginfReg:
                startActivity(new Intent(this,Login.class));
                break;

            case R.id.btnReg:
                registerUser();
                break;
        }
    }

    private void registerUser() {
        String nama = edtNama.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        if(nama.isEmpty()){
            edtNama.setError("Mohon isikan nama anda!");
            edtNama.requestFocus();
            return;
        }

        if(email.isEmpty()){
            edtEmail.setError("Mohon isikan email anda!");
            edtEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            edtEmail.setError("Mohon isikan email yang valid!");
            edtEmail.requestFocus();
            return;
        }

        if(password.isEmpty()){
            edtPassword.setError("Mohon isikan nama anda!");
            edtPassword.requestFocus();
            return;
        }
        if(password.length() < 5){
            edtPassword.setError("Password harus minimal 5 karakter!");
            edtNama.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()){
                            User user = new User(nama,email,password);

                            FirebaseDatabase.getInstance().getReference("User")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                Toast.makeText(Register.this,"User telah ditambahkan!",Toast.LENGTH_LONG).show();
                                                startActivity(new Intent(Register.this,Login.class));
                                            }
                                            else{
                                                Toast.makeText(Register.this,"User gagal ditambahkan!",Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                        }else{
                            Toast.makeText(Register.this,"User gagal ditambahkan!",Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }
}