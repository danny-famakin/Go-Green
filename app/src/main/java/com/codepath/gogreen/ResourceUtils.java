package com.codepath.gogreen;

import android.content.Context;
import android.graphics.Color;

import com.codepath.gogreen.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by anyazhang on 8/1/17.
 */

public class ResourceUtils {
    public static HashMap<String, Double> weights = new HashMap<>();
    public static HashMap<String, Integer[]> colors = new HashMap<>();
    public String[] resources = new String[] {"emissions", "fuel", "water", "trees"};
    private Context context;


    public ResourceUtils(Context context){
        this.context=context;
    }


    public HashMap<String, Double> getWeights() {
        weights.put("emissions", 2.);
        weights.put("fuel", 20.);
        weights.put("water", 0.1);
        weights.put("trees", 1.);
        return weights;
    }

    public void getColors() {
        colors.put("transit", new Integer[] {Color.parseColor("#679D4C"), Color.parseColor("#A8C799"), Color.parseColor("#C2D7B7"), Color.parseColor("#527D3C")});
        colors.put("water", new Integer[] {Color.parseColor("#B2E9DA"), Color.parseColor("#7FDAC2"), Color.parseColor("#4CCBA9"), context.getResources().getColor(R.color.colorAccentDark)});
        colors.put("reuse", new Integer[] {Color.parseColor("#B2C9E3"), Color.parseColor("#7FA5D1"), Color.parseColor("#4C81BE"), context.getResources().getColor(R.color.darkBlue)});
        colors.put("recycle", new Integer[] {Color.parseColor("#C6E369"), Color.parseColor("#ADD729"), Color.parseColor("#9CC124"), context.getResources().getColor(R.color.colorPrimary)});
    }


    public double sumPoints(Double[] constants) {
        getWeights();
        double sum = 0;
        for(int i = 0; i < resources.length; i++) {
            sum += constants[i] * weights.get(resources[i]);
        }
        return sum;
    }

    public double[] getResourcePoints(Double[] constants) {
        getWeights();
        double[] resourcePoints = new double[resources.length];
        for(int i = 0; i < resources.length; i++) {
            resourcePoints[i] = constants[i] * weights.get(resources[i]);
        }
        return resourcePoints;
    }

    public ArrayList<Integer> getColorArray(String resource, int size) {
        getColors();
        return new ArrayList<>(Arrays.asList(colors.get(resource)).subList(0, size));
    }

    public Integer getInverseImage(String actionType) {
        switch (actionType) {
            case "transit":
                return R.drawable.ic_transit_inverse;
            case "water":
                return R.drawable.ic_water_inverse;
            case "recycle":
                return R.drawable.ic_recycle_inverse;
            case "reuse":
                return R.drawable.ic_bag_inverse;
        }
        return -1;
    }

    // returns string containing plural form of unit only if magnitude != 1
    public String checkUnits(double magnitude, String format, String units, boolean castToInt) {
        if (magnitude == 1.) {
            if (castToInt) {
                return (int) magnitude + " " + units;
            } else {
                return String.format(format, magnitude) + " " + units;
            }
        } else {
            if (castToInt) {
                return (int) magnitude + " " + units + "s";
            } else {
                return String.format(format, magnitude) + " " + units + "s";
            }
        }
    }


}
