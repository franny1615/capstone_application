package com.example.plaiddemo;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.plaiddemo.BankAccounts.BankAccountFragment;
import com.example.plaiddemo.Transaction.TransactionsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        BottomNavigationView nav = findViewById(R.id.navigation_view);
        nav.setOnItemSelectedListener(item -> {
            String selected = item.getTitle().toString();
            FragmentTransaction transaction2 = manager.beginTransaction();
            if(selected.equals(getResources().getString(R.string.transactions))){
                transaction2.replace(R.id.fragment_container_main, TransactionsFragment.class,null);
                transaction2.commit();
            }
            else if(selected.equals(getResources().getString(R.string.accounts))) {
                transaction2.replace(R.id.fragment_container_main, BankAccountFragment.class, null);
                transaction2.commit();
            }
            return false;
        });
        // default fragment to be seen
        transaction.replace(R.id.fragment_container_main, BankAccountFragment.class,null);
        transaction.commit();
    }
}