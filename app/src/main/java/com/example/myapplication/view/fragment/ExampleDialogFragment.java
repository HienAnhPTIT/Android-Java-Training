package com.example.myapplication.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.myapplication.R;

public class ExampleDialogFragment extends DialogFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dialog_example, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setLayout(
                (int) (getResources().getDisplayMetrics().widthPixels / 1.2),
                (int) (getResources().getDisplayMetrics().heightPixels / 2.5));
    }
}
