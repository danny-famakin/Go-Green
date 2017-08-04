package com.codepath.gogreen;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;

/**
 * Created by anyazhang on 8/3/17.
 */

public class PointFormatter implements IValueFormatter {
    private DecimalFormat mFormat;

    public PointFormatter() {
        mFormat = new DecimalFormat("###,###,##0.00");
    }

    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
        return mFormat.format(value) + " pts"; // e.g. append a dollar-sign
    }
}
