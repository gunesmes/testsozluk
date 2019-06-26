package testrisk.dictionary

import android.app.Activity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.rule.ActivityTestRule
import junit.extensions.ActiveTestSuite
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SozlukTests {
    private var recyclerViewSozluk = onView(withId(R.id.recyclerView))
    private var searchImage = onView(withId(R.id.imageSearch))

    @Rule
    @JvmField
    var activityTestsRule = ActivityTestRule(MainActivity::class.java, true, true)

    @Before
    fun setUp() {
        AnasayfaTests().goToSozluk()
    }

    @Test
    fun testSozlukLoaded() {
        searchImage.check(matches(isDisplayed()))
    }

}