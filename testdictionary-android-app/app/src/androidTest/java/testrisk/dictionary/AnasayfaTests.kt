package testrisk.dictionary

import android.widget.TextView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@LargeTest
@RunWith(AndroidJUnit4::class)
class AnasayfaTests {
    private var btnAnasayfa = onView(withId(R.id.navigation_home))
    private var btnSozluk = onView(withId(R.id.navigation_dictionary))
    private var btnLicense = onView(withId(R.id.navigation_about))
    private var titleAnasayfa = onView(withId(R.id.header))
    private var titleLicense = onView(withId(R.id.headerAbout))
    private var contentAnasayfa = onView(withId(R.id.message))
    private var contentLicense = onView(withId(R.id.about))
    var loader = onView(withId(R.id.loadingPanel))

    @Before
    fun setUp() {

    }

    @Rule
    @JvmField
    var activityTestsRule = ActivityTestRule(MainActivity::class.java, true, true)

    @Test
    fun testReadmeLoaded() {
        btnAnasayfa.check(matches(isDisplayed()))
        btnSozluk.check(matches(isDisplayed()))
        btnLicense.check(matches(isDisplayed()))

        // How to check if text view equals to som text
        // Junit assert lib gives result and expected more clearly
        // Junit assertEquals
        val textView: TextView = activityTestsRule.activity.findViewById(R.id.header)
        assertEquals("Neden Sözlükd", textView.text)

        // Espresso matcher
        titleAnasayfa.check(matches(withText("Neden Sözlük")))

        assertTrue(contentAnasayfa.toString().contains("test"))
    }

    @Test
    fun testSozlukLoaded() {
        goToSozluk()
        Thread.sleep(1000)
        loader.check(matches(isDisplayed()))
    }

    fun goToSozluk() {
        btnSozluk.check(matches(isClickable())).perform(ViewActions.click())
    }

    fun goToLicense() {
        btnLicense.check(matches(isClickable())).perform(ViewActions.click())
    }
}