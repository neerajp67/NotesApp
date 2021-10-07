package com.neeraj.notesapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.neeraj.notesapp.R;
import com.neeraj.notesapp.database.DatabaseClass;
import com.neeraj.notesapp.fragments.FragmentHome;
import com.neeraj.notesapp.fragments.FragmentLogin;

public class MainActivity extends AppCompatActivity {

    GoogleSignInClient mGoogleSignInClient;
    SharedPreferences sharedPreferences;
    String personId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentLogin()).commit();
        }


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(MainActivity.this, gso);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        sharedPreferences = MainActivity.this.getSharedPreferences("com.neeraj.notesapp",
                Context.MODE_PRIVATE);
        personId = sharedPreferences.getString("userId", "");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.delete_all_notes) {
            deleteAllNotes();
        }
        if (item.getItemId() == R.id.sign_out) {
            signOut();
        }
        return super.onOptionsItemSelected(item);
    }

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        sharedPreferences = MainActivity.this.getSharedPreferences("com.neeraj.notesapp", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.clear().apply();

                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentLogin()).commit();
                    }
                });
    }

    private void deleteAllNotes() {
        try {
            DatabaseClass db = new DatabaseClass(MainActivity.this);
            db.deleteAllNotes(personId);
            Log.i("success activity", "done");

//            recreate();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentHome()).commit();
        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }
    }
}