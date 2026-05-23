package com.example.myapplication.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.controller.OnItemClickListener;
import com.example.myapplication.model.Employee;

import java.util.ArrayList;
import java.util.List;

public class EmployeeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<Employee> mList;
    private OnItemClickListener mOnItemClickListener;

    public EmployeeAdapter(Context context, List<Employee> list, OnItemClickListener onItemClickListener){
        mContext = context;
        mList = list;
        mOnItemClickListener = onItemClickListener;
    }

    public void setList(List<Employee> list){
        if(list != null){
            mList = new ArrayList<>(list);
            notifyDataSetChanged();
        }
    }

    public void removeItem(int pos){
        if (pos > -1 && pos < mList.size()){
            mList.remove(pos);
            notifyItemRemoved(pos);
        }
    }

    public void addItem(Employee employee){
        mList.add(employee);
        notifyItemInserted(mList.size());
    }

    public int getPositionForSection(char section) {
        for (int i = 0; i < getItemCount(); i++) {
            String name = mList.get(i).getName();
            if (name != null && !name.isEmpty()) {
                char firstChar = name.toUpperCase().charAt(0);
                if (firstChar == section) {
                    return i;
                }
            }
        }
        return -1;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_recycler_emplyee, parent, false);
        return new StepViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final StepViewHolder stepViewHolder = (StepViewHolder) holder;
        Employee emp = mList.get(position);

        stepViewHolder.mTxtName.setText(emp.getName());
        stepViewHolder.mTxtPart.setText(emp.getDepartment());
        stepViewHolder.mTxtPos.setText(emp.getPosition());
        stepViewHolder.mLayoutItem.setOnClickListener(v -> {
            int pos = stepViewHolder.getBindingAdapterPosition();
            mOnItemClickListener.clickItem(pos);
        });
        stepViewHolder.mLayoutItem.setOnLongClickListener(v -> {
            int pos = stepViewHolder.getBindingAdapterPosition();
            mOnItemClickListener.deleteItem(pos, mList.get(pos).getName());
            return false;
        });
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }
    static class StepViewHolder extends RecyclerView.ViewHolder {
        TextView mTxtName, mTxtPart, mTxtPos;
        ConstraintLayout mLayoutItem;

        public StepViewHolder(@NonNull View itemView) {
            super(itemView);
            mLayoutItem = itemView.findViewById(R.id.layout_item);
            mTxtName = itemView.findViewById(R.id.txt_name);
            mTxtPart = itemView.findViewById(R.id.txt_department);
            mTxtPos = itemView.findViewById(R.id.txt_position);
        }
    }
}
