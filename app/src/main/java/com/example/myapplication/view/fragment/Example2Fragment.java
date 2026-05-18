package com.example.myapplication.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication.R;
import com.example.myapplication.model.Employee;

public class Example2Fragment extends Fragment {
    private String mName, mPart, mPos, mSalary;
    private TextView mTxtName, mTxtPart, mTxtPos, mTxtSalary;

    public static Example2Fragment newInstance(Employee employee) {
        Example2Fragment fragment = new Example2Fragment();
        Bundle bundle = new Bundle();
        bundle.putString("emp_name", employee.getName());
        bundle.putString("emp_part", employee.getDepartment());
        bundle.putString("emp_pos", employee.getPosition());
        bundle.putString("emp_salary", String.valueOf(employee.getSalary()));
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mName = getArguments().getString("emp_name");
            mPart = getArguments().getString("emp_part");
            mPos = getArguments().getString("emp_pos");
            mSalary = getArguments().getString("emp_salary");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_example2, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTxtName = view.findViewById(R.id.txt_name);
        mTxtPart = view.findViewById(R.id.txt_part);
        mTxtPos = view.findViewById(R.id.txt_position);
        mTxtSalary = view.findViewById(R.id.txt_salary);

        mTxtName.setText(mName);
        mTxtPart.setText(mPart);
        mTxtPos.setText(mPos);
        mTxtSalary.setText(mSalary);
    }
}
