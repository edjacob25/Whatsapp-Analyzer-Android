package com.example.jacob.myapplication.activities

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.TypedValue
import android.view.*
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import com.example.jacob.myapplication.Constants
import com.example.jacob.myapplication.PeopleListAdapter
import com.example.jacob.myapplication.R
import com.example.jacob.myapplication.logic.ConversationData
import com.example.jacob.myapplication.logic.ConversationDataDB
import com.example.jacob.myapplication.tasks.AnalyzePeopleTask
import com.example.jacob.myapplication.tasks.ShareScreenshotTask
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.formatter.PercentFormatter
import java.text.SimpleDateFormat

class ResultsActivity : AppCompatActivity() {

    /**
     * The [android.support.v4.view.PagerAdapter] that will provide
     * fragments for each of the sections. We use a
     * [FragmentPagerAdapter] derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * [android.support.v4.app.FragmentStatePagerAdapter].
     */
    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null
    /**
     * The [ViewPager] that will host the section contents.
     */
    private var mViewPager: ViewPager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_results)


        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById<View>(R.id.container) as ViewPager
        mViewPager!!.adapter = mSectionsPagerAdapter

        val tabLayout = findViewById<View>(R.id.tabs) as TabLayout
        tabLayout.setupWithViewPager(mViewPager)

        if (Constants.conversationData.javaClass == ConversationData::class.java)
            AnalyzePeopleTask(this).execute()
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_results, menu)
        if (Constants.conversationData.javaClass == ConversationDataDB::class.java)
            menu.getItem(0).isEnabled = false
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId


        when (id) {
            R.id.menu_save -> saveDB()
            R.id.menu_item_share -> share()
        }

        return super.onOptionsItemSelected(item)
    }

    fun share() {
        ShareScreenshotTask(this).execute()
    }


    fun saveDB() {
        Toast.makeText(this, "Saving", Toast.LENGTH_LONG).show()
        Constants.dbHandler.insert(ConversationDataDB(Constants.conversationData))
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    class PlaceholderFragment : Fragment() {

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                                  savedInstanceState: Bundle?): View? {
            var rootView: View? = null
            when (arguments!!.getInt(ARG_SECTION_NUMBER)) {
                0 -> {
                    rootView = inflater.inflate(R.layout.general_data_results, container, false)
                    createGeneralResultsView(rootView!!)
                }
                1 -> {
                    rootView = inflater.inflate(R.layout.participants_data_results, container, false)
                    createParticipantsView(rootView!!)
                }
                2 -> {
                    rootView = inflater.inflate(R.layout.days_results, container, false)
                    if (Constants.conversationData.javaClass == ConversationData::class.java)
                        createDaysView(rootView!!)
                }
                3 -> {
                    rootView = inflater.inflate(R.layout.hours_results, container, false)
                    if (Constants.conversationData.javaClass == ConversationData::class.java)
                        createHoursView(rootView!!)
                }
            }

            return rootView
        }

        private fun createGeneralResultsView(rootView: View) {
            val cv = Constants.conversationData
            val sdf = SimpleDateFormat("dd/MM/yyyy")

            val days = rootView.findViewById<TextView>(R.id.conversationDays)
            days.text = cv.totalDaysTalked.toString() + ""

            val totalMsgs = rootView.findViewById<View>(R.id.totalMessages) as TextView
            totalMsgs.text = cv.totalMessages.toString() + ""

            val avg = rootView.findViewById<View>(R.id.msg_avg) as TextView
            avg.text = String.format("%.2f", cv.dailyAvg)

            val real_avg = rootView.findViewById<View>(R.id.msg_real_avg) as TextView
            real_avg.text = String.format("%.2f", cv.realDailyAvg)

            val mtDay = rootView.findViewById<View>(R.id.most_talked_day) as TextView
            val day = cv.mostTalkedDay
            val mtDayString = "Is " + sdf.format(day) + " with " +
                    cv.getDayData(day) + " messages."
            mtDay.text = mtDayString

            val mtMonth = rootView.findViewById<View>(R.id.most_talked_month) as TextView
            val mtMonthString = "Is " + cv.mostTalkedMonth + " with " +
                    cv.getMonthData(cv.mostTalkedMonth) + " messages."
            mtMonth.text = mtMonthString
        }

        private fun createParticipantsView(rootView: View) {
            val l = rootView.findViewById<View>(R.id.people_list) as ListView
            val peopleListAdapter = PeopleListAdapter(rootView.context, Constants.conversationData)
            l.adapter = peopleListAdapter
        }

        private fun createDaysView(rootView: View) {
            val cv = Constants.conversationData as ConversationData
            val daysChart = rootView.findViewById<View>(R.id.days_chart) as LineChart
            val data = cv.chartDaysData
            daysChart.data = data.component1()
            formatLineChart(daysChart, rootView.context, data.component2())
            val a = Description()
            a.text = "Days"
            a.textColor = Color.LTGRAY
            daysChart.description = a

            val allDaysChart = rootView.findViewById<View>(R.id.all_days_chart) as LineChart
            val data2 = cv.allDaysChartData
            allDaysChart.data = data2.component1()
            formatLineChart(allDaysChart, rootView.context, data2.component2())
            a.text = "All days"
            allDaysChart.description = a
        }

        private fun createHoursView(rootView: View) {
            val cv = Constants.conversationData as ConversationData
            val hoursChart = rootView.findViewById<View>(R.id.hours_chart) as PieChart
            hoursChart.data = cv.timeChartData
            hoursChart.data.setValueFormatter(PercentFormatter())

            val typedValue = TypedValue()
            val theme = rootView.context.theme
            theme.resolveAttribute(R.attr.colorPrimary, typedValue, true)
            hoursChart.setHoleColor(0)
            theme.resolveAttribute(R.attr.colorAccent, typedValue, true)
            val color = typedValue.data
            (hoursChart.data.dataSet as PieDataSet).setColors(color, color + 50, color - 50, color + 100, color - 100)
            hoursChart.setUsePercentValues(true)
            hoursChart.centerText = "Time of the day"
            hoursChart.setCenterTextColor(Color.LTGRAY)
            val des = Description()
            hoursChart.description = des
            hoursChart.legend.isEnabled = false
        }

        private fun formatLineChart(ln: LineChart, cont: Context, tags: List<String>) {
            val leftAxis = ln.axisLeft
            leftAxis.textColor = Color.LTGRAY
            val xAxis = ln.xAxis
            xAxis.textColor = Color.LTGRAY
            xAxis.position = XAxis.XAxisPosition.BOTTOM

            val formatter = IAxisValueFormatter { value, axis -> tags[value.toInt()] }

            xAxis.valueFormatter = formatter


            val typedValue = TypedValue()
            val theme = cont.theme
            theme.resolveAttribute(R.attr.colorPrimary, typedValue, true)
            ln.setGridBackgroundColor(typedValue.data)
            //ln.setDescriptionColor(Color.LTGRAY);
            ln.setBorderColor(Color.LTGRAY)
            ln.axisRight.isEnabled = false
            ln.legend.textColor = Color.LTGRAY
            ln.lineData.setValueTextColor(Color.LTGRAY)
            //ln.getLineData().getDataSetByIndex(0).setColors(new int[]{color, color + 100, color - 100});
        }

        companion object {
            /**
             * The fragment argument representing the section number for this
             * fragment.
             */
            private val ARG_SECTION_NUMBER = "section_number"

            /**
             * Returns a new instance of this fragment for the given section
             * number.
             */
            fun newInstance(sectionNumber: Int): PlaceholderFragment {
                val fragment = PlaceholderFragment()
                val args = Bundle()
                args.putInt(ARG_SECTION_NUMBER, sectionNumber)
                fragment.arguments = args
                return fragment
            }
        }


    }

    /**
     * A [FragmentPagerAdapter] that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position)
        }

        override fun getCount(): Int {
            return 4
        }

        override fun getPageTitle(position: Int): CharSequence? {
            when (position) {
                0 -> return "General Data"
                1 -> return "Participants"
                2 -> return "Days"
                3 -> return "Hours"
            }
            return null
        }
    }
}
