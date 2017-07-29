package com.codepath.gogreen;

import android.content.Context;
import android.text.format.DateUtils;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by anyazhang on 7/28/17.
 */

public class TimeStampUtils {

    public static String getRelativeTimeAgo(Date date) {
        long dateMillis = date.getTime();
        long currentMillis = new Date().getTime();

        if (currentMillis < dateMillis) {
            currentMillis = dateMillis;
        }

        String relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,

                currentMillis, DateUtils.SECOND_IN_MILLIS).toString();

        return relativeDate;
    }

    public static String shortenTimeStamp(String timestamp, Context context) {
        String[] splitTime = timestamp.trim().split("\\s+");
        List<String> times = Arrays.asList("second", "seconds", "minute", "minutes", "hour", "hours", "day", "days", "week", "weeks");
        // deal with recent tweets of form "# _ ago"
        if (splitTime.length > 1 && times.contains(splitTime[1])) {
            timestamp = splitTime[0] + splitTime[1].charAt(0);
        }
        // deal with old tweets of form M D, Y
        else if (splitTime.length > 2 && splitTime[2].equals(context.getString(R.string.current_year))) {
            timestamp = splitTime[0] + " " + splitTime[1].substring(0, splitTime[1].length() - 1);
        }
        else if (splitTime[0].equals("Yesterday")) {
            timestamp = "1d";
        }
        return timestamp;
    }

}
