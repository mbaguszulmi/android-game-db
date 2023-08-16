package co.mbznetwork.gamesforyou.util

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun AppCompatActivity.observeOnLifecycle(
    lifecycle: Lifecycle.State = Lifecycle.State.RESUMED,
    callback: suspend CoroutineScope.() -> Unit
) {
    lifecycleScope.launch {
        repeatOnLifecycle(lifecycle) {
            callback()
        }
    }
}

fun Fragment.observeOnLifecycle(
    lifecycle: Lifecycle.State = Lifecycle.State.RESUMED,
    callback: suspend CoroutineScope.() -> Unit
) {
    viewLifecycleOwner.lifecycleScope.launch {
        repeatOnLifecycle(lifecycle) {
            callback()
        }
    }
}