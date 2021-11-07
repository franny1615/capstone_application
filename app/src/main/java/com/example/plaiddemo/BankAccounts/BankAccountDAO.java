package com.example.plaiddemo.BankAccounts;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface BankAccountDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertBA(BankAccount ba);

    @Update
    int updateBA(BankAccount ba);

    @Delete
    int deleteBA(BankAccount ba);

    @Query("SELECT * FROM bank_account_table")
    LiveData<List<BankAccount>> loadAllBA();

    @Query("SELECT * FROM bank_account_table")
    List<BankAccount> loadAllBARaw();

    @Query("SELECT * FROM bank_account_table WHERE account_name=:itemId")
    BankAccount getByItemId(String itemId);

}
