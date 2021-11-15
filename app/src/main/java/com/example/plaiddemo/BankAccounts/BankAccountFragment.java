package com.example.plaiddemo.BankAccounts;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.activity.result.ActivityResultLauncher;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.plaiddemo.Networking.InteractionResult;
import com.example.plaiddemo.Networking.PlaidLinkInteractions;
import com.example.plaiddemo.R;
import com.example.plaiddemo.Transaction.Transactions;
import com.example.plaiddemo.Transaction.TransactionsViewModel;
import com.plaid.link.OpenPlaidLink;
import com.plaid.link.configuration.LinkTokenConfiguration;
import com.plaid.link.result.LinkSuccess;
import org.json.JSONArray;
import org.json.JSONObject;

public class BankAccountFragment extends Fragment {
    private InteractionResult plaidResults = null;
    private PlaidLinkInteractions plaidRequests;
    private BankAccountViewModel bankAccountVM;

    public BankAccountFragment() {
        super(R.layout.bank_account_list);
    }

    private void initializePlaidResults() {
        plaidResults = new InteractionResult() {
            @Override
            public void createdLinkToken(String public_token) {
                LinkTokenConfiguration config = new LinkTokenConfiguration.Builder().token(public_token).build();
                linkAccountToPlaid.launch(config);
            }
            @Override
            public void exchangedPublicToken(String access_token, String item_id) {
                LabelAccountDialog d = new LabelAccountDialog(access_token,item_id);
                d.show(requireActivity().getSupportFragmentManager(),"labelAccount");
                if (bankAccountVM.getByItemId(item_id) != null) {
                    plaidRequests.getTransactions(access_token,item_id);
                }
            }
            @Override
            public void retrievedTransactions(JSONArray transactions, String itemId) { }
            @Override
            public void notifyError(Exception error) { Log.d("ERROR::::",error.getMessage()); }
        };
    }

    private final ActivityResultLauncher<LinkTokenConfiguration> linkAccountToPlaid = registerForActivityResult(new OpenPlaidLink(),
            result -> {
                if (result instanceof LinkSuccess) {
                    LinkSuccess res = (LinkSuccess) result;
                    plaidRequests.exchangePublicToken(res.getPublicToken());
                } // there is an instanceof LinkExit that can be handled
            }
    );

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        bankAccountVM = new ViewModelProvider(requireActivity()).get(BankAccountViewModel.class);
        BankAccountAdapter bankAccountAdapter = new BankAccountAdapter(requireActivity());
        bankAccountVM.getAllAccounts().observe(getViewLifecycleOwner(), bankAccountAdapter::setData);
        initializePlaidResults();
        //
        View view = inflater.inflate(R.layout.bank_account_list,container,false);
        plaidRequests = new PlaidLinkInteractions(plaidResults,getContext());
        RecyclerView bankAccountsRecyclerView = view.findViewById(R.id.bank_account_reclycer_view);
        bankAccountsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        bankAccountsRecyclerView.setAdapter(bankAccountAdapter);
        bankAccountsRecyclerView.setHasFixedSize(true);
        Button btn = view.findViewById(R.id.add_bankaccount_button);
        btn.setOnClickListener(v -> plaidRequests.createLinkToken());
        return view;
    }
}
