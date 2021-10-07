package com.neeraj.notesapp.fragments;

import android.content.Context;
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

public class FragmentUpdateNotes extends Fragment {
    Context context;

    EditText title, description;
    Button updateNote;

    String id;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_update_note, container, false);

        title = view.findViewById(R.id.title);
        description = view.findViewById(R.id.description);
        updateNote = view.findViewById(R.id.updateNote);

        Bundle args = getArguments();
        title.setText(args.getString("title"));
        description.setText(args.getString("description"));
        id = args.getString("id");

        updateNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (!TextUtils.isEmpty(title.getText().toString()) &&
                            !TextUtils.isEmpty(description.getText().toString()) ){

                        DatabaseClass db = new DatabaseClass(context);
                        db.updateNotes(title.getText().toString(), description.getText().toString(), id);

                        openHomeFragment();

                    }else {
                        Toast.makeText(context, "Both fields are required", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e){
                    Log.e("Exception", e.toString());
                }

            }
        });

        return view;
    }

    private void openHomeFragment() {
        FragmentManager fragmentManager = getParentFragmentManager();
        fragmentManager.popBackStack();
    }

}
