package com.neeraj.notesapp.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.neeraj.notesapp.R;
import com.neeraj.notesapp.adapters.Adapter;
import com.neeraj.notesapp.adapters.OnRecyclerViewItemClickListener;
import com.neeraj.notesapp.database.DatabaseClass;
import com.neeraj.notesapp.models.Model;

import java.util.ArrayList;
import java.util.List;

public class FragmentHome extends Fragment implements OnRecyclerViewItemClickListener {
    Context context;

    SharedPreferences sharedPreferences;
    String personId;

    RecyclerView recyclerView;
    FloatingActionButton fab;
    CoordinatorLayout coordinatorLayout;

    Adapter adapter;
    List<Model> notesList;
    DatabaseClass databaseClass;
    private OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        sharedPreferences = context.getSharedPreferences("com.neeraj.notesapp",
                Context.MODE_PRIVATE);
        personId = sharedPreferences.getString("userId", "");
        Log.i("personId ", personId);

        recyclerView = view.findViewById(R.id.recycler_view);
        fab = view.findViewById(R.id.fab);
        coordinatorLayout = view.findViewById(R.id.layout_main);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddNote();
            }
        });

        notesList = new ArrayList<>();
        databaseClass = new DatabaseClass(context);

        fetchAllNotesFromDatabase();

        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        adapter = new Adapter(notesList, this);
        adapter.setOnRecyclerViewItemClickListener(FragmentHome.this);
        recyclerView.setAdapter(adapter);

        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(recyclerView);

        return view;
    }

    private void fetchAllNotesFromDatabase() {
        try {
            Cursor cursor = databaseClass.readAllData(personId);
            if (cursor.getCount() == 0) {
                Toast.makeText(context, "Please add new notes", Toast.LENGTH_SHORT).show();
            } else {
                while (cursor.moveToNext()) {
                    notesList.add(new Model(cursor.getString(0), cursor.getString(1),
                            cursor.getString(2)));
                }
            }
        } catch (Exception e) {
            Toast.makeText(context, "No Data to Show!", Toast.LENGTH_SHORT).show();
        }
    }

    private void openAddNote() {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        FragmentAddNotes fragmentAddNotes = new FragmentAddNotes();
        try {
            if (fragmentManager.findFragmentByTag(getTag()) == null) {
                fragmentTransaction.replace(R.id.fragment_container, fragmentAddNotes, getTag()).addToBackStack(getTag()).commit();
            } else {
                fragmentTransaction.show(fragmentManager.findFragmentByTag(getTag())).commit();
            }
        } catch (Exception e) {
            Log.i("Exception", e.toString());
        }

    }

    ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder,
                              @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            Model item = adapter.getList().get(position);

            adapter.removeItem(viewHolder.getAdapterPosition());

            Snackbar snackbar = Snackbar.make(coordinatorLayout, "Item deleted", Snackbar.LENGTH_LONG)
                    .setAction("Undo", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            adapter.restoreItem(item, position);
                            recyclerView.scrollToPosition(position);
                        }
                    }).addCallback(new BaseTransientBottomBar.BaseCallback<Snackbar>() {
                        @Override
                        public void onDismissed(Snackbar transientBottomBar, int event) {
                            super.onDismissed(transientBottomBar, event);

                            if (!(event == DISMISS_EVENT_ACTION)) {
                                DatabaseClass db = new DatabaseClass(context);
                                db.deleteSingleItem(item.getId(), personId);
                            }
                        }
                    });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }
    };

    @Override
    public void onItemClick(int adapterPosition) {
        Log.i("test click", notesList.get(adapterPosition).getDescription());
        try {
            if (!TextUtils.isEmpty(notesList.get(adapterPosition).getTitle()) &&
                    !TextUtils.isEmpty(notesList.get(adapterPosition).getDescription())) {

                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                FragmentUpdateNotes fragmentUpdateNotes = new FragmentUpdateNotes();

                if (fragmentManager.findFragmentByTag(getTag()) == null) {
                    Bundle args = new Bundle();
                    args.putString("id", notesList.get(adapterPosition).getId());
                    args.putString("title", notesList.get(adapterPosition).getTitle());
                    args.putString("description", notesList.get(adapterPosition).getDescription());
                    fragmentUpdateNotes.setArguments(args);

                    fragmentTransaction.replace(R.id.fragment_container, fragmentUpdateNotes, getTag()).addToBackStack(getTag()).commit();

                } else {
                    fragmentTransaction.show(fragmentManager.findFragmentByTag(getTag())).commit();
                }
            }
        } catch (Exception e) {
            Log.i("Exception", e.toString());
        }

    }
}
