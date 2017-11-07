package com.arm.tourist.Auth;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.arm.tourist.UI.MainActivity;
import com.arm.tourist.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "SignIn Page";

    private AutoCompleteTextView mEmailFld;
    private EditText mPassFld;
    private Button mLoginBtn, mGoogleLoginBtn, mFbLoginBtn, mSignUpBtn, mForgotPassBtn;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mEmailFld = (AutoCompleteTextView) findViewById(R.id.email_txt);
        mPassFld = (EditText) findViewById(R.id.password_txt);

        findViewById(R.id.b_login).setOnClickListener(this);
        findViewById(R.id.b_googleSignUp).setOnClickListener(this);
        findViewById(R.id.b_facebookSignUp).setOnClickListener(this);
        findViewById(R.id.b_createAcc).setOnClickListener(this);
        findViewById(R.id.b_forgotPass).setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();

    }
    @Override
    public void onClick(View v)
    {
        int i = v.getId();
        if(i== R.id.b_login)
        {
            startSignIn();
        }
        else if(i==R.id.b_forgotPass)
        {
            Log.d(TAG,"Forgot Password");
            String emailAddress = mEmailFld.getText().toString().trim();
            if(emailAddress.equals(""))
            {
                mEmailFld.setError("Required");
                return;
            }

            mAuth.sendPasswordResetEmail(emailAddress)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "Email sent.");
                            // go to profile page or home page
                            //working on it
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(SignInActivity.this,"Password reset Email send failed",Toast.LENGTH_LONG )
                                    .show();
                        }
                    });
        }
        else if(i==R.id.b_createAcc)
        {
            startActivity(new Intent(SignInActivity.this, CreateAccountActivity.class));
            this.finish();
        }
        else if(i==R.id.b_googleSignUp)
        {
            Intent intent = new Intent(SignInActivity.this, GoogleSignInActivity.class);
            startActivity(intent);
            //this.finish();

        }
        else if(i==R.id.b_facebookSignUp)
        {
            //working on it
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = mAuth.getCurrentUser();
        updateUI(user);
    }

    private void updateUI(FirebaseUser user)
    {
        if(user!=null)
        {
            // go to news feed with user profile information
            Intent intent = new Intent(SignInActivity.this,MainActivity.class);
            startActivity(intent);
            this.finish();
        }
        else
        {
            //
            return;
        }
    }

    private void startSignIn() {
        String email = mEmailFld.getText().toString();
        String pass = mPassFld.getText().toString();

        if(!validateForm(email,pass))
        {
            return;
        }

        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(pass))
        {
            Toast.makeText(SignInActivity.this,"Field Empty!",Toast.LENGTH_LONG).show();
        }
        else {
            mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(!task.isSuccessful())
                    {
                        Toast.makeText(SignInActivity.this,"Sign in Problem",Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        Toast.makeText(SignInActivity.this,"Sign in successful",Toast.LENGTH_LONG).show();
                        updateUI(mAuth.getCurrentUser());
                    }
                }
            });
        }


    }

    private boolean validateForm(String email, String pass)
    {
        boolean valid = true;
        if(TextUtils.isEmpty(email))
        {
            mEmailFld.setError("Required");
            valid = false;
        }
        else mEmailFld.setError(null);
        if(TextUtils.isEmpty(pass))
        {
            mPassFld.setError("Required");
            valid = false;
        }
        else mPassFld.setError(null);

        return valid;
    }

    private void sendEmailVerification()
    {
        //findViewById(R.id.btn_sendEmailVerication).setEnabled(false);

        final FirebaseUser user = mAuth.getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //findViewById(R.id.btn_sendEmailVerication).setEnabled(true);

                        if(task.isSuccessful())
                        {
                            Toast.makeText(SignInActivity.this, "Verification Email sent to: "+user.getEmail(),Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            Log.e(TAG, "Send email verification ", task.getException());
                            Toast.makeText(SignInActivity.this,"Failed to send email verification",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

}