package legear.colm.ulalarmclock;

import android.content.Context;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by colml on 26/04/2017.
 */

public class TimetableAlarmSetter {

    private Context context;
    public TimetableAlarmSetter(Context context)
    {
        this.context = context;
    }

    public boolean setAlarmsFromTimetable(int id) throws IOException
    {
        String urlString = "http://tt.daniel.ie/tt.php?id=" + id;
        Document doc = Jsoup.connect(urlString).get();
        Element containerDiv = doc.getElementById("container");
        Elements lectures = containerDiv.getElementsByTag("div");
        //Time of the first lecture, monday to friday
        String [] firstLectureTimes = {"", "", "", "", ""};
        for(Element lecture : lectures)
        {
            String styleString = lecture.attr("style");
            String [] styleSplit = styleString.split(";");
            String [] styleComponentSplit = {};
            int day = 0;
            for(String s : styleSplit)
            {
                if(s.contains("left"))
                {
                    styleComponentSplit = s.split(":");
                    day = Integer.parseInt(styleComponentSplit[1]);
                }
            }

        }







        return true;
    }
}
