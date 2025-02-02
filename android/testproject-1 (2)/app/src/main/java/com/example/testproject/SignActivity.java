package com.example.testproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SignActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    EditText mEmailText, mPasswdText, mPasswdTextCheck, mName;
    ImageView SignupButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);

        firebaseAuth = FirebaseAuth.getInstance();

        mEmailText =findViewById(R.id.editid);
        mPasswdText = findViewById(R.id.editpwd);
        mPasswdTextCheck = findViewById(R.id.editpwdc);
        mName = findViewById(R.id.editsname);
        SignupButton = findViewById(R.id.buttonSign);


        SignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = mEmailText.getText().toString().trim();
                String pwd = mPasswdText.getText().toString().trim();
                String pwdcheck = mPasswdTextCheck.getText().toString().trim();

                if (pwd.equals(pwdcheck)){
                    firebaseAuth.createUserWithEmailAndPassword(email,pwd).addOnCompleteListener(SignActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()){
                                FirebaseUser user = firebaseAuth.getCurrentUser();
                                String email = user.getEmail();
                                String uid = user.getUid();
                                String name = mName.getText().toString().trim();

                                HashMap<Object,String>hashMap = new HashMap<>();

                                hashMap.put("uid",uid);
                                hashMap.put("email",email);
                                hashMap.put("name",name);

                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference reference = database.getReference("Users");
                                reference.child(uid).setValue(hashMap);

                                Intent intent = new Intent(SignActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                                Toast.makeText(SignActivity.this,"회원가입이 성공적으로 되었습니다",Toast.LENGTH_SHORT).show();



                            }else{
                                if(task.getException() != null) //추가 라인 전체
                                Toast.makeText(SignActivity.this,"이미존재하는 아이디", Toast.LENGTH_SHORT).show();
                            }
                            return;
                        }
                    });


                }else{
                    Toast.makeText(SignActivity.this, "비밀번호가 틀렸습니다. 다시 입력해 주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

            }
        });


    }
    }

