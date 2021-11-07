package com.example.plaiddemo;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.Transaction;

import com.example.plaiddemo.BankAccounts.BankAccount;
import com.example.plaiddemo.BankAccounts.BankAccountDAO;
import com.example.plaiddemo.Transaction.Transactions;
import com.example.plaiddemo.Transaction.TransactionsDAO;

@Database(entities = {BankAccount.class, Transactions.class}, version = 1)
public abstract class AccountInfoDatabase extends RoomDatabase {
    private static final String DB_NAME = "account_info_db";

    // you will populate this only once
    private static AccountInfoDatabase INSTANCE = null;

    public static AccountInfoDatabase getInstance(Context context) {
        // basically make the database if it doesn't already exist
        if(INSTANCE == null) {
            synchronized (AccountInfoDatabase.class) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        AccountInfoDatabase.class,
                        DB_NAME
                ).build();
            }
        }
        return INSTANCE;
    }

    // write these abstract methods to expose Data Access Objects
    public abstract BankAccountDAO bankAccountDao();
    public abstract TransactionsDAO transactionsDao();
}
