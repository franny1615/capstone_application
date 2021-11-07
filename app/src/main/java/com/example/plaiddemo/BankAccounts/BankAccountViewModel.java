package com.example.plaiddemo.BankAccounts;
import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.plaiddemo.AccountInfoDatabase;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class BankAccountViewModel extends AndroidViewModel {
    private final BankAccountDAO bankAccountDao;
    private final ExecutorService executorService;

    public BankAccountViewModel(@NonNull Application application) {
        super(application);
        AccountInfoDatabase db = AccountInfoDatabase.getInstance(application);
        this.bankAccountDao = db.bankAccountDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public void insertBankAccount(BankAccount ba) {
        executorService.execute(()->bankAccountDao.insertBA(ba));
    }

    public void updateBankAccount(BankAccount ba) {
        executorService.execute(()->bankAccountDao.updateBA(ba));
    }

    public void deleteBankAccount(BankAccount ba) {
        executorService.execute(()->bankAccountDao.deleteBA(ba));
    }

    public BankAccount getByItemId(String itemId) {
        Future<BankAccount> f = executorService.submit(()->bankAccountDao.getByItemId(itemId));
        BankAccount b = null;
        try {
            b = f.get(200, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return b;
    }

    public LiveData<List<BankAccount>> getAllAccounts() {
        return bankAccountDao.loadAllBA();
    }

    public List<BankAccount> getAllAccountsRaw(){
        Future<List<BankAccount>> f = executorService.submit(bankAccountDao::loadAllBARaw);
        List<BankAccount> b = null;
        try {
            b = f.get(200, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return b;
    }
}
