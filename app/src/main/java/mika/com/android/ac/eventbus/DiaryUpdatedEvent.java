/*
 * Created mika <sun00743@gmail.com>
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package mika.com.android.ac.eventbus;

import mika.com.android.ac.network.api.info.frodo.Diary;

public class DiaryUpdatedEvent extends Event {

    public Diary diary;

    public DiaryUpdatedEvent(Diary diary, Object source) {
        super(source);

        this.diary = diary;
    }
}
