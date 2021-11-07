package com.example.plaiddemo.Transaction;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.plaiddemo.AccountInfoDatabase;
import com.example.plaiddemo.BankAccounts.BankAccount;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class TransactionsViewModel extends AndroidViewModel {
    private final TransactionsDAO transactionsDao;
    private final ExecutorService executorService;

    public TransactionsViewModel(@NonNull @NotNull Application application) {
        super(application);
        AccountInfoDatabase db = AccountInfoDatabase.getInstance(application);
        this.transactionsDao = db.transactionsDao();
        this.executorService = Executors.newSingleThreadExecutor();
    }

    public void insertTransaction(Transactions t) {
        executorService.execute(()->transactionsDao.insertTransaction(t));
    }

    public void deleteAllTransactionsByItemId(String item_id){
        executorService.execute(()->transactionsDao.deleteTransactionsByItemId(item_id));
    }

    public List<Transactions> getAllTransactionsByItemId(String item_id){
        Future<List<Transactions>> f = executorService.submit(()->transactionsDao.loadAllTransactionsByItemId(item_id));
        List<Transactions> b = null;
        try {
            b = f.get(200, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return b;
    }
}
