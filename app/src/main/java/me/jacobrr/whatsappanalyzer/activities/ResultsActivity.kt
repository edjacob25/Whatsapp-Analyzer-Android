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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.formatter.PercentFormatter
import me.jacobrr.whatsappanalyzer.Constants
import me.jacobrr.whatsappanalyzer.PeopleListAdapter
import me.jacobrr.whatsappanalyzer.R
import me.jacobrr.whatsappanalyzer.databinding.*
import me.jacobrr.whatsappanalyzer.logic.ConversationData
import me.jacobrr.whatsappanalyzer.logic.ConversationDataDB
import me.jacobrr.whatsappanalyzer.tasks.AnalyzePeopleTask
import me.jacobrr.whatsappanalyzer.tasks.ShareScreenshotTask

class ResultsActivity : AppCompatActivity() {

    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null
    private lateinit var binding: ActivityResultsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

        // Set up the ViewPager with the sections adapter.
        binding.container.adapter = mSectionsPagerAdapter
        binding.tabs.setupWithViewPager(binding.container)

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


        when (item.itemId) {
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

        private var _binding: ViewBinding? = null
        private val binding get() = _binding!!

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                                  savedInstanceState: Bundle?): View? {
            var rootView: View? = null
            when (arguments!!.getInt(ARG_SECTION_NUMBER)) {
                0 -> {
                    _binding = GeneralDataResultsBinding.inflate(layoutInflater, container, false)
                    createGeneralResultsView(binding as GeneralDataResultsBinding)
                }
                1 -> {
                    _binding = ParticipantsDataResultsBinding.inflate(layoutInflater, container, false)
                    createParticipantsView(binding as ParticipantsDataResultsBinding)
                }
                2 -> {
                    _binding = DaysResultsBinding.inflate(layoutInflater, container, false)
                    if (Constants.conversationData.javaClass == ConversationData::class.java)
                        createDaysView(binding as DaysResultsBinding)
                }
                3 -> {
                    _binding = HoursResultsBinding.inflate(layoutInflater, container, false)
                    if (Constants.conversationData.javaClass == ConversationData::class.java)
                        createHoursView(binding as HoursResultsBinding)
                }
            }
            rootView = binding.root
            return rootView
        }

        private fun createGeneralResultsView(binding: GeneralDataResultsBinding) {
            val cv = Constants.conversationData
            val day = cv.mostTalkedDay
            binding.apply {
                conversationDays.text = "${cv.totalDaysTalked}"
                totalMessages.text = "${cv.totalMessages}"
                msgAvg.text = String.format("%.2f", cv.dailyAvg)
                msgRealAvg.text = String.format("%.2f", cv.realDailyAvg)
                mostTalkedDay.text = getString(R.string.most_talked_day_result, day, cv.getDayData(day))
                mostTalkedMonth.text = getString(R.string.most_talked_month_result, cv.mostTalkedMonth, cv.getMonthData(cv.mostTalkedMonth))
            }
        }

        private fun createParticipantsView(binding: ParticipantsDataResultsBinding) {
            val viewManager = LinearLayoutManager(context)
            val peopleListAdapter = PeopleListAdapter(Constants.conversationData)

            binding.peopleList.apply {
                setHasFixedSize(true)
                layoutManager = viewManager
                adapter = peopleListAdapter
            }
        }

        private fun createDaysView(binding: DaysResultsBinding) {
            val cv = Constants.conversationData as ConversationData
            val daysData = cv.chartDaysData
            binding.daysChart.data = daysData.component1()
            formatLineChart(binding.daysChart, binding.root.context, daysData.component2(), "Days")

            val allDaysData = cv.allDaysChartData
            binding.allDaysChart.data = allDaysData.component1()
            formatLineChart(binding.allDaysChart, binding.root.context, allDaysData.component2(), "All days")
        }

        private fun createHoursView(binding: HoursResultsBinding) {
            val cv = Constants.conversationData as ConversationData
            val theme = binding.root.context.theme
            val typedValue = TypedValue()
            theme.resolveAttribute(R.attr.colorAccent, typedValue, true)
            val color = typedValue.data
            val title = getString(R.string.time_of_the_day_title)
            val des = Description()
            des.text = title
            binding.hoursChart.apply {
                data = cv.timeChartData
                data.setValueFormatter(PercentFormatter())
                setHoleColor(0)
                (data.dataSet as PieDataSet).setColors(color, color + 50, color - 50, color + 100, color - 100)
                setUsePercentValues(true)
                centerText = title
                setCenterTextColor(Color.LTGRAY)
                description = des
                legend.isEnabled = false
            }
        }

        private fun formatLineChart(ln: LineChart, cont: Context, tags: List<String>, descText: String) {
            val formatter = IAxisValueFormatter { value, _ -> tags[value.toInt()] }
            val desc = Description().apply {
                text = descText
                textColor = Color.LTGRAY
            }

            val typedValue = TypedValue()
            val theme = cont.theme
            theme.resolveAttribute(R.attr.colorPrimary, typedValue, true)

            ln.apply {
                axisLeft.textColor = Color.LTGRAY

                xAxis.textColor = Color.LTGRAY
                xAxis.position = XAxis.XAxisPosition.BOTTOM
                xAxis.valueFormatter = formatter

                description = desc
                setGridBackgroundColor(typedValue.data)
                setBorderColor(Color.LTGRAY)
                axisRight.isEnabled = false
                legend.textColor = Color.LTGRAY
                lineData.setValueTextColor(Color.LTGRAY)
            }
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
