/*
 * Created mika <sun00743@gmail.com>
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package mika.com.android.ac.settings.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import mika.com.android.ac.BuildConfig;
import mika.com.android.ac.scalpel.ScalpelHelperFragment;
import mika.com.android.ac.ui.KonamiCodeDetector;

public class AboutFragment extends Fragment implements ConfirmEnableScalpelDialogFragment.Listener {

    @BindView(mika.com.android.ac.R.id.container)
    LinearLayout mContainerLayout;
    @BindView(mika.com.android.ac.R.id.toolbar)
    Toolbar mToolbar;
    @BindView(mika.com.android.ac.R.id.version)
    TextView mVersionText;
    @BindView(mika.com.android.ac.R.id.douban)
    Button mDoubanButton;

    public static AboutFragment newInstance() {
        //noinspection deprecation
        return new AboutFragment();
    }

    /**
     * @deprecated Use {@link #newInstance()} instead.
     */
    public AboutFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(mika.com.android.ac.R.layout.about_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ScalpelHelperFragment.attachTo(this);

        final AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(mToolbar);
        activity.getSupportActionBar().setTitle(null);

        // Seems that ScrollView intercepts touch event, so we have to set the onTouchListener on a
        // view inside it.
        mContainerLayout.setOnTouchListener(new KonamiCodeDetector(activity) {
            @Override
            public void onDetected() {
                onEnableScalpel();
            }
        });

        mVersionText.setText(getString(mika.com.android.ac.R.string.about_version_format, BuildConfig.VERSION_NAME));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void onEnableScalpel() {
        ConfirmEnableScalpelDialogFragment.show(this);
    }

    @Override
    public void enableScalpel() {
        ScalpelHelperFragment.setEnabled(true);
    }
}
