package co.mbznetwork.gamesforyou.view

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import co.mbznetwork.gamesforyou.R
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class MainActivityTest {
    @Test
    fun checkTabNavigation() {
        ActivityScenario.launch(MainActivity::class.java)

        onView(withId(R.id.menu_home)).perform(click())
        onView(withId(R.id.et_search)).check(matches(isDisplayed()))
        onView(withId(R.id.rv_games)).check(matches(isDisplayed()))

        onView(withId(R.id.menu_favorite)).perform(click())
        onView(withId(R.id.rv_favorite_games)).check(matches(isDisplayed()))
    }
}