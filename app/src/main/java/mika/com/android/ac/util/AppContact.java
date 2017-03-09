package mika.com.android.ac.util;

/**
 * Created by mika on 2017/3/9
 */

public interface AppContact {

    interface RequestCode{
        int REQUEST_CODE_SIGN_IN = 7;
        int REQUEST_CODE_QUOTE = 8;
    }

    interface ItemTag{
        int ITEM_HEAD = 0x124;
        int ITEM_ARTICLE = 0x123;
        int ITEM_SUBTITLE = 0x125;
    }
}
