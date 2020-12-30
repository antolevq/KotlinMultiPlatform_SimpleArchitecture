package com.leva.androidApp.view.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.leva.kmm.shared.domain.model.Response
import com.leva.kmm.shared.domain.model.RocketLaunch
import com.leva.kmm.shared.domain.model.Status
import com.leva.kmm.shared.domain.usecase.GetRocketListUseCase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async


class MainViewModel() : ViewModel() {

    private var launcherList: MutableLiveData<Response<List<RocketLaunch>>> =
        MutableLiveData<Response<List<RocketLaunch>>>()

    private var usecase: GetRocketListUseCase? = null
    fun setUseCase(uc: GetRocketListUseCase){
        usecase = uc
    }

    fun getLauncherResult() = launcherList

    fun displayLauncher(forceReload: Boolean) {
        launcherList.postValue(Response(Status.LOADING))
        usecase?.setForceReload(forceReload)
        GlobalScope.async {
            val result = usecase?.execute()
            launcherList.postValue(result)
        }
    }
}
