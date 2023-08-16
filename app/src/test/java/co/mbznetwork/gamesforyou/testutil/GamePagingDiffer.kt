package co.mbznetwork.gamesforyou.testutil

import androidx.paging.AsyncPagingDataDiffer
import androidx.recyclerview.widget.ListUpdateCallback
import co.mbznetwork.gamesforyou.view.adapter.DIFF_CALLBACK
import kotlinx.coroutines.Dispatchers

fun generateDiffer() = AsyncPagingDataDiffer(
    diffCallback = DIFF_CALLBACK,
    updateCallback = noopListUpdateCallback,
    workerDispatcher = Dispatchers.Main
)

private val noopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}
