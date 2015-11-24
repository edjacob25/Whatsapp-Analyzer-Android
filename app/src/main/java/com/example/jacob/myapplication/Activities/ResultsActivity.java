package com.example.jacob.myapplication.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jacob.myapplication.Constants;
import com.example.jacob.myapplication.Logic.ConversationData;
import com.example.jacob.myapplication.Logic.ConversationDataDB;
import com.example.jacob.myapplication.Logic.IConversationData;
import com.example.jacob.myapplication.PeopleListAdapter;
import com.example.jacob.myapplication.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

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

    }


    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_results, menu);
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
        Bitmap myBitmap;
        Log.i("Mira", mViewPager.getCurrentItem() + "");
        View v1 =
         mViewPager.getChildAt(mViewPager.getCurrentItem()).getRootView();

        //getWindow().getDecorView().getRootView(); View v1 = iv.getRootView(); //even this works
        // View v1 = findViewById(android.R.id.content); //this works too
        // but gives only content
        v1.setDrawingCacheEnabled(true);
        myBitmap = v1.getDrawingCache();
        saveBitmap(myBitmap);
    }

    public void saveBitmap(Bitmap bitmap) {
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);
        File dir = new File(Environment.getExternalStorageDirectory()
                + File.separator + "WhatsAppAnalyzer");
        if (!dir.exists())
            dir.mkdir();
        String filePath = Environment.getExternalStorageDirectory()
                + File.separator + "WhatsAppAnalyzer" + File.separator + now +  ".png";
        File imagePath = new File(filePath);
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(imagePath);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
            sendIntent(filePath);
        } catch (FileNotFoundException e) {
            Log.e("GREC", e.getMessage(), e);
        } catch (IOException e) {
            Log.e("GREC", e.getMessage(), e);
        }
    }

    public void sendIntent(String path) {
        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
        intent.putExtra(android.content.Intent.EXTRA_TEXT,
                "Generated with Whatsapp Analyzer");
        intent.setType("image/png");
        Uri myUri = Uri.parse("file://" + path);
        intent.putExtra(Intent.EXTRA_STREAM, myUri);
        startActivity(Intent.createChooser(intent, "Send image"));
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

            TextView days = (TextView) rootView.findViewById(R.id.conversationDays);
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
            daysChart.setData(cv.getChartDaysData());
            formatLineChart(daysChart, rootView.getContext());
            daysChart.setDescription("Days");

            LineChart allDaysChart = (LineChart) rootView.findViewById(R.id.all_days_chart);
            allDaysChart.setData(cv.getAllDaysChartData());
            formatLineChart(allDaysChart, rootView.getContext());
            allDaysChart.setDescription("All days");
        }

        private void createHoursView(View rootView){
            ConversationData cv = (ConversationData) Constants.conversationData;
            PieChart hoursChart = (PieChart) rootView.findViewById(R.id.hours_chart);
            hoursChart.setData(cv.getTimeChartData());


            TypedValue typedValue = new TypedValue();
            Resources.Theme theme = rootView.getContext().getTheme();
            theme.resolveAttribute(R.attr.colorPrimary, typedValue, true);
            hoursChart.setHoleColorTransparent(true);
            theme.resolveAttribute(R.attr.colorAccent, typedValue, true);
            int color = typedValue.data;
            hoursChart.getData().getDataSetByIndex(0).setColors(new int[]{color, color + 50, color - 50, color + 100, color - 100});
            hoursChart.setUsePercentValues(true);
            hoursChart.setCenterText("Time of the day");
            hoursChart.setCenterTextColor(Color.LTGRAY);
            hoursChart.setDescription("");
            hoursChart.getLegend().setEnabled(false);
        }

        private void formatLineChart(LineChart ln, Context cont){
            YAxis leftAxis = ln.getAxisLeft();
            leftAxis.setTextColor(Color.LTGRAY);
            XAxis xAxis = ln.getXAxis();
            xAxis.setTextColor(Color.LTGRAY);
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            TypedValue typedValue = new TypedValue();
            Resources.Theme theme = cont.getTheme();
            theme.resolveAttribute(R.attr.colorPrimary, typedValue, true);
            ln.setGridBackgroundColor(typedValue.data);
            ln.setDescriptionColor(Color.LTGRAY);
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
