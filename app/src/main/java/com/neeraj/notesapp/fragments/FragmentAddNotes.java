package com.neeraj.notesapp.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.neeraj.notesapp.R;
import com.neeraj.notesapp.database.DatabaseClass;

public class FragmentAddNotes extends Fragment {
    Context context;

    EditText title, description;
    Button addNote;

    SharedPreferences sharedPreferences;
    String personId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_notes, container, false);

        sharedPreferences = context.getSharedPreferences("com.neeraj.notesapp",
                Context.MODE_PRIVATE);
        personId = sharedPreferences.getString("userId", "");
        Log.i("personId add notes ", personId);

        title = view.findViewById(R.id.title);
        description = view.findViewById(R.id.description);
        addNote = view.findViewById(R.id.addNote);

        addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!TextUtils.isEmpty(title.getText().toString()) &&
                        !TextUtils.isEmpty(description.getText().toString()) ){

                    DatabaseClass db = new DatabaseClass(context);
                    db.addNotes(title.getText().toString(), description.getText().toString(), personId);

                    openHomeFragment();

                }else {
                    Toast.makeText(context, "Both fields are required!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    private void openHomeFragment() {
        try {
            FragmentManager fragmentManager = getParentFragmentManager();
            fragmentManager.popBackStack();
        } catch (Exception e){
            Toast.makeText(context, "Please try again!", Toast.LENGTH_SHORT).show();
        }

    }
}
