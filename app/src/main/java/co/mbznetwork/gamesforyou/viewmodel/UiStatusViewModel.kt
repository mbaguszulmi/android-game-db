package co.mbznetwork.gamesforyou.viewmodel

import androidx.lifecycle.ViewModel
import co.mbznetwork.gamesforyou.eventbus.UIStatusEventBus
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UiStatusViewModel @Inject constructor(
    uiStatusEventBus: UIStatusEventBus
): ViewModel() {
    val uiStatus = uiStatusEventBus.uiStatus
}