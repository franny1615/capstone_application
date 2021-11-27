package com.example.plaiddemo.BankAccounts;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "bank_account_table")
public class BankAccount {
    @PrimaryKey(autoGenerate = true)
    private int _id;
    private final String account_name;
    private final String access_token;
    private final String account_label;

    public BankAccount(String account_name, String access_token, String account_label) {
        this.account_name = account_name;
        this.access_token = access_token;
        this.account_label = account_label;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getAccount_name() {
        return account_name;
    }

    public String getAccess_token() {
        return access_token;
    }

    public String getAccount_label() { return account_label; }
}
