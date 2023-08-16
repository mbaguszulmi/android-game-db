package co.mbznetwork.gamesforyou.util

import android.content.res.Resources
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher

fun withRecyclerView(recyclerViewId: Int): RecyclerViewMatcher {
    return RecyclerViewMatcher(recyclerViewId)
}

fun getText(matcher: ViewInteraction, id: Int): String? {
    val text = arrayOfNulls<String>(1)
    val va: ViewAction = object : ViewAction {
        override fun getConstraints(): Matcher<View> {
            return isAssignableFrom(View::class.java)
        }

        override fun getDescription(): String {
            return "Text of the view"
        }

        override fun perform(uiController: UiController?, view: View) {
            val tv = view.findViewById<TextView>(id)
            text[0] = tv.text.toString()
        }
    }
    matcher.perform(va)
    return text[0]
}

class RecyclerViewMatcher(private val recyclerViewId: Int) {
    fun atPosition(position: Int): Matcher<View> {
        return atPositionOnView(position, -1)
    }

    private fun atPositionOnView(position: Int, targetViewId: Int): Matcher<View> {
        return object : TypeSafeMatcher<View>() {
            var resources: Resources? = null
            var childView: View? = null
            override fun describeTo(description: Description) {
                var idDescription = recyclerViewId.toString()
                if (resources != null) {
                    idDescription = try {
                        resources!!.getResourceName(recyclerViewId)
                    } catch (var4: Resources.NotFoundException) {
                        String.format(
                            "%s (resource name not found)",
                            Integer.valueOf(recyclerViewId)
                        )
                    }
                }
                description.appendText("with id: $idDescription")
            }

            public override fun matchesSafely(view: View): Boolean {
                resources = view.resources
                if (childView == null) {
                    val recyclerView = view.rootView.findViewById<View>(
                        recyclerViewId
                    ) as? RecyclerView
                    childView = if (recyclerView?.id == recyclerViewId) {
                        recyclerView.findViewHolderForAdapterPosition(position)?.itemView
                    } else {
                        return false
                    }
                }
                return if (targetViewId == -1) {
                    view === childView
                } else {
                    val targetView = childView!!.findViewById<View>(targetViewId)
                    view === targetView
                }
            }
        }
    }
}
