package co.mbznetwork.gamesforyou.util

import androidx.test.espresso.IdlingResource
import androidx.test.espresso.idling.CountingIdlingResource

object EspressoIdlingResource {
    private const val RESOURCE: String = "GLOBAL"
    private val espressoTestIdlingResource = CountingIdlingResource(RESOURCE)
    fun increment() {
        espressoTestIdlingResource.increment()
    }
    fun decrement() {
        if (!getEspressoIdlingResource().isIdleNow)
            espressoTestIdlingResource.decrement()
    }
    fun getEspressoIdlingResource(): IdlingResource {
        return espressoTestIdlingResource
    }
}