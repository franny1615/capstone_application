package com.example.plaiddemo.Transaction;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "transactions_table")
public class Transactions {
    @PrimaryKey(autoGenerate = true)
    private long tran_id;
    private final String item_id; // associates a transaction with an account
    private final String date;
    private final String category;
    private final String merchant;
    private final double amount;

    public Transactions(String item_id, String date, String category, String merchant, double amount) {
        this.item_id = item_id;
        this.date = date;
        this.category = category;
        this.merchant = merchant;
        this.amount = amount;
    }

    public long getTran_id() { return tran_id; }

    public void setTran_id(long id) { tran_id = id; }

    public String getDate() {
        return date;
    }

    public String getCategory() {
        return category;
    }

    public String getMerchant() {
        return merchant;
    }

    public double getAmount() {
        return amount;
    }

    public String getItem_id() { return item_id; }
}
