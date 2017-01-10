/*
 * Created mika <sun00743@gmail.com>
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package mika.com.android.ac.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import java.util.ArrayList;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;
import me.zhanghai.android.systemuihelper.SystemUiHelper;
import mika.com.android.ac.R;
import mika.com.android.ac.network.api.info.apiv2.Image;

public class GalleryActivity extends AppCompatActivity {

    private static final String KEY_PREFIX = GalleryActivity.class.getSimpleName() + '.';

    public static final String EXTRA_IMAGE_LIST = KEY_PREFIX + "image_list";
    public static final String EXTRA_POSITION = KEY_PREFIX + "position";

    @BindInt(android.R.integer.config_mediumAnimTime)
    int mToolbarHideDuration;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.viewPager)
    ViewPager mViewPager;

    private SystemUiHelper mSystemUiHelper;

    public static Intent makeIntent(ArrayList<String> imageList, int position, Context context) {
        return new Intent(context, GalleryActivity.class)
                .putStringArrayListExtra(EXTRA_IMAGE_LIST, imageList)
                .putExtra(EXTRA_POSITION, position);
    }

    public static Intent makeImageListIntent(ArrayList<Image> imageList, int position,
                                             Context context) {
        ArrayList<String> imageUrlList = new ArrayList<>();
        for (Image image : imageList) {
            imageUrlList.add(image.getLargest());
        }
        return makeIntent(imageUrlList, position, context);
    }

    public static Intent makeIntent(String image, Context context) {
        ArrayList<String> imageList = new ArrayList<>();
        imageList.add(image);
        return makeIntent(imageList, 0, context);
    }

    public static Intent makeIntent(Image image, Context context) {
        return makeIntent(image.getLargest(), context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.gallery_activity);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);

        mSystemUiHelper = new SystemUiHelper(this, SystemUiHelper.LEVEL_IMMERSIVE,
                SystemUiHelper.FLAG_IMMERSIVE_STICKY,
                new SystemUiHelper.OnVisibilityChangeListener() {
                    @Override
                    public void onVisibilityChange(boolean visible) {
                        if (visible) {
                            mToolbar.animate()
                                    .alpha(1)
                                    .translationY(0)
                                    .setDuration(mToolbarHideDuration)
                                    .setInterpolator(new FastOutSlowInInterpolator())
                                    .start();
                        } else {
                            mToolbar.animate()
                                    .alpha(0)
                                    .translationY(-mToolbar.getBottom())
                                    .setDuration(mToolbarHideDuration)
                                    .setInterpolator(new FastOutSlowInInterpolator())
                                    .start();
                        }
                    }
                });
        // This will set up window flags.
        mSystemUiHelper.show();

        ArrayList<String> imageList = getIntent().getStringArrayListExtra(EXTRA_IMAGE_LIST);
        mViewPager.setAdapter(new GalleryAdapter(imageList, new GalleryAdapter.OnTapListener() {
            @Override
            public void onTap() {
                mSystemUiHelper.toggle();
            }
        }));
        int position = getIntent().getIntExtra(EXTRA_POSITION, 0);
        mViewPager.setCurrentItem(position);
        mViewPager.setPageTransformer(true, new ViewPagerTransformers.Depth());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
