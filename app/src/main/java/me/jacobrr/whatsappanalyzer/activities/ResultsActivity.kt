package me.jacobrr.whatsappanalyzer.activities

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.formatter.PercentFormatter
import kotlinx.android.synthetic.main.activity_results.*
import kotlinx.android.synthetic.main.days_results.view.*
import kotlinx.android.synthetic.main.general_data_results.view.*
import kotlinx.android.synthetic.main.hours_results.view.*
import kotlinx.android.synthetic.main.participants_data_results.view.*
import me.jacobrr.whatsappanalyzer.Constants
import me.jacobrr.whatsappanalyzer.PeopleListAdapter
import me.jacobrr.whatsappanalyzer.R
import me.jacobrr.whatsappanalyzer.logic.ConversationData
import me.jacobrr.whatsappanalyzer.logic.ConversationDataDB
import me.jacobrr.whatsappanalyzer.tasks.AnalyzePeopleTask
import me.jacobrr.whatsappanalyzer.tasks.ShareScreenshotTask

class ResultsActivity : AppCompatActivity() {

    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_results)

        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

        // Set up the ViewPager with the sections adapter.
        container.adapter = mSectionsPagerAdapter
        tabs.setupWithViewPager(container)

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

    private fun share() {
        ShareScreenshotTask(this).execute()
    }


    private fun saveDB() {
        Toast.makeText(this, "Saving", Toast.LENGTH_LONG).show()
        Constants.dbHandler?.insert(ConversationDataDB(Constants.conversationData))
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

            rootView.conversationDays.text = "${cv.totalDaysTalked}"
            rootView.totalMessages.text = "${cv.totalMessages}"
            rootView.msg_avg.text = String.format("%.2f", cv.dailyAvg)
            rootView.msg_real_avg.text = String.format("%.2f", cv.realDailyAvg)

            val day = cv.mostTalkedDay
            rootView.most_talked_day.text = getString(R.string.most_talked_day_result, day, cv.getDayData(day))
            rootView.most_talked_month.text = getString(R.string.most_talked_month_result, cv.mostTalkedMonth, cv.getMonthData(cv.mostTalkedMonth))
        }

        private fun createParticipantsView(rootView: View) {
            val peopleListAdapter = PeopleListAdapter(rootView.context, Constants.conversationData)
            rootView.people_list.adapter = peopleListAdapter
        }

        private fun createDaysView(rootView: View) {
            val cv = Constants.conversationData as ConversationData
            val data = cv.chartDaysData
            rootView.days_chart.data = data.component1()
            formatLineChart(rootView.days_chart, rootView.context, data.component2())
            val description = Description()
            description.text = "Days"
            description.textColor = Color.LTGRAY
            rootView.days_chart.description = description


            val data2 = cv.allDaysChartData
            rootView.all_days_chart.data = data2.component1()
            formatLineChart(rootView.all_days_chart, rootView.context, data2.component2())
            description.text = "All days"
            rootView.all_days_chart.description = description
        }

        private fun createHoursView(rootView: View) {
            val cv = Constants.conversationData as ConversationData
            rootView.hours_chart.data = cv.timeChartData
            rootView.hours_chart.data.setValueFormatter(PercentFormatter())

            val typedValue = TypedValue()
            val theme = rootView.context.theme
            theme.resolveAttribute(R.attr.colorPrimary, typedValue, true)
            rootView.hours_chart.setHoleColor(0)
            theme.resolveAttribute(R.attr.colorAccent, typedValue, true)
            val color = typedValue.data
            (rootView.hours_chart.data.dataSet as PieDataSet).setColors(color, color + 50, color - 50, color + 100, color - 100)
            rootView.hours_chart.setUsePercentValues(true)
            rootView.hours_chart.centerText = "Time of the day"
            rootView.hours_chart.setCenterTextColor(Color.LTGRAY)
            val des = Description()
            rootView.hours_chart.description = des
            rootView.hours_chart.legend.isEnabled = false
        }

        private fun formatLineChart(ln: LineChart, cont: Context, tags: List<String>) {
            val leftAxis = ln.axisLeft
            leftAxis.textColor = Color.LTGRAY
            val xAxis = ln.xAxis
            xAxis.textColor = Color.LTGRAY
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            val formatter = IAxisValueFormatter { value, _ -> tags[value.toInt()] }
            xAxis.valueFormatter = formatter

            val typedValue = TypedValue()
            val theme = cont.theme
            theme.resolveAttribute(R.attr.colorPrimary, typedValue, true)
            ln.setGridBackgroundColor(typedValue.data)
            ln.setBorderColor(Color.LTGRAY)
            ln.axisRight.isEnabled = false
            ln.legend.textColor = Color.LTGRAY
            ln.lineData.setValueTextColor(Color.LTGRAY)
        }

        companion object {
            /**
             * The fragment argument representing the section number for this
             * fragment.
             */
            private const val ARG_SECTION_NUMBER = "section_number"

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
    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

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
                0 -> return getString(R.string.general_data_title)
                1 -> return getString(R.string.participants_title)
                2 -> return getString(R.string.days)
                3 -> return getString(R.string.time_of_the_day_title)
            }
            return null
        }
    }
}
