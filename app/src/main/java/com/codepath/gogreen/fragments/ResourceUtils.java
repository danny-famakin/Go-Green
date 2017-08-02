package com.codepath.gogreen.fragments;

import java.util.HashMap;

/**
 * Created by anyazhang on 8/1/17.
 */

public class ResourceUtils {
    public static HashMap<String, Double> weights = new HashMap<>();
    public String[] resources = new String[] {"emissions", "fuel", "water", "trees"};

    public HashMap getWeights() {
        weights.put("fuel", 20.);
        weights.put("emissions", 5.);
        weights.put("water", 0.1);
        weights.put("trees", 1.);
        return weights;
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

}
