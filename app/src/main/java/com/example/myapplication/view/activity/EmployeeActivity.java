package com.example.myapplication.view.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
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

public class EmployeeActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private EmployeeAdapter mAdapter;
    private FloatingActionButton mFabAdd;
    private ProgressBar mProgressBar;
    private EmployeeViewModel mViewModel;
    private SideBar mSideBar;
    private TextView mTextDialog;
    private ImageButton btnToggleLayout;
    private View titleBar, searchContainer, listContainer;
    private SearchView searchView;
    private boolean isGridLayout = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee);

        initViews();
        setupViewModel();
        setupListeners();
        
        mViewModel.loadEmployee();
    }

    private void initViews() {
        mRecyclerView = findViewById(R.id.recycler_view);
        mFabAdd = findViewById(R.id.fab);
        mProgressBar = findViewById(R.id.progressBar);
        mSideBar = findViewById(R.id.sidrbar);
        mTextDialog = findViewById(R.id.dialog);
        btnToggleLayout = findViewById(R.id.btn_toggle_layout);
        titleBar = findViewById(R.id.title_bar);
        searchView = findViewById(R.id.search_view);
        searchContainer = findViewById(R.id.search_container);
        listContainer = findViewById(R.id.list_container);

        mAdapter = new EmployeeAdapter(this, new ArrayList<>(), new OnItemClickListener() {
            @Override
            public void clickItem(int pos) {
                Employee employee = mAdapter.getItem(pos);
                Intent intent = new Intent(EmployeeActivity.this, DetailActivity.class);
                intent.putExtra("employee", employee);
                startActivity(intent);
            }

            @Override
            public void deleteItem(int pos, String name) {
                confirmDelete(pos, name);
            }
        });

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
        mSideBar.setTextView(mTextDialog);
    }

    private void setupViewModel() {
        mViewModel = new ViewModelProvider(this).get(EmployeeViewModel.class);

        mViewModel.getIsLoading().observe(this, isLoading -> {
            mProgressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        });

        mViewModel.getError().observe(this, error -> {
            if (error != null) Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
        });

        mViewModel.getEmployees().observe(this, entities -> {
            mAdapter.setList(EmployeeMapper.toModelList(entities));
        });
    }

    private void setupListeners() {
        btnToggleLayout.setOnClickListener(v -> toggleLayout());

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) { return false; }

            @Override
            public boolean onQueryTextChange(String newText) {
                mAdapter.getFilter().filter(newText);
                return false;
            }
        });

        mSideBar.setOnTouchingLetterChangedListener(s -> {
            int position = mAdapter.getPositionForSection(s.charAt(0));
            if (position != -1) {
                ((LinearLayoutManager) mRecyclerView.getLayoutManager()).scrollToPositionWithOffset(position, 0);
            }
        });

        mFabAdd.setOnClickListener(v -> openAddEmployeeFragment());

        getSupportFragmentManager().setFragmentResultListener("add_emp_result", this, (requestKey, result) -> {
            Employee employee = (Employee) result.getSerializable("emp");
            if (employee != null) {
                mViewModel.addEmployee(employee);
            }
        });

        getSupportFragmentManager().addOnBackStackChangedListener(() -> {
            boolean isRoot = getSupportFragmentManager().getBackStackEntryCount() == 0;
            updateUIForRoot(isRoot);
        });
    }

    private void updateUIForRoot(boolean isRoot) {
        mFabAdd.setVisibility(isRoot ? View.VISIBLE : View.GONE);
        titleBar.setVisibility(isRoot ? View.VISIBLE : View.GONE);
        searchContainer.setVisibility(isRoot ? View.VISIBLE : View.GONE);
        listContainer.setVisibility(isRoot ? View.VISIBLE : View.GONE);
    }

    private void openAddEmployeeFragment() {
        AddEmpFragment fragment = AddEmpFragment.newInstance();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    private void toggleLayout() {
        isGridLayout = !isGridLayout;
        float density = getResources().getDisplayMetrics().density;
        
        if (isGridLayout) {
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
            btnToggleLayout.setImageResource(android.R.drawable.ic_menu_sort_by_size);
            int p = (int) (8 * density);
            btnToggleLayout.setPadding(p, p, p, p);
        } else {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            btnToggleLayout.setImageResource(android.R.drawable.ic_dialog_dialer);
            int p = (int) (12 * density);
            btnToggleLayout.setPadding(p, p, p, p);
        }
        mAdapter.setGridLayout(isGridLayout);
    }

    private void confirmDelete(int pos, String name) {
        new AlertDialog.Builder(this)
                .setTitle("Confirm")
                .setMessage(getString(R.string.confirm_delete, name))
                .setPositiveButton("Yes", (dialog, which) -> {
                    Employee emp = mAdapter.getItem(pos);
                    mViewModel.deleteEmployee(emp.getId());
                })
                .setNegativeButton("No", null)
                .show();
    }
}
