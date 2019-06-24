package testrisk.dictionary

import androidx.test.espresso.Espresso.onView
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import org.junit.After
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import org.hamcrest.Matchers.containsString
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue


@LargeTest
@RunWith(AndroidJUnit4::class)
class MainActivityTests {
    private var btnAnasayfa = onView(withId(R.id.navigation_home))
    private var btnSozluk = onView(withId(R.id.navigation_dictionary))
    private var btnLicense = onView(withId(R.id.navigation_about))
    private var titleAnasayfa = onView(withId(R.id.header))
    private var titleLicense = onView(withId(R.id.headerAbout))
    private var contentAnasayfa = onView(withId(R.id.message))
    private var contentLicense = onView(withId(R.id.about))

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

        assertTrue(contentAnasayfa.toString().contains("test"))
    }

}