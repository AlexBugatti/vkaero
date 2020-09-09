package com.travels.searchtravels

import android.app.Application
import android.content.Context
import android.content.res.AssetManager
import org.junit.Test

import org.junit.Assert.*
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import android.view.View
import androidx.test.core.app.ApplicationProvider
import com.google.api.services.vision.v1.model.LatLng
import com.travels.searchtravels.activity.DetailsActivity
import com.travels.searchtravels.api.OnVisionApiListener
import com.travels.searchtravels.api.VisionApi
import com.travels.searchtravels.utils.L
import kotlinx.android.synthetic.main.activity_main.*
import org.junit.Assert
import org.junit.Before
import org.robolectric.RuntimeEnvironment
import org.robolectric.internal.bytecode.ResourceProvider
import java.net.URL
import java.net.HttpURLConnection

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class VisionAPIUnitTest {

    companion object {
        private const val INTERVAL_TIMEOUT = 10000L
        private const val GOOGLE_API_TOKEN = ""

        private const val OCEAN = "http://animalworld.com.ua/images/2017/August/Akva/Oknan/Oknan-1.jpg"
        private const val MOUNTAIN = "https://st.turtella.ru/photos/723/l144733.jpg"
        private const val SNOW = "https://cdnimg.rg.ru/img/content/134/83/54/sneg1_d_850.jpg"
    }

    @Test
    fun correctSea() {
        visialApiTest(OCEAN, "ocean")
    }

    @Test
    fun uncorrectSea() {
        visialApiTest(OCEAN, "snow")
    }

    @Test
    fun correctSnow() {
        visialApiTest(SNOW, "snow")
    }

    @Test
    fun uncorrectSnow() {
        visialApiTest(SNOW, "mountain")
    }

    @Test
    fun correctMountain() {
        visialApiTest(MOUNTAIN, "snow")
    }

    @Test
    fun uncorrectMountain() {
        visialApiTest(MOUNTAIN, "mountain")
    }


    private fun visialApiTest(urlString: String, type: String) {

        var bitmap = this.downloadImage(urlString)
        VisionApi.findLocation(bitmap, GOOGLE_API_TOKEN,
            object : OnVisionApiListener {
                override fun onSuccess(latLng: LatLng) {
                    Assert.assertTrue(true)
                }

                override fun onErrorPlace(category: String) {
                    Assert.assertTrue(category.equals(type))
                }

                override fun onError() {
                    Assert.assertTrue(false)
                }
            })
    }

    private fun downloadImage(urlString: String): Bitmap {
        val imageURL = URL(urlString)

        val connect: HttpURLConnection = imageURL.openConnection() as HttpURLConnection
        connect.doInput = true
        connect.connect()

        val input = connect.inputStream
        return BitmapFactory.decodeStream(input)
    }

}
