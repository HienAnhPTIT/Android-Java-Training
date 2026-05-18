package com.example.myapplication.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication.R;
import com.example.myapplication.model.Employee;
import com.google.android.material.textfield.TextInputEditText;

public class AddEmpFragment extends Fragment {

    private TextInputEditText mEdtName, mEdtDept, mEdtPos, mEdtSal, mEdtEmail, mEdtPhone;
    private Button mBtnSave;

    public AddEmpFragment() {
        // Required empty public constructor
    }

    public static AddEmpFragment newInstance() {
        return new AddEmpFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_emp, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mEdtName = view.findViewById(R.id.etName);
        mEdtDept = view.findViewById(R.id.etDept);
        mEdtPos = view.findViewById(R.id.etPos);
        mEdtSal = view.findViewById(R.id.etSal);
        mEdtEmail = view.findViewById(R.id.etEmail);
        mEdtPhone = view.findViewById(R.id.etPhone);
        mBtnSave = view.findViewById(R.id.btnSave);

        mBtnSave.setOnClickListener(v -> {
            String name = mEdtName.getText().toString().trim();
            String dept = mEdtDept.getText().toString().trim();
            String pos = mEdtPos.getText().toString().trim();
            String salStr = mEdtSal.getText().toString().trim();
            String email = mEdtEmail.getText().toString().trim();
            String phone = mEdtPhone.getText().toString().trim();

            if (name.isEmpty() || dept.isEmpty() || pos.isEmpty() || salStr.isEmpty()) {
                Toast.makeText(getContext(), "Please fill all required fields", Toast.LENGTH_SHORT).show();
                return;
            }

            double salary;
            try {
                salary = Double.parseDouble(salStr);
            } catch (NumberFormatException e) {
                salary = 0;
            }

            Employee employee = new Employee();
            employee.setName(name);
            employee.setDepartment(dept);
            employee.setPosition(pos);
            employee.setSalary(salary);
            employee.setEmail(email);
            employee.setPhone(phone);

            Bundle bundle = new Bundle();
            bundle.putSerializable("emp", employee);
            getParentFragmentManager().setFragmentResult("add_emp_result", bundle);
            getParentFragmentManager().popBackStack();
        });
    }
}
