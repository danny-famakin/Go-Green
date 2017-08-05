package com.codepath.gogreen.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.codepath.gogreen.R;
import com.codepath.gogreen.SourceAdapter;

import java.util.ArrayList;
import java.util.Arrays;


/**
 * Created by anyazhang on 8/4/17.
 */

public class AboutFragment extends Fragment {
    Context context;
    SourceAdapter sourceAdapter;
    RecyclerView sourceList;
    View v;
    ArrayList<Bundle> sources;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        context = getContext();
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.fragment_about, null);

        sourceList = (RecyclerView) v.findViewById(R.id.rvSources);
        // Create and populate a List of planet names.
        final String[] bagLinks = {
                "<a href=\"http://www.ncpa.org/pub/st340?pg=4\">National Center for Policy Analysis</a>",
                "<a href=\"http://www.abs.gov.au/ausstats/abs@.nsf/0/2498b7e0c5178282ca256dea000539bc?OpenDocument\">Australian Bureau of Statistics</a>"
                };

        final String[] bottleLinks = {
                "<a href=\"http://www.popularmechanics.com/science/environment/a3752/4291566/\">Popular Mechanics</a>",
                "<a href=\"https://www3.epa.gov/epawaste/conserve/tools/warm/pdfs/Plastics.pdf\">US Environmental Protection Agency</a>"
        };

        final String[] paperLinks = {
                "<a href=\"http://www.gracelinks.org/285/the-hidden-water-in-everyday-products\">GRACE Communications Foundation</a>",
                "<a href=\"http://recycled-papers.co.uk/green-matters/why-use-recycled-papers/co2-and-greenhouse-gases/\">Arjowiggins Graphic</a>",
                "<a href=\"https://lbre.stanford.edu/pssistanford-recycling/frequently-asked-questions/frequently-asked-questions-benefits-recycling\">Stanford University Land, Buildings, & Real Estate</a>"
        };
        final String[] canLinks = {
                "<a href=\"http://www.ct.gov/deep/cwp/view.asp?a=2714&q=440320\">Connecticut Deptartment of Energy and Environmental Protection</a>",
                "<a href=\"https://www.earthworksaction.org/files/publications/DirtyMetals_EnergyWater.pdf\">Earthworks</a>",
                "<a href=\"https://www.thoughtco.com/the-benefits-of-aluminum-recycling-1204138\">ThoughtCo</a>",
                "<a href=\"http://www.abcwua.org/education/pdfs/Guess_Water_complete.pdf\">Albuquerque County Water Utility Authority</a>"
        };
        final String[] transitLinks = {
                "<a href=\"https://www.transit.dot.gov/sites/fta.dot.gov/files/docs/PublicTransportationsRoleInRespondingToClimateChange2010.pdf\">Federal Transit Authority</a>",
                "<a href=\"http://planet3.org/2012/10/31/does-public-transportation-save-energy/\">Planet 3.0</a>",
                "<a href=\"http://www.vtpi.org/tran_climate.pdf\">Victoria Transport Policy Institute</a>",
                "<a href=\"https://www.rita.dot.gov/bts/sites/rita.dot.gov.bts/files/publications/national_transportation_statistics/html/table_01_11.html\">Bureau of Transportation Statistics</a>",
                "<a href=\"http://www.ci.benicia.ca.us/vertical/sites/%7BF991A639-AAED-4E1A-9735-86EA195E2C8D%7D/uploads/truecost_2014.pdf\">Official Website of the City of Benicia, California</a>",
                "<a href=\"http://www.apta.com/resources/reportsandpublications/Documents/apta_public_transportation_fuel_savings_final_010807.pdf\">American Public Transportation Association</a>"
               };
        final String[] showerLinks = {
                "<a href=\"https://www.epa.gov/watersense/showerheads\">US Environmental Protection Agency</a>",
                "<a href=\"http://sustainability.yale.edu/sites/default/files/boola_shower.pdf\">Yale University Sustainability</a>",
                "<a href=\"http://www.home-water-works.org/indoor-use/showers\">Alliance for Water Efficiency</a>"

                };

        final String[] bagIcons = {
                "https://upload.wikimedia.org/wikipedia/en/7/70/NCPA_logo.png",
                "http://amsi.org.au/wp-content/uploads/2014/06/12_ABS.jpg"
        };
        final String[] bottleIcons = {
                "http://pop.h-cdn.co/assets/popularmechanics/20170803152459/images/apple-touch-icon.png",
                "https://www3.epa.gov/epahome/images/epa_seal_profiles.jpg"
        };
        final String[] paperIcons = {
                "http://www.gracelinks.org/images/apicon.png",
                "https://pbs.twimg.com/profile_images/884462719109402625/iHHEXekh_400x400.jpg",
                "https://www-media.stanford.edu/assets/favicon/favicon-196x196.png",

        };
        final String[] canIcons = {
                "https://sft.ct.gov/html/skin/bestwc/C/assets/icons/axw_ct.gov_l_logo.png",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQWn8GXpbx23OFUhuxIhnQOZk9uNYODMDFcWAPAgNCZiIonwk8JCw",
                "https://www.thoughtco.com/static/2.19.3/icons/favicons/apple-touch-icon-152x152.png",
                "http://local.thecityofalbuquerque.com/images/water-utility-authority.jpg"
        };

        final String[] transitIcons = {
                "https://www.brandsoftheworld.com/sites/default/files/styles/logo-thumbnail/public/032011/department_of_transportation.png?itok=SbsCg-Az",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSSY-FCL7Z4qLeff4RgMaHUsZOQc1WUeTVsXQU4WeLRc-CBVN8X",
                "http://www.omegacentre.bartlett.ucl.ac.uk/wp-content/uploads/2014/11/victoria-trasport-policy-institute-logo.png",
                "https://www.brandsoftheworld.com/sites/default/files/styles/logo-thumbnail/public/032011/department_of_transportation.png?itok=SbsCg-Az",
                "http://www.ci.benicia.ca.us/repository/designs/images/GOLogo144.png",
                "https://upload.wikimedia.org/wikipedia/en/thumb/2/20/American_Public_Transportation_Association_%28logo%29.svg/1200px-American_Public_Transportation_Association_%28logo%29.svg.png"
        };

        final String[] showerIcons = {
                "https://www3.epa.gov/epahome/images/epa_seal_profiles.jpg",
                "https://www.yale.edu/sites/all/themes/yale_blue/images/favicons/favicon.ico",
                "http://s3.amazonaws.com/rdcms-phcc/files/production/public/images/Logos/HomeWaterWorksLogo.png"
        };

        ArrayList<String[]> linksArray = new ArrayList<>(Arrays.asList(transitLinks, showerLinks, bagLinks, bottleLinks, canLinks, paperLinks));
        ArrayList<String[]> iconsArray = new ArrayList<>(Arrays.asList(transitIcons, showerIcons, bagIcons, bottleIcons, canIcons, paperIcons));

        ArrayList<String> titlesArray = new ArrayList<>(Arrays.asList("Public Transit", "Shorter Showers", "Reusing Bags", "Recycling Bottles", "Recycling Cans", "Recycling Paper"));
        ArrayList<Integer> titleColorsArray = new ArrayList<>(Arrays.asList(R.color.colorPrimaryDark, R.color.colorAccentDark, R.color.darkBlue, R.color.lightGreen, R.color.lightGreen,R.color.lightGreen));
                sources = new ArrayList<>();


        sourceAdapter = new SourceAdapter(sources);
        // Set the ArrayAdapter as the ListView's adapter.
        sourceList.setAdapter(sourceAdapter);
        sourceList.setLayoutManager(new LinearLayoutManager(getActivity()));

        for (int i = 0; i < linksArray.size(); i++) {
            Bundle source = new Bundle();
            source.putString("sectTitle", titlesArray.get(i));
            source.putStringArray("sourceLinks", linksArray.get(i));
            source.putStringArray("sourceIcons", iconsArray.get(i));
            source.putInt("titleColor", titleColorsArray.get(i));

            sources.add(source);
            sourceAdapter.notifyDataSetChanged();

        }

        MaterialDialog modal = new MaterialDialog.Builder(context)
                .customView(v, false)
                .show();
    }



}
