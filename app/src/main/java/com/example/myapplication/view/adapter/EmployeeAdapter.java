package com.example.myapplication.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.controller.OnItemClickListener;
import com.example.myapplication.model.Employee;

import java.util.ArrayList;
import java.util.List;

public class EmployeeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {
    private Context mContext;
    private List<Employee> mList;
    private List<Employee> mListFull;
    private OnItemClickListener mOnItemClickListener;
    private boolean isGridLayout = false;

    public EmployeeAdapter(Context context, List<Employee> list, OnItemClickListener onItemClickListener){
        mContext = context;
        mList = list;
        mListFull = new ArrayList<>(list);
        mOnItemClickListener = onItemClickListener;
    }

    public void setGridLayout(boolean isGrid) {
        this.isGridLayout = isGrid;
        notifyDataSetChanged();
    }

    public void setList(List<Employee> list){
        if(list != null){
            mList = new ArrayList<>(list);
            mListFull = new ArrayList<>(list);
            notifyDataSetChanged();
        }
    }

    public void removeItem(int pos){
        if (pos > -1 && pos < mList.size()){
            Employee emp = mList.get(pos);
            mList.remove(pos);
            mListFull.remove(emp);
            notifyItemRemoved(pos);
        }
    }

    public void addItem(Employee employee){
        mList.add(employee);
        mListFull.add(employee);
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

        if (isGridLayout) {
            stepViewHolder.mTxtNameTitle.setVisibility(View.GONE);
            stepViewHolder.mTxtPartTitle.setVisibility(View.VISIBLE); 
            stepViewHolder.mTxtPosTitle.setVisibility(View.VISIBLE);
            stepViewHolder.mImgDetail.setVisibility(View.GONE);
            
            stepViewHolder.mTxtName.setGravity(android.view.Gravity.START);
            stepViewHolder.mTxtPart.setGravity(android.view.Gravity.START);
            stepViewHolder.mTxtPos.setGravity(android.view.Gravity.START);
            
            stepViewHolder.mTxtName.setTextSize(15);
        } else {
            stepViewHolder.mTxtNameTitle.setVisibility(View.VISIBLE);
            stepViewHolder.mTxtPartTitle.setVisibility(View.VISIBLE);
            stepViewHolder.mTxtPosTitle.setVisibility(View.VISIBLE);
            stepViewHolder.mImgDetail.setVisibility(View.VISIBLE);

            stepViewHolder.mTxtName.setGravity(android.view.Gravity.START);
            stepViewHolder.mTxtPart.setGravity(android.view.Gravity.START);
            stepViewHolder.mTxtPos.setGravity(android.view.Gravity.START);
            
            stepViewHolder.mTxtName.setTextSize(16);
        }

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

    @Override
    public Filter getFilter() {
        return employeeFilter;
    }

    private Filter employeeFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Employee> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(mListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Employee item : mListFull) {
                    if (item.getName().toLowerCase().contains(filterPattern) ||
                        item.getDepartment().toLowerCase().contains(filterPattern) ||
                        item.getPosition().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mList.clear();
            mList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    static class StepViewHolder extends RecyclerView.ViewHolder {
        TextView mTxtName, mTxtPart, mTxtPos;
        TextView mTxtNameTitle, mTxtPartTitle, mTxtPosTitle;
        android.widget.ImageView mImgDetail;
        ConstraintLayout mLayoutItem;

        public StepViewHolder(@NonNull View itemView) {
            super(itemView);
            mLayoutItem = itemView.findViewById(R.id.layout_item);
            mTxtName = itemView.findViewById(R.id.txt_name);
            mTxtPart = itemView.findViewById(R.id.txt_department);
            mTxtPos = itemView.findViewById(R.id.txt_position);
            mTxtNameTitle = itemView.findViewById(R.id.txt_name_title);
            mTxtPartTitle = itemView.findViewById(R.id.txt_part_title);
            mTxtPosTitle = itemView.findViewById(R.id.txt_pos_title);
            mImgDetail = itemView.findViewById(R.id.img_detail);
        }
    }
}
