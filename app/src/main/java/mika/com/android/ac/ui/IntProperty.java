/*
 * Created mika <sun00743@gmail.com>
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package mika.com.android.ac.ui;

import android.util.Property;

/**
 * From {@code android.util.IntProperty}.
 *
 * An implementation of {@link Property} to be used specifically with fields of type
 * <code>int</code>. This type-specific subclass enables performance benefit by allowing
 * calls to a {@link #set(Object, Integer) set()} function that takes the primitive
 * <code>int</code> type and avoids autoboxing and other overhead associated with the
 * <code>Integer</code> class.
 *
 * @param <T> The class on which the Property is declared.
 */
public abstract class IntProperty<T> extends Property<T, Integer> {

    public IntProperty(String name) {
        super(Integer.class, name);
    }

    /**
     * A type-specific override of the {@link #set(Object, Integer)} that is faster when dealing
     * with fields of type <code>int</code>.
     */
    public abstract void setValue(T object, int value);

    @Override
    final public void set(T object, Integer value) {
        setValue(object, value);
    }
}
