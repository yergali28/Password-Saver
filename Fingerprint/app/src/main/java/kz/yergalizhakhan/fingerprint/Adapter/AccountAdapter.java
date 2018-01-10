package kz.yergalizhakhan.fingerprint.Adapter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import kz.yergalizhakhan.fingerprint.Entity.Account;
import kz.yergalizhakhan.fingerprint.R;

/**
 * Created by zhakhanyergali on 08.11.17.
 */

public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.ViewHolder> {

    // Variables
    private List<Account> accountList;
    private Context context;

    public AccountAdapter(List<Account> accountList) {
        this.accountList = accountList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = null;

        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);

        ViewHolder vh = new ViewHolder(v);
        context = parent.getContext();
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Log.e("Hello world ", accountList.get(position).getLogin() + " " + accountList.get(position).getPassword());
        holder.title.setText((position + 1) + ". " + accountList.get(position).getTitle());
        holder.login.setText(accountList.get(position).getLogin());
        holder.password.setText(accountList.get(position).getPassword());
        holder.copyPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("", accountList.get(position).getPassword());
                clipboard.setPrimaryClip(clip);

                Toast.makeText(context, "Password copied to clipboard", Toast.LENGTH_SHORT).show();
            }
        });
        holder.copyLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("", accountList.get(position).getLogin());
                clipboard.setPrimaryClip(clip);

                Toast.makeText(context, "Login copied to clipboard", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return accountList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView title, login, password ;
        public ImageView copyLogin, copyPass;

        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.contextTextView);
            login = (TextView) itemView.findViewById(R.id.loginTextView);
            password = (TextView) itemView.findViewById(R.id.passwordTextView);
            copyLogin = (ImageView) itemView.findViewById(R.id.copyLoginImageView);
            copyPass = (ImageView) itemView.findViewById(R.id.copyPassImageView);
        }
    }

}
