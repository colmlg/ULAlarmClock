package legear.colm.ulalarmclock;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by colml on 03/04/2017.
 */

public class AlarmAdapter extends ArrayAdapter<Alarm>{


    public AlarmAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<Alarm> objects) {
        super(context, resource, objects);
    }


}
