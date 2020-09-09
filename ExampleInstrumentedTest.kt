package com.travels.searchtravels

import android.os.Looper
import org.junit.Assert.*
import android.widget.TextView
import androidx.core.widget.doOnTextChanged
import androidx.test.core.app.ActivityScenario.launch
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.travels.searchtravels.activity.ChipActivity
import org.junit.Before
import org.robolectric.Shadows.shadowOf

import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    companion object {
        const val INTERVAL_TIMOUT = 10000L

        const val CORRECT_CITY_NAME = "Санкт-Петербург"
        const val UNCORRECTED_CITY_NAME = "Пваакенцупг"
    }

    private lateinit var latch: CountDownLatch
    private lateinit var prices: MutableList<Double>

    @Before
    fun setup() {
        prices = ArrayList()
        latch = CountDownLatch(1)
    }

    @Test
    fun correctPrice() {
        priceTest(CORRECT_CITY_NAME)
    }

    @Test
    fun uncorrectedPrice() {
        priceTest(UNCORRECTED_CITY_NAME)
    }

    private fun priceTest(cityname: String) {
        val scenario = launch(ChipActivity::class.java)

        scenario.onActivity { activity ->
            val view = activity.findViewById<TextView>(R.id.airticketTV)
            activity.getInfoNomad(cityname)
            view.doOnTextChanged { text, start, count, _ -> onPriceTextChanged(text.toString()) }
        }
        latch.await(INTERVAL_TIMOUT, TimeUnit.MILLISECONDS)
        shadowOf(Looper.getMainLooper()).idle()

        assertEquals("Error countdown", 0, latch.count)
        assertEquals("error prices", 1, prices)

        val price = prices.first()
        assertTrue("Correct price", price > 0)
    }

    private fun onPriceTextChanged(text: String) {
        val priceWithoutAdditions = text.replace(Regex("от\\s+"), "")
                                        .replace(Regex("\\u20BD"), "").toDouble()
        prices.add(priceWithoutAdditions)
        latch.countDown()
    }

}