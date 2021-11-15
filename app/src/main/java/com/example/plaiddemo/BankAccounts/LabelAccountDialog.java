package com.example.plaiddemo.BankAccounts;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.plaiddemo.R;

public class LabelAccountDialog extends DialogFragment {
    private final String access_token;
    private final String item_id;
    private BankAccountViewModel bankAccountVM;

    public LabelAccountDialog(String access_token, String item_id) {
        this.access_token = access_token;
        this.item_id = item_id;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.label_new_account, null);
        TextView label = view.findViewById(R.id.label_account);
        bankAccountVM = new ViewModelProvider(requireActivity()).get(BankAccountViewModel.class);
        builder.setView(view)
                .setPositiveButton(R.string.add_account, (dialog, id) -> bankAccountVM.insertBankAccount(new BankAccount(item_id,access_token,label.getText().toString())))
                .setNegativeButton(R.string.cancel, (dialog, id) -> dialog.cancel());
        return builder.create();
    }
}
