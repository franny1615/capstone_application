package com.example.plaiddemo.Transaction;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import java.util.List;

@Dao
public interface TransactionsDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertTransaction(Transactions t);

    @Query("DELETE FROM transactions_table WHERE item_id = :item_id")
    int deleteTransactionsByItemId(String item_id);

    @Query("SELECT * FROM transactions_table WHERE item_id = :item_id")
    List<Transactions> loadAllTransactionsByItemId(String item_id);
}
