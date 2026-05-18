package com.example.myapplication.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication.R;
import com.example.myapplication.model.Employee;

public class ExampleFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = ExampleFragment.class.getName();
    private String mName, mPart, mPos;
    private TextView mTxtName, mTxtPart, mTxtPosition;
    private EditText mEdtSalary;
    private Button mBtnSend;

    public static ExampleFragment newInstance(Employee employee) {
        ExampleFragment fragment = new ExampleFragment();
        Bundle bundle = new Bundle();
        bundle.putString("emp_name", employee.getName());
        bundle.putString("emp_part", employee.getDepartment());
        bundle.putString("emp_pos", employee.getPosition());
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_example, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mName = getArguments().getString("emp_name");
            mPart = getArguments().getString("emp_part");
            mPos = getArguments().getString("emp_pos");
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTxtName = view.findViewById(R.id.txt_name);
        mTxtPart = view.findViewById(R.id.txt_part);
        mTxtPosition = view.findViewById(R.id.txt_position);
        mEdtSalary = view.findViewById(R.id.txt_salary);
        mBtnSend = view.findViewById(R.id.btn_send);

        mTxtName.setText(mName);
        mTxtPart.setText(mPart);
        mTxtPosition.setText(mPos);

        mBtnSend.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        if (v.getId() == R.id.btn_send) {
            // Hide keyboard
            InputMethodManager imm = (InputMethodManager) requireContext()
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null && getView() != null) {
                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
            }

            // Get data
            String salaryStr = mEdtSalary.getText().toString().trim();

            if (salaryStr.isEmpty()) {
                mEdtSalary.setError("Salary is required!");
                return;
            }
            Employee employee = new Employee();
            employee.setName(mName);
            employee.setDepartment(mPart);
            employee.setPosition(mPos);
//            employee.SetSalary(Double.parseDouble(mEdtSalary.getText().toString().trim()));
            employee.setSalary(Double.parseDouble(salaryStr));
            Example2Fragment fragment = Example2Fragment.newInstance(employee);
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack("Example2Fragment")
                    .commit();
        }
    }
}
