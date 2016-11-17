/*
 * Created mika <sun00743@gmail.com>
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package mika.com.android.ac.content;

import android.accounts.Account;
import android.os.Bundle;

import mika.com.android.ac.account.util.AccountUtils;
import mika.com.android.ac.app.TargetedRetainedFragment;
import mika.com.android.ac.util.FragmentUtils;

public class ResourceFragment extends TargetedRetainedFragment {

    // Not static because we are to be subclassed.
    protected final String KEY_PREFIX = getClass().getName() + '.';

    private final String EXTRA_ACCOUNT = KEY_PREFIX + "account";

    private Account mAccount;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arguments = FragmentUtils.ensureArguments(this);
        if (arguments.containsKey(EXTRA_ACCOUNT)) {
            mAccount = arguments.getParcelable(EXTRA_ACCOUNT);
        } else {
            mAccount = AccountUtils.getActiveAccount(getContext());
        }
    }

    // TODO
}
