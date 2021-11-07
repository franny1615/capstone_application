package com.example.plaiddemo.Transaction;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.plaiddemo.BankAccounts.BankAccount;
import com.example.plaiddemo.BankAccounts.BankAccountViewModel;
import com.example.plaiddemo.Networking.InteractionResult;
import com.example.plaiddemo.Networking.PlaidLinkInteractions;
import com.example.plaiddemo.R;
import org.json.JSONArray;

import java.util.List;

public class TransactionsFragment extends Fragment {
    private TransactionsViewModel transactionsViewModel;

    public TransactionsFragment() {
        super(R.layout.transactions_list);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.transactions_list,container,false);
        BankAccountViewModel bankAccountVM = new ViewModelProvider(requireActivity()).get(BankAccountViewModel.class);
        NumberPicker picker = view.findViewById(R.id.account_selection);
        List<BankAccount> accounts = bankAccountVM.getAllAccountsRaw();
        boolean wasAccounts = initNumberPicker(picker, accounts);
        if (wasAccounts && (accounts.size() > 0)) {
            //
            transactionsViewModel = new ViewModelProvider(requireActivity()).get(TransactionsViewModel.class);
            //
            RecyclerView transactionsRV = view.findViewById(R.id.transactions_recyclerview);
            transactionsRV.setLayoutManager(new LinearLayoutManager(getContext()));
            TransactionAdapter transactionAdapter = new TransactionAdapter();
            // get transactions on value picked
            picker.setOnValueChangedListener((picker1, oldVal, newVal) -> {
                String n = picker1.getDisplayedValues()[newVal];
                transactionAdapter.setTransactionData(transactionsViewModel.getAllTransactionsByItemId(n));
            });
            // display the transactions
            transactionsRV.setAdapter(transactionAdapter);
            transactionsRV.setHasFixedSize(true);
        }
        return view;
    }

    private boolean initNumberPicker(NumberPicker picker, List<BankAccount> accounts) {
        picker.setMinValue(0);
        if(accounts.size() == 0) {
            picker.setDisplayedValues(new String[]{"Add Accounts for Transaction Data"});
            picker.setMaxValue(0);
            return false;
        }
        picker.setMaxValue(accounts.size()-1);
        String[] data = new String[accounts.size()];
        for (int i = 0; i < accounts.size(); i++) {
            data[i] = accounts.get(i).getAccount_name(); // item_id really
        }
        picker.setDisplayedValues(data);
        return true;
    }
}
