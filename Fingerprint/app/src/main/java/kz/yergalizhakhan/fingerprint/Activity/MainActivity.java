package kz.yergalizhakhan.fingerprint.Activity;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import kz.yergalizhakhan.fingerprint.Adapter.AccountAdapter;
import kz.yergalizhakhan.fingerprint.Entity.Account;
import kz.yergalizhakhan.fingerprint.R;
import kz.yergalizhakhan.fingerprint.SQLite.ORM.AccountORM;
import kz.yergalizhakhan.fingerprint.Support.RecyclerTouchListener;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    // Init ui elements
    @BindView(R.id.listSwipeRefreshLayout)
    SwipeRefreshLayout listSwipeRefreshLayout;
    @BindView(R.id.listRecyclerView)
    RecyclerView listRecyclerView;
    @BindView(R.id.addAccountFab)
    FloatingActionButton addAccount;

    // Variables
    AccountORM a = new AccountORM();
    List<Account> accountList;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        listSwipeRefreshLayout.setOnRefreshListener(this);

        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        listRecyclerView.setLayoutManager(layoutManager);
        getData();

        listRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(),
                listRecyclerView,
                new RecyclerTouchListener.ClickListener() {
                    @Override
                    public void onClick(View view, int position) {

                    }
                    @Override
                    public void onLongClick(View view, final int position) {
                        final Dialog dialog = new Dialog(view.getContext());
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setCancelable(true);
                        dialog.setContentView(R.layout.custom_dialog_notice_cancelable);

                        View v = dialog.getWindow().getDecorView();
                        v.setBackgroundResource(android.R.color.transparent);

                        Button cancel = (Button) dialog.findViewById(R.id.cancelButton);
                        Button delete = (Button) dialog.findViewById(R.id.deleteButton);

                        delete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                a.deleteGood(view.getContext(), accountList.get(position).getId());
                                dialog.dismiss();
                            }
                        });
                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                    }
                }));

        addAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(view.getContext());
                View sheetView = LayoutInflater.from(view.getContext()).inflate(R.layout.custom_add_account_dialog, null);
                mBottomSheetDialog.setContentView(sheetView);
                mBottomSheetDialog.setCancelable(true);

                final EditText title = (EditText) mBottomSheetDialog.findViewById(R.id.titleEditText);
                final EditText login = (EditText) mBottomSheetDialog.findViewById(R.id.loginEditText);
                final EditText pass = (EditText) mBottomSheetDialog.findViewById(R.id.passEditText);

                Button saveButton = (Button) mBottomSheetDialog.findViewById(R.id.addAccountButton);
                saveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Account acc = new Account();
                        acc.setTitle(title.getText().toString().trim());
                        acc.setLogin(login.getText().toString().trim());
                        acc.setPassword(pass.getText().toString().trim());
                        Log.e("Hello world ", login.getText().toString().trim() + " " + pass.getText().toString().trim());
                        a.add(view.getContext(), acc);
                        mBottomSheetDialog.dismiss();
                    }
                });

                mBottomSheetDialog.show();
            }
        });
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Отменяем анимацию обновления
                getData();
                listSwipeRefreshLayout.setRefreshing(false);
            }
        }, 2000);
    }

    private void getData() {
        accountList = a.getAll(getApplicationContext());
        adapter = new AccountAdapter(accountList);
        listRecyclerView.setAdapter(adapter);
    }

}
