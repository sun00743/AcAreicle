/*
 * Created mika <sun00743@gmail.com>
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package mika.com.android.ac.account.ui;

import android.accounts.Account;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import mika.com.android.ac.account.util.AccountUtils;
import mika.com.android.ac.ui.SimpleDialogFragment;

public class SelectAccountActivity extends AppCompatActivity
        implements SimpleDialogFragment.SimpleDialogListenerProvider {

    public static final String EXTRA_ON_SELECTED_INTENT = SelectAccountActivity.class.getName()
            + ".on_selected_intent";

    private SimpleDialogFragment.SimpleDialogListener mDialogListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Account[] accounts = AccountUtils.getAccounts(this);
        int numAccounts = accounts.length;
        String[] accountNames = new String[numAccounts];
        for (int i = 0; i < numAccounts; ++i) {
            accountNames[i] = accounts[i].name;
        }

        mDialogListener = new SimpleDialogFragment.SimpleDialogListener() {
            @Override
            public void onSingleChoiceItemClicked(int requestCode, int index) {
                AccountUtils.setActiveAccount(accounts[index], SelectAccountActivity.this);
                // Calling finish() before startActivity() makes it work when the Intent is a
                // launcher one.
                finish();
                Intent onSelectedIntent = getIntent().getParcelableExtra(EXTRA_ON_SELECTED_INTENT);
                startActivity(onSelectedIntent);
            }
            @Override
            public void onNegativeButtonClicked(int requestCode) {
                onCancel(requestCode);
            }
            @Override
            public void onCancel(int requestCode) {
                finish();
            }
        };

        if (savedInstanceState == null) {
            SimpleDialogFragment.makeSingleChoice(mika.com.android.ac.R.string.auth_select_account, accountNames, -1,
                    this)
                    .show(this);
        }
    }

    @Override
    public SimpleDialogFragment.SimpleDialogListener getDialogListener() {
        return mDialogListener;
    }
}
