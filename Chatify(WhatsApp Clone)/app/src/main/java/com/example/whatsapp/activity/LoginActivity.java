package com.example.whatsapp.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.whatsapp.R;
import com.example.whatsapp.databinding.ActivityLoginBinding;
import com.example.whatsapp.models.Users;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding loginBinding;
    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;
    GoogleSignInClient signInClient;
    private int RC_SIGN_IN=60;
    FirebaseDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginBinding=ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(loginBinding.getRoot());
        getSupportActionBar().hide();

        firebaseAuth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();

       progressDialog=new ProgressDialog(LoginActivity.this);
       progressDialog.setTitle("Please Wait....");
       progressDialog.setMessage("Login to your account");

        loginBinding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(loginBinding.loginEmail.getText().toString() .isEmpty())
                {
                   loginBinding.loginEmail.setError("Enter Email_Id");
                   loginBinding.loginEmail.requestFocus();
                   return;
                }
                if(loginBinding.loginPass.getText().toString() .isEmpty())
                {
                    loginBinding.loginPass.setError("Enter Password");
                    loginBinding.loginPass.requestFocus();
                    return;
                }
                progressDialog.show();

                firebaseAuth.signInWithEmailAndPassword(loginBinding.loginEmail.getText().toString(),loginBinding.loginPass.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressDialog.dismiss();
                                    if(task.isSuccessful())
                                    {
                                       Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                                       startActivity(intent);
                                    }
                                    else
                                    {
                                        Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
            }
        });

        //Google Login
//        BeginSignInRequest signInRequest = BeginSignInRequest.builder()
//                .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
//                        .setSupported(true)
//                        // Your server's client ID, not your Android client ID.
//                        .setServerClientId(getString(R.string.default_web_client_id))
//                        // Only show accounts previously used to sign in.
//                        .setFilterByAuthorizedAccounts(true)
//                        .build())
//                .build();

        GoogleSignInOptions signInOptions=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail().build();
        signInClient= GoogleSignIn.getClient(this,signInOptions);

        loginBinding.loginGoogleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                googleLogin();
                //Toast.makeText(LoginActivity.this, "Login Process by Gmail", Toast.LENGTH_SHORT).show();
            }
        });

        loginBinding.loginFacebookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginActivity.this, "Login Process by Facebook", Toast.LENGTH_SHORT).show();
            }
        });



        loginBinding.signUpActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),SignUpActivity.class));
            }
        });

        if(firebaseAuth.getCurrentUser()!=null)
        {
            Intent intent=new Intent(LoginActivity.this,MainActivity.class);
            startActivity(intent);
        }
    }

    private void googleLogin()
    {
        Intent intent=signInClient.getSignInIntent();
        startActivityIfNeeded(intent,RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==RC_SIGN_IN)
        {
            Task<GoogleSignInAccount> task=GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account=task.getResult(ApiException.class);
                Log.d("TAG","firebaseAuthWithGoogle:"+account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            }
            catch (ApiException e)
            {
               Log.w("TAG","Google login failed",e);
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential= GoogleAuthProvider.getCredential(idToken,null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this,new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                       if(task.isSuccessful())
                       {
                          Log.d("TAG","SignInWithCredential:Success");

                           FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
                           Users users=new Users();
                           users.setUserId(firebaseUser.getUid());
                           users.setUserName(firebaseUser.getDisplayName());
                           users.setProfilePic(firebaseUser.getPhotoUrl().toString());
                           database.getReference().child("Users").child(firebaseUser.getUid()).setValue(users);


                           Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                           startActivity(intent);
                           finish();
                           Toast.makeText(LoginActivity.this, "Login with Google..", Toast.LENGTH_SHORT).show();
                       }
                       else
                       {
                           Log.w("TAG","SignInWithCredential:Failed",task.getException());
                       }
                    }
                });
    }


}