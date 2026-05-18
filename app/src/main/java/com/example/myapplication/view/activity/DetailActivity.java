package com.example.myapplication.view.activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.R;
import com.example.myapplication.model.Employee;
import com.example.myapplication.viewmodel.EmployeeViewModel;
import com.google.android.material.textfield.TextInputEditText;

public class DetailActivity extends AppCompatActivity {
    private TextInputEditText etName, etDepartment, etPosition, etSalary, etEmail, etPhone;
    private TextView tvName, tvDepartment, tvPosition, tvSalary, tvEmail, tvPhone;
    private Button btnEdit, btnSave, btnDelete;
    private LinearLayout layoutViewMode, layoutEditMode;
    private EmployeeViewModel mViewModel;
    private Employee currentEmployee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        initViews();
        mViewModel = new ViewModelProvider(this).get(EmployeeViewModel.class);

        currentEmployee = (Employee) getIntent().getSerializableExtra("employee");
        if (currentEmployee != null) {
            fillData();
        }

        toggleEditMode(false); // Bắt đầu ở chế độ View (Profile)

        btnEdit.setOnClickListener(v -> toggleEditMode(true));
        btnSave.setOnClickListener(v -> updateEmployee());
        btnDelete.setOnClickListener(v -> confirmDelete());

        mViewModel.getError().observe(this, error -> {
            if (error != null) Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
        });

        mViewModel.getIsLoading().observe(this, isLoading -> {
            btnSave.setEnabled(!isLoading);
            btnDelete.setEnabled(!isLoading);
            btnEdit.setEnabled(!isLoading);
        });
    }

    private void initViews() {
        // Edit fields
        etName = findViewById(R.id.et_name);
        etDepartment = findViewById(R.id.et_department);
        etPosition = findViewById(R.id.et_position);
        etSalary = findViewById(R.id.et_salary);
        etEmail = findViewById(R.id.et_email);
        etPhone = findViewById(R.id.et_phone);
        
        // View fields
        tvName = findViewById(R.id.tv_name);
        tvDepartment = findViewById(R.id.tv_department);
        tvPosition = findViewById(R.id.tv_position);
        tvSalary = findViewById(R.id.tv_salary);
        tvEmail = findViewById(R.id.tv_email);
        tvPhone = findViewById(R.id.tv_phone);

        // Buttons and Layouts
        btnEdit = findViewById(R.id.btn_edit);
        btnSave = findViewById(R.id.btn_save);
        btnDelete = findViewById(R.id.btn_delete);
        layoutViewMode = findViewById(R.id.layout_view_mode);
        layoutEditMode = findViewById(R.id.layout_edit_mode);
    }

    private void fillData() {
        // Fill View labels
        tvName.setText(currentEmployee.getName());
        tvDepartment.setText(currentEmployee.getDepartment());
        tvPosition.setText(currentEmployee.getPosition());
        tvSalary.setText(String.format("%,.0f", currentEmployee.getSalary()));
        tvEmail.setText(currentEmployee.getEmail());
        tvPhone.setText(currentEmployee.getPhone());

        // Fill Edit fields
        etName.setText(currentEmployee.getName());
        etDepartment.setText(currentEmployee.getDepartment());
        etPosition.setText(currentEmployee.getPosition());
        etSalary.setText(String.valueOf((long)currentEmployee.getSalary()));
        etEmail.setText(currentEmployee.getEmail());
        etPhone.setText(currentEmployee.getPhone());
    }

    private void toggleEditMode(boolean enabled) {
        if (enabled) {
            layoutViewMode.setVisibility(View.GONE);
            layoutEditMode.setVisibility(View.VISIBLE);
            btnEdit.setVisibility(View.GONE);
            btnSave.setVisibility(View.VISIBLE);
        } else {
            layoutViewMode.setVisibility(View.VISIBLE);
            layoutEditMode.setVisibility(View.GONE);
            btnEdit.setVisibility(View.VISIBLE);
            btnSave.setVisibility(View.GONE);
        }
    }

    private void updateEmployee() {
        currentEmployee.setName(etName.getText().toString());
        currentEmployee.setDepartment(etDepartment.getText().toString());
        currentEmployee.setPosition(etPosition.getText().toString());
        try {
            currentEmployee.setSalary(Double.parseDouble(etSalary.getText().toString()));
        } catch (NumberFormatException e) {
            currentEmployee.setSalary(0);
        }
        currentEmployee.setEmail(etEmail.getText().toString());
        currentEmployee.setPhone(etPhone.getText().toString());

        mViewModel.updateEmployee(currentEmployee);
        Toast.makeText(this, "Updated successfully", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void confirmDelete() {
        new AlertDialog.Builder(this)
                .setTitle("Delete Employee")
                .setMessage("Are you sure you want to delete this employee?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    mViewModel.deleteEmployee(currentEmployee.getId());
                    Toast.makeText(this, "Deleted successfully", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .setNegativeButton("No", null)
                .show();
    }
}
