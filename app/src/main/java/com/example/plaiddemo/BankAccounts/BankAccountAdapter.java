package com.example.plaiddemo.BankAccounts;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import com.example.plaiddemo.R;
import com.example.plaiddemo.Transaction.TransactionsViewModel;

import java.util.List;

public class BankAccountAdapter extends RecyclerView.Adapter<BankAccountAdapter.ViewHolder> {
    private List<BankAccount> localDataSet;
    private final BankAccountViewModel bavm;
    private final TransactionsViewModel tvm;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView accountLabel;
        private final TextView accountId;
        private final Button deleteAccount;

        public ViewHolder(View view) {
            super(view);
            accountLabel = view.findViewById(R.id.bank_account_label);
            accountId = view.findViewById(R.id.bank_account_id);
            deleteAccount = view.findViewById(R.id.delete_account);
        }

        public TextView getAccountLabel() {return accountLabel;}
        public TextView getAccountId() {return accountId;}
        public Button getDeleteAccountButton() {return deleteAccount;}
    }

    public BankAccountAdapter(Context context) {
        bavm = new ViewModelProvider((FragmentActivity) context).get(BankAccountViewModel.class);
        tvm = new ViewModelProvider((FragmentActivity) context).get(TransactionsViewModel.class);
    }

    public void setData(List<BankAccount> newData) {
        this.localDataSet = newData;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BankAccountAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.bank_account_cardview, viewGroup, false);
        return new BankAccountAdapter.ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(BankAccountAdapter.ViewHolder viewHolder, final int position) {
        viewHolder.getAccountLabel().setText(localDataSet.get(position).getAccount_label());
        String id = "ID: " + localDataSet.get(position).get_id();
        viewHolder.getAccountId().setText(id);
        viewHolder.getDeleteAccountButton().setOnClickListener(v -> {
            bavm.deleteBankAccount(localDataSet.get(position));
            tvm.deleteAllTransactionsByItemId(localDataSet.get(position).getAccount_name());
        });
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
