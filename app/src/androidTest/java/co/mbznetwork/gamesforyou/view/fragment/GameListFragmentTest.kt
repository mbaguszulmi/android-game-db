package co.mbznetwork.gamesforyou.view.fragment

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import co.mbznetwork.gamesforyou.R
import co.mbznetwork.gamesforyou.config.DEFAULT_PAGE_SIZE
import co.mbznetwork.gamesforyou.util.EspressoIdlingResource
import co.mbznetwork.gamesforyou.util.getText
import co.mbznetwork.gamesforyou.util.launchFragmentInHiltContainer
import co.mbznetwork.gamesforyou.util.withRecyclerView
import co.mbznetwork.gamesforyou.view.adapter.GameAdapter
import org.hamcrest.CoreMatchers.not
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class GameListFragmentTest {

    @Before
    fun setUp() {
        launchFragmentInHiltContainer<GameListFragment>()
        IdlingRegistry.getInstance().register(EspressoIdlingResource.getEspressoIdlingResource())
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.getEspressoIdlingResource())
    }

    @Test
    fun checkGameItems() {
        // check if there are game items in the recycler view
        for (i in 0 until DEFAULT_PAGE_SIZE) {
            onView(withId(R.id.rv_games)).perform(scrollToPosition<GameAdapter.ViewHolder>(i))

            onView(withRecyclerView(R.id.rv_games).atPosition(i))
                .check(matches(hasDescendant(withId(R.id.tv_game_name))))
        }
    }

    @Test
    fun checkSearchField() {
        onView(withId(R.id.iv_clear)).check(matches(not(isDisplayed())))

        onView(withId(R.id.et_search)).perform(typeText("abc"))


        onView(withId(R.id.iv_clear)).check(matches(isDisplayed()))
            .perform(click())

        onView(withId(R.id.iv_clear)).check(matches(not(isDisplayed())))

        val text = getText(onView(withId(R.id.et_search)), R.id.et_search)
        assertEquals("", text)
    }
}