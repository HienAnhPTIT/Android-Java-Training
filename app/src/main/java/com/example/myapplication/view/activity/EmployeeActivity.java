package com.example.myapplication.view.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.controller.OnItemClickListener;
import com.example.myapplication.db.entity.EmployeeMapper;
import com.example.myapplication.model.Employee;
import com.example.myapplication.view.SideBar;
import com.example.myapplication.view.adapter.EmployeeAdapter;
import com.example.myapplication.view.fragment.AddEmpFragment;
import com.example.myapplication.viewmodel.EmployeeViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class EmployeeActivity extends AppCompatActivity {

    private List<Employee> mList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private EmployeeAdapter mAdapter;
    private FloatingActionButton mFabAdd;
    private ProgressBar mProgressBar;
    private EmployeeViewModel mViewModel;
    private SideBar mSideBar;
    private TextView mTextDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee);

        mRecyclerView = findViewById(R.id.recycler_view);
        mFabAdd = findViewById(R.id.fab);
        mProgressBar = findViewById(R.id.progressBar);
        mSideBar = findViewById(R.id.sidrbar);
        mTextDialog = findViewById(R.id.dialog);

        setAdapter();

        mSideBar.setTextView(mTextDialog);
        mSideBar.setOnTouchingLetterChangedListener(s -> {
            if (mAdapter != null) {
                int position = mAdapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    ((LinearLayoutManager) mRecyclerView.getLayoutManager()).scrollToPositionWithOffset(position, 0);
                }
            }
        });

        getSupportFragmentManager().addOnBackStackChangedListener(() -> {

            boolean isRoot = getSupportFragmentManager().getBackStackEntryCount() == 0;

            mFabAdd.setVisibility(isRoot ? View.VISIBLE : View.GONE);
        });

        mFabAdd.setOnClickListener(v -> {

            mFabAdd.setVisibility(View.GONE);

            AddEmpFragment fragment = AddEmpFragment.newInstance();

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        });

        getSupportFragmentManager().setFragmentResultListener("add_emp_result", this,
                (requestKey, result) -> {
                    Employee employee = (Employee) result.getSerializable("emp");
                    if (employee != null) {
                        mViewModel.addEmployee(employee);
                    }
                });
        mViewModel = new ViewModelProvider(this).get(EmployeeViewModel.class);
        mViewModel.getIsLoading().observe(this, isLoading -> {
            mProgressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        });
        mViewModel.getError().observe(this, errorMessages -> {
            Toast.makeText(this, "Error: " + errorMessages, Toast.LENGTH_SHORT).show();
        });
        mViewModel.getEmployees().observe(this, entities -> {
            mAdapter.setList(EmployeeMapper.toModelList(entities));
        });
        mViewModel.loadEmployee();
    }

    private void setAdapter() {

        mRecyclerView.setLayoutManager( new LinearLayoutManager(this));

        mAdapter = new EmployeeAdapter(EmployeeActivity.this, mList, new OnItemClickListener() {

            @Override
            public void clickItem(int pos) {
                Employee employee = EmployeeMapper.toModel(mViewModel.getEmployees().getValue().get(pos));
                Intent intent = new Intent(EmployeeActivity.this, DetailActivity.class);
                intent.putExtra("employee", employee);
                startActivity(intent);
            }

            @Override
            public void deleteItem(int pos, String string) {

                new AlertDialog.Builder(EmployeeActivity.this)
                        .setTitle("Confirm")
                        .setMessage(getString(R.string.confirm_delete, string))
                        .setPositiveButton("Yes", (dialog,which) -> {
                            mAdapter.removeItem(pos);
                            Toast.makeText(EmployeeActivity.this,"deleted " + string, Toast.LENGTH_SHORT).show();
                        })
                        .setNegativeButton("No", (dialog, which) -> {
                            dialog.dismiss();
                        })
                        .show();
            }

        });

        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setList(mList);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}