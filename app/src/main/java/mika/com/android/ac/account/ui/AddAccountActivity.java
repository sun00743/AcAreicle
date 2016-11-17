/*
 * Created mika <sun00743@gmail.com>
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package mika.com.android.ac.account.ui;

import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.io.IOException;

import mika.com.android.ac.account.util.AccountUtils;

public class AddAccountActivity extends AppCompatActivity {

    public static final String EXTRA_ON_ADDED_INTENT = AddAccountActivity.class.getName()
            + ".on_added_intent";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            AccountUtils.addAccount(this, new AccountManagerCallback<Bundle>() {
                @Override
                public void run(AccountManagerFuture<Bundle> future) {
                    finish();
                    try {
                        Bundle result = future.getResult();
                        if (result.containsKey(AccountManager.KEY_ACCOUNT_NAME)
                                && result.containsKey(AccountManager.KEY_ACCOUNT_TYPE)) {
                            // NOTE:
                            // Active account should have been set in
                            // AuthenticatorActivity.onAuthResult() since the mode should be
                            // AUTH_MODE_NEW.
                            Intent onAddedIntent = getIntent()
                                    .getParcelableExtra(EXTRA_ON_ADDED_INTENT);
                            startActivity(onAddedIntent);
                        }
                    } catch (AuthenticatorException | IOException | OperationCanceledException e) {
                        e.printStackTrace();
                    }
                }
            }, null);
        }
    }
}
