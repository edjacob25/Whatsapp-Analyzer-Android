package com.example.jacob.myapplication.Activities;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jacob.myapplication.Tasks.AnalyzePeopleTask;
import com.example.jacob.myapplication.Constants;
import com.example.jacob.myapplication.Logic.ConversationData;
import com.example.jacob.myapplication.Logic.ConversationDataDB;
import com.example.jacob.myapplication.Logic.IConversationData;
import com.example.jacob.myapplication.PeopleListAdapter;
import com.example.jacob.myapplication.R;
import com.example.jacob.myapplication.Tasks.ShareScreenshotTask;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ResultsActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        if (Constants.conversationData.getClass().equals(ConversationData.class))
            new AnalyzePeopleTask(this).execute();
    }


    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_results, menu);
        if (Constants.conversationData.getClass().equals(ConversationDataDB.class))
            menu.getItem(0).setEnabled(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id){
            case R.id.menu_save:
                saveDB();
                break;
            case R.id.menu_item_share:
                share();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void share(){
        new ShareScreenshotTask(this).execute();
    }


    public void saveDB(){
        Toast.makeText(this, "Saving", Toast.LENGTH_LONG).show();
        Constants.dbHandler.insert(new ConversationDataDB(Constants.conversationData));
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override

        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = null;
            switch (getArguments().getInt(ARG_SECTION_NUMBER)) {
                case 0:
                    rootView   = inflater.inflate(R.layout.general_data_results, container, false);
                    createGeneralResultsView(rootView);
                    break;
                case 1:
                    rootView   = inflater.inflate(R.layout.participants_data_results, container, false);
                    createParticipantsView(rootView);
                    break;
                case 2:
                    rootView   = inflater.inflate(R.layout.days_results, container, false);
                    if (Constants.conversationData.getClass() == ConversationData.class)
                        createDaysView(rootView);
                    break;
                case 3:
                    rootView   = inflater.inflate(R.layout.hours_results, container, false);
                    if (Constants.conversationData.getClass() == ConversationData.class)
                        createHoursView(rootView);
                    break;
            }

            return rootView;
        }

        private void createGeneralResultsView(View rootView){
            IConversationData cv =  Constants.conversationData;
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

            TextView days = rootView.findViewById(R.id.conversationDays);
            days.setText(cv.getTotalDaysTalked() + "");

            TextView totalMsgs = (TextView) rootView.findViewById(R.id.totalMessages);
            totalMsgs.setText(cv.getTotalMessages() + "");

            TextView avg = (TextView) rootView.findViewById(R.id.msg_avg);
            avg.setText(String.format("%.2f",cv.getDailyAvg()));

            TextView real_avg = (TextView) rootView.findViewById(R.id.msg_real_avg);
            real_avg.setText(String.format("%.2f",cv.getRealDailyAvg()));

            TextView mtDay = (TextView) rootView.findViewById(R.id.most_talked_day);
            Date day = cv.getMostTalkedDay();
            String mtDayString = "Is " + sdf.format(day) +" with " +
                    cv.getDayData(day) + " messages.";
            mtDay.setText(mtDayString);

            TextView mtMonth = (TextView) rootView.findViewById(R.id.most_talked_month);
            String mtMonthString = "Is " + cv.getMostTalkedMonth() + " with " +
                    cv.getMonthData(cv.getMostTalkedMonth()) + " messages.";
            mtMonth.setText(mtMonthString);
        }

        private void createParticipantsView(View rootView){
            ListView l = (ListView) rootView.findViewById(R.id.people_list);
            PeopleListAdapter peopleListAdapter = new PeopleListAdapter(rootView.getContext(),Constants.conversationData);
            l.setAdapter(peopleListAdapter);
        }

        private void createDaysView(View rootView){
            ConversationData cv = (ConversationData) Constants.conversationData;
            LineChart daysChart = (LineChart) rootView.findViewById(R.id.days_chart);
            kotlin.Pair<LineData, List<String>> data = cv.getChartDaysData();
            daysChart.setData(data.component1());
            formatLineChart(daysChart, rootView.getContext(),data.component2());
            Description a = new Description();
            a.setText("Days");
            a.setTextColor(Color.LTGRAY);
            daysChart.setDescription(a);

            LineChart allDaysChart = (LineChart) rootView.findViewById(R.id.all_days_chart);
            kotlin.Pair<LineData, List<String>> data2 = cv.getAllDaysChartData();
            allDaysChart.setData(data2.component1());
            formatLineChart(allDaysChart, rootView.getContext(), data2.component2());
            a.setText("All days");
            allDaysChart.setDescription(a);
        }

        private void createHoursView(View rootView){
            ConversationData cv = (ConversationData) Constants.conversationData;
            PieChart hoursChart = (PieChart) rootView.findViewById(R.id.hours_chart);
            hoursChart.setData(cv.getTimeChartData());
            hoursChart.getData().setValueFormatter(new PercentFormatter());

            TypedValue typedValue = new TypedValue();
            Resources.Theme theme = rootView.getContext().getTheme();
            theme.resolveAttribute(R.attr.colorPrimary, typedValue, true);
            hoursChart.setHoleColor(0);
            theme.resolveAttribute(R.attr.colorAccent, typedValue, true);
            int color = typedValue.data;
            ((PieDataSet)hoursChart.getData().getDataSet()).setColors(color, color + 50, color - 50, color + 100, color - 100);
            hoursChart.setUsePercentValues(true);
            hoursChart.setCenterText("Time of the day");
            hoursChart.setCenterTextColor(Color.LTGRAY);
            Description des = new Description();
            hoursChart.setDescription(des);
            hoursChart.getLegend().setEnabled(false);
        }

        private void formatLineChart(LineChart ln, Context cont, final List<String> tags){
            YAxis leftAxis = ln.getAxisLeft();
            leftAxis.setTextColor(Color.LTGRAY);
            XAxis xAxis = ln.getXAxis();
            xAxis.setTextColor(Color.LTGRAY);
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

            IAxisValueFormatter formatter = new IAxisValueFormatter() {

                @Override
                public String getFormattedValue(float value, AxisBase axis) {
                    return tags.get(((int) value));
                }
            };

            xAxis.setValueFormatter(formatter);


            TypedValue typedValue = new TypedValue();
            Resources.Theme theme = cont.getTheme();
            theme.resolveAttribute(R.attr.colorPrimary, typedValue, true);
            ln.setGridBackgroundColor(typedValue.data);
            //ln.setDescriptionColor(Color.LTGRAY);
            ln.setBorderColor(Color.LTGRAY);
            ln.getAxisRight().setEnabled(false);
            ln.getLegend().setTextColor(Color.LTGRAY);
            ln.getLineData().setValueTextColor(Color.LTGRAY);
            //ln.getLineData().getDataSetByIndex(0).setColors(new int[]{color, color + 100, color - 100});
        }


    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "General Data";
                case 1:
                    return "Participants";
                case 2:
                    return "Days";
                case 3:
                    return "Hours";
            }
            return null;
        }
    }
}
