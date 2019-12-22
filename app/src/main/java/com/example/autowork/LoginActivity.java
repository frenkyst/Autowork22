package com.example.autowork;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import com.example.autowork.kasir.KasirActivity;
import com.example.autowork.model.UserMan;
import com.example.autowork.owner.BossActivity;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    //a constant for detecting the login intent result
    private static final int RC_SIGN_IN = 234;

    //Tag for the logs optional
    private static final String TAG = "simplifiedcoding";

    //creating a GoogleSignInClient object
    GoogleSignInClient mGoogleSignInClient;

    //And also a Firebase Auth object
    FirebaseAuth mAuth;

    private DatabaseReference database;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        //first we intialized the FirebaseAuth object
        mAuth = FirebaseAuth.getInstance();

        //Then we need a GoogleSignInOptions object
        //And we need to build it as below
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        //Then we will get the GoogleSignInClient object from GoogleSignIn class
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        //Now we will attach a click listener to the sign_in_button
        //and inside onClick() method we are calling the signIn() method that will open
        //google sign in intent
        findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

    }




    @Override
    protected void onStart() {
        super.onStart();

        //if the user is already signed in
        //we will close this activity
        //and take the user to profile activity
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {

            findViewById(R.id.sign_in_button).setVisibility(View.GONE);
            findViewById(R.id.loadingLogin).setVisibility(View.VISIBLE);

            UserMan();




        }
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //if the requestCode is the Google Sign In code that we defined at starting
        if (requestCode == RC_SIGN_IN) {

            //Getting the GoogleSignIn Task
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                //Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);

                //authenticating with firebase
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Toast.makeText(LoginActivity.this, "Api Key Error yak e"+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        //getting the auth credential
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);

        //Now using firebase we are signing in the user here
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            Toast.makeText(LoginActivity.this, "User Signed In", Toast.LENGTH_SHORT).show();
                            onStart(); // ke activity main KEY THIS
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });
    }


    //this method is called on click
    private void signIn() {
        //getting the google signin intent
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();

        //starting the activity for result
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    /**
     * =================================================================================================================================(STAR)
     * PENGATURAN USERMANAGER KASIR DAN KARYAWAN AKAN DI APRROF OLEH OWNER ATAU ADMIN
     */
    public void UserMan() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();
            String uid = user.getUid();



            database = FirebaseDatabase.getInstance().getReference().child(GlobalVariabel.Toko).child(GlobalVariabel.UserMan).child(uid);
            database.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    String uid1 =(dataSnapshot.child("uid").getValue().toString());
//                    String status1 = String.valueOf((dataSnapshot.child("status").getValue()));

                    if (dataSnapshot.child("uid").exists()) {

//                        Toast.makeText(LoginActivity.this, "  CEK ?  ", Toast.LENGTH_SHORT).show();

                        if (dataSnapshot.child("BOSS").exists()){

                            confim("BOSS");

                        } else if (dataSnapshot.child("Karyawan").exists()){

                            confim("Karyawan");

                        } else if (dataSnapshot.child("Kasir").exists()){

                            confim("Kasir");

                        } else {

                            findViewById(R.id.loadingLogin).setVisibility(View.INVISIBLE);
                            findViewById(R.id.belumTerdaftar).setVisibility(View.VISIBLE);



                        }

//                        confim();

                    } else {

                        submit(new UserMan(
                                        name,
                                        email.toLowerCase(),
                                        uid),
                                uid
                        );

                        UserMan();

                        Toast.makeText(LoginActivity.this, "  Register ?  ", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(LoginActivity.this, "  Google Play Error  ", Toast.LENGTH_SHORT).show();
                }
            });



//            submit(new UserMan(
//                            name,
//                            email.toLowerCase(),
//                            uid),
//                    uid
//            );
//
//
//            if (email.equals("fs75614011@gmail.com")) {
//
//                startActivity(new Intent(this,KasirActivity.class));
//
//            }
//            Toast.makeText(LoginActivity.this, name+"  "+uid+"  "+email, Toast.LENGTH_SHORT).show();

        }
    }

    /**
     * =================================================================================================================================(END)
     */


    /**
     * @deprecated push data user ke firebase dari variabel userman
     * @param userman data user
     * @param uid key lokasi penyimpanan data
     */
    private void submit(UserMan userman, String uid) {
        database = FirebaseDatabase.getInstance().getReference();
        database.child(GlobalVariabel.Toko)
                .child(GlobalVariabel.UserMan)
                .child(uid)
                .setValue(userman);

        Toast.makeText(LoginActivity.this, "input data user",
                Toast.LENGTH_SHORT).show();
    }

    /**
     * @deprecated menampilkan BossActivity/MainActivity/KasirActivity
     * @param select BOSS/Karyawan/Kasir
     */
    private void confim(String select){

        if (select.equals("BOSS")){
            startActivity(new Intent(this, BossActivity.class));
        } else if (select.equals("Karyawan")){
            startActivity(new Intent(this, MainActivity.class));
        } else if (select.equals("Kasir")){
            startActivity(new Intent(this, KasirActivity.class));
        }

        finish();


    }

}
