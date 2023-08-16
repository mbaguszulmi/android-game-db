package co.mbznetwork.gamesforyou.eventbus

import co.mbznetwork.gamesforyou.model.ui.UiStatus
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UIStatusEventBus @Inject constructor() {

    private val _uiStatus = MutableSharedFlow<UiStatus>()
    val uiStatus = _uiStatus.asSharedFlow()

    suspend fun setUiStatus(uiStatus: UiStatus) {
        _uiStatus.emit(uiStatus)
    }
}
