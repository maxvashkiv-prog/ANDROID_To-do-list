package com.example.to_dolist;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText editTextTask;
    private Button buttonAdd;
    private RecyclerView recyclerView;

    private final ArrayList<ChecklistItem> items = new ArrayList<>();
    private ChecklistAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextTask = findViewById(R.id.editTextTask);
        buttonAdd = findViewById(R.id.buttonAdd);
        recyclerView = findViewById(R.id.recyclerView);

        adapter = new ChecklistAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        buttonAdd.setOnClickListener(v -> {
            items.add(0, new ChecklistItem(""));
            adapter.notifyItemInserted(0);
            recyclerView.scrollToPosition(0);
        });
    }

    private class ChecklistAdapter extends RecyclerView.Adapter<ChecklistAdapter.ViewHolder> {

        class ViewHolder extends RecyclerView.ViewHolder {
            CheckBox checkBox;
            EditText editText;
            TextWatcher textWatcher;

            ViewHolder(View view) {
                super(view);
                checkBox = view.findViewById(R.id.checkBox);
                editText = view.findViewById(R.id.editTextItem);
            }
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_checklist, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            ChecklistItem item = items.get(position);

            holder.checkBox.setOnCheckedChangeListener(null);

            if (holder.textWatcher != null) {
                holder.editText.removeTextChangedListener(holder.textWatcher);
            }

            holder.checkBox.setChecked(item.checked);
            holder.editText.setText(item.text);

            holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                int pos = holder.getAdapterPosition();
                if (pos == RecyclerView.NO_POSITION) return;

                ChecklistItem movedItem = items.get(pos);
                movedItem.checked = isChecked;

                items.remove(pos);

                if (isChecked) {
                    items.add(movedItem);
                } else {
                    items.add(0, movedItem);
                }

                notifyDataSetChanged();
            });

            holder.textWatcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    int pos = holder.getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        items.get(pos).text = s.toString();
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            };

            holder.editText.addTextChangedListener(holder.textWatcher);
        }

        @Override
        public int getItemCount() {
            return items.size();
        }
    }
}