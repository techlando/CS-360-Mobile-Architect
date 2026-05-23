package com.example.landon_phillips_first_app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class WeightAdapter extends RecyclerView.Adapter<WeightAdapter.WeightViewHolder> {

    private List<Weight> weightList;
    private OnItemActionListener listener;

    public interface OnItemActionListener {
        void onDelete(int weightId);
        void onUpdate(Weight weight);
    }

    public WeightAdapter(List<Weight> weightList, OnItemActionListener listener) {
        this.weightList = weightList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public WeightViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.person_weight, parent, false);
        return new WeightViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull WeightViewHolder holder, int position) {
        Weight weight = weightList.get(position);
        holder.tvWeight.setText(weight.weight + " lb");
        holder.tvDate.setText(weight.date);

        holder.btnDelete.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDelete(weight.id);
            }
        });

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onUpdate(weight);
            }
        });
    }

    @Override
    public int getItemCount() {
        return weightList.size();
    }

    public void updateList(List<Weight> newList) {
        this.weightList = newList;
        notifyDataSetChanged();
    }

    public static class WeightViewHolder extends RecyclerView.ViewHolder {
        TextView tvWeight, tvDate;
        ImageButton btnDelete;

        public WeightViewHolder(@NonNull View itemView) {
            super(itemView);
            tvWeight = itemView.findViewById(R.id.tvWeight);
            tvDate = itemView.findViewById(R.id.tvDate);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
