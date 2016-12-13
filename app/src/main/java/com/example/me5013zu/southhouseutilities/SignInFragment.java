package com.example.me5013zu.southhouseutilities;

import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;

/**
 * Created by Alex on 12/9/16.
 */

public class SignInFragment extends FragmentActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    private static final int REQUEST_CODE_SIGN_IN = 12345;
    protected static final String FIREBASE_USER_ID_PREF_KEY = "Firebase user id";
    protected static final String USERS_PREFS = "User_preferences";
    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private static final String Admin = "alexsimpson440.dev@gmail.com";

    private static final String TAG = "SIGN IN ACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_sign_in);

        //User signs in with google and then a google token is exchanged for a firebase token
        mFirebaseAuth = FirebaseAuth.getInstance();

        //google sign in to request the users data required by the application. requests basic data and email
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(getString(R.string.default_web_client_id))
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                Log.d(TAG, "onAuthStateChanged for user: " + firebaseAuth.getCurrentUser());
                FirebaseUser user = firebaseAuth.getCurrentUser();
                SignInFragment.this.authStateChanged(user);
            }
        };

        SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.sign_in_button) {
            signIn();
        }
    }

    //launches an activity where the user can sign into their google account or create a new google account
    public void signIn() {
        Intent SignInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(SignInIntent, REQUEST_CODE_SIGN_IN);
    }

    //deals with the result from signIn()
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignIn(result);
        }
    }

    private void handleSignIn(GoogleSignInResult result) {
        Log.d(TAG, "handleSignIn for result " + result.getSignInAccount());
        if(result.isSuccess()) {
            // now need to use these creds to authenticate for FireBase
            Log.d(TAG, "Google sign in success");
            GoogleSignInAccount account = result.getSignInAccount();
            firebaseAuthWithGoogleCreds(account);
        } else {
            Log.e(TAG, "Google sign in failed");
            //fail if no internet connection or not enabled in firebase console
            Toast.makeText(this, "Google sign in failed!", Toast.LENGTH_SHORT).show();
        }
    }

    //this takes the google sign in credentials to authenticate to the firebase
    private void firebaseAuthWithGoogleCreds(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        Log.d(TAG, "firebase auth attempt with creds " + credential);

        //attempt to sign into firebase with google creds. the onCompleteListener is used for logging success
        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            Log.d(TAG, "firebase auth success");
                        } else {
                            Log.d(TAG, "firebase auth fail");
                        }
                    }
                });
    }

    //add and remove listeners for auth state as this activity stops and starts
    //The mAuthStateChangedListener is what will permit the user to contine with the app once authenticated
    @Override
    public void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
    }

    //called if user signs in or signs out. also called when the app is launched
    //if the user has already authenticated, and their session has not timed out, they will not be
    //prompted to authenticate again.
    private void authStateChanged(FirebaseUser user) {
        if (user == null) {
            Log.d(TAG, "user is signed out");
        } else {
            Log.d(TAG, "user is signed in");

            //save the user id in shared prefs
            Log.d(TAG, "The user id is = " + user.getUid() + " " + user.toString());

            SharedPreferences.Editor prefEditor = getSharedPreferences(USERS_PREFS, MODE_PRIVATE).edit();
            prefEditor.putString(FIREBASE_USER_ID_PREF_KEY, user.getUid());
            prefEditor.apply();

            //then boot up the app by starting the MainActivity/AdminMainActivity
            Intent startMainActivity = new Intent(this, MainActivity.class);
            startActivity(startMainActivity);

        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "Connection to Google failed", Toast.LENGTH_LONG).show();
        Log.d(TAG, "CONNECTION FAILED" + connectionResult.getErrorMessage() + connectionResult.getErrorCode());
    }
}
