package legear.colm.ulalarmclock;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewParent;
import android.widget.TimePicker;

/**
 * Created by colml on 28/04/2017.
 * Custom time picker widget so the scrollview does not interfere with time selection.
 * Some code taken from  https://stackoverflow.com/questions/14354006/scrollview-fighting-with-scroll-of-timepicker-timepicker-not-scrolling-as-a-res
 */

public class CustomTimePicker extends TimePicker {

    public CustomTimePicker(Context context)
    {
        super(context);
    }

    public CustomTimePicker(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public CustomTimePicker(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }


    //https://stackoverflow.com/questions/14354006/scrollview-fighting-with-scroll-of-timepicker-timepicker-not-scrolling-as-a-res
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        // Stop ScrollView from getting involved once you interact with the View
        if (ev.getActionMasked() == MotionEvent.ACTION_DOWN) {
            ViewParent p = getParent();
            if (p != null)
                p.requestDisallowInterceptTouchEvent(true);
        }
        return false;
    }
}
