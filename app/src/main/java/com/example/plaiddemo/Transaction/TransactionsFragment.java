package com.example.plaiddemo.Transaction;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.Spinner;

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
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class TransactionsFragment extends Fragment {
    private TransactionsViewModel transactionsViewModel;
    private PlaidLinkInteractions plaidLinkInteractions;
    private InteractionResult     results;
    private JSONArray             transactions;
    private ProgressBar           progress;
    private TransactionAdapter    transactionAdapter;

    public TransactionsFragment() {
        super(R.layout.transactions_list);
        initResults();
    }

    private void initResults() {
        results = new InteractionResult() {
            @Override
            public void createdLinkToken(String publicToken) { }
            @Override
            public void exchangedPublicToken(String accessToken, String itemId) { }
            @Override
            public void retrievedTransactions(JSONArray response, String itemId) {
                transactions = response;
                progress.setVisibility(View.GONE);
                insertTransactions(itemId);
                transactionAdapter.setTransactionData(transactionsViewModel.getAllTransactionsByItemId(itemId));
            }
            @Override
            public void notifyError(Exception error) { Log.d("ERROR::::",error.getMessage()); }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        BankAccountViewModel bankAccountVM = new ViewModelProvider(requireActivity()).get(BankAccountViewModel.class);
        //
        View view = inflater.inflate(R.layout.transactions_list,container,false);
        progress = view.findViewById(R.id.loading_bar);
        Spinner picker = view.findViewById(R.id.account_selection);
        plaidLinkInteractions = new PlaidLinkInteractions(results,view.getContext());
        //
        List<BankAccount> accounts = bankAccountVM.getAllAccountsRaw();
        boolean wasAccounts = initSpinner(picker, accounts);
        if (wasAccounts && (accounts.size() > 0)) {
            // connection to db
            transactionsViewModel = new ViewModelProvider(requireActivity()).get(TransactionsViewModel.class);
            // set up the list
            RecyclerView transactionsRV = view.findViewById(R.id.transactions_recyclerview);
            transactionsRV.setLayoutManager(new LinearLayoutManager(getContext()));
            transactionAdapter = new TransactionAdapter();
            // get transactions on value picked
            picker.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    progress.setVisibility(View.VISIBLE);
                    String item_id = accounts.get(position).getAccount_name();
                    String access_token = accounts.get(position).getAccess_token();
                    List<Transactions> t = transactionsViewModel.getAllTransactionsByItemId(item_id);
                    if(t.size() > 0) {
                        transactionAdapter.setTransactionData(t);
                        progress.setVisibility(View.GONE);
                    } else {
                        plaidLinkInteractions.getTransactions(access_token, item_id);
                    }
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {}
            });
            // display the transactions
            transactionsRV.setAdapter(transactionAdapter);
            transactionsRV.setHasFixedSize(true);
        }
        return view;
    }

    private void insertTransactions(String itemId) {
        for(int i = 0; i < transactions.length(); i++){
            try {
                JSONObject item = transactions.getJSONObject(i);
                String categories = item.getJSONArray("category").toString();
                String merchant = item.getString("name");
                double amount = (double) item.get("amount");
                String date = item.getString("date");
                transactionsViewModel.insertTransaction(new Transactions(itemId,date,categories,merchant,amount));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private boolean initSpinner(Spinner picker, List<BankAccount> accounts) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.bank_account_array, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        picker.setAdapter(adapter);
        if(accounts.size() > 0) {
            ArrayAdapter<String> options = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item);
            for(BankAccount b : accounts) {
                options.add(b.getAccount_label());
            }
            picker.setAdapter(options);
            return true;
        }
        return false;
    }
}
