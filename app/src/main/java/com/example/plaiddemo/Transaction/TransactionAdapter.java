package com.example.plaiddemo.Transaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.plaiddemo.R;

import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {
    private List<Transactions> localDataSet;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView date;
        private final TextView category;
        private final TextView merchant;
        private final TextView amount;

        public ViewHolder(View view) {
            super(view);
            date = view.findViewById(R.id.transaction_date_textview);
            category = view.findViewById(R.id.transaction_category_textview);
            merchant = view.findViewById(R.id.transaction_merchant_textview);
            amount = view.findViewById(R.id.transaction_amount_textview);
        }

        public TextView getDate() {return date;}
        public TextView getCategory() {return category;}
        public TextView getMerchant() {return merchant;}
        public TextView getAmount() {return amount;}
    }

    public void setTransactionData(List<Transactions> data) {
        localDataSet = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.trasaction_cardview, viewGroup, false);
        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        String amount = Double.toString(localDataSet.get(position).getAmount());
        viewHolder.getDate().setText(localDataSet.get(position).getDate());
        viewHolder.getCategory().setText(localDataSet.get(position).getCategory());
        viewHolder.getMerchant().setText(localDataSet.get(position).getMerchant());
        viewHolder.getAmount().setText(amount);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if (localDataSet == null) {
            return 0;
        }
        return localDataSet.size();
    }
}
