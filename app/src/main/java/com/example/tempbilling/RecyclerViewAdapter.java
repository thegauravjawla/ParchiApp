package com.example.tempbilling;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewViewHolder> {

    private List<Item> itemList;
    private OnItemListener mOnItemListener;
    FormatHelper format;

    public RecyclerViewAdapter(List<Item> itemList, OnItemListener onItemListener) {
        this.itemList = itemList;
        this.mOnItemListener = onItemListener;
        format = new FormatHelper();
    }

    @NonNull
    @Override
    //1st this is called
    public RecyclerViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item_layout, parent, false);
        //this is the 2nd thing that is being called
        return new RecyclerViewViewHolder(view, mOnItemListener);
    }

    @Override
    //3rd thing that will be called
    public void onBindViewHolder(@NonNull RecyclerViewViewHolder holder, final int position) {

        //setting data in the view at the given position
        holder.addedItemName.setText(itemList.get(position).getItemName());
        holder.addedRate.setText(format.rate(itemList.get(position).getRate()));
        holder.addedWeight.setText(format.weight(itemList.get(position).getWeight()));
        holder.addedQuantity.setText(Integer.toString(Math.round(itemList.get(position).getQuantity())));
        holder.isBundleSelected.setText(itemList.get(position).isBundleChecked()?"B":"Pc");
        holder.addedTotal.setText(format.money(itemList.get(position).getTotal()));
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    //2nd thing that will be called
    public class RecyclerViewViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView addedItemName, addedRate, addedWeight, addedQuantity, addedTotal, isBundleSelected;
        ImageButton btnRemove;
        OnItemListener onItemListener;
        public RecyclerViewViewHolder(@NonNull View itemView, OnItemListener onItemListener) {
            super(itemView);

            this.onItemListener = onItemListener;

            addedItemName = itemView.findViewById(R.id.addedItemName);
            addedRate = itemView.findViewById(R.id.addedRate);
            addedWeight = itemView.findViewById(R.id.addedWeight);
            addedQuantity = itemView.findViewById(R.id.addedQuantity);
            isBundleSelected = itemView.findViewById(R.id.isBundleSelected);
            addedTotal = itemView.findViewById(R.id.addedTotal);
            btnRemove = itemView.findViewById(R.id.btnRemove);

            //this refers to the interface that is implemented in this class
            btnRemove.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onItemListener.onItemClick((getAdapterPosition()));
        }
    }

    public interface OnItemListener {
        void onItemClick(int position);
    }
}
