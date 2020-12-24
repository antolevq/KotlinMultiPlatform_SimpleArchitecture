package com.leva.androidApp.view.main

import androidx.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.jetbrains.handson.androidApp.R
import com.leva.kmm.shared.domain.model.Result
import com.leva.kmm.shared.domain.usecase.GetRocketListUseCase
import com.leva.kmm.shared.persistence.DatabaseDriverFactory
import com.leva.kmm.shared.repository.RocketRespository
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel


class MainActivity : AppCompatActivity() {
    private val mainScope = MainScope()

    private lateinit var launchesRecyclerView: RecyclerView
    private lateinit var progressBarView: FrameLayout
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var context: Context
    private lateinit var viewmodel: MainViewModel

    private val launchesRvAdapter = LaunchesRvAdapter(listOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = this
        title = "SpaceX Launches"
        setContentView(R.layout.activity_main)
        viewmodel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        viewmodel.setUseCase(GetRocketListUseCase(RocketRespository(DatabaseDriverFactory(this))))
        launchesRecyclerView = findViewById(R.id.launchesListRv)
        progressBarView = findViewById(R.id.progressBar)
        swipeRefreshLayout = findViewById(R.id.swipeContainer)

        launchesRecyclerView.adapter = launchesRvAdapter
        launchesRecyclerView.layoutManager = LinearLayoutManager(this)

        swipeRefreshLayout.setOnRefreshListener {
            swipeRefreshLayout.isRefreshing = false
            displayLaunches(true)
        }

        displayLaunches(false)

        viewmodel.getLauncherResult().observe(this, Observer { res ->
            when (res) {
                is Result.Success -> {
                    progressBarView.isVisible = false
                    launchesRvAdapter.launchEntities = res.data
                    launchesRvAdapter.notifyDataSetChanged()
                }
                is Result.Error -> {
                    progressBarView.isVisible = false
                    Toast.makeText(this@MainActivity, res.message, Toast.LENGTH_SHORT).show()
                }
                else -> {
                    progressBarView.isVisible = true
                }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        mainScope.cancel()
    }


    private fun displayLaunches(needReload: Boolean) {
        viewmodel.displayLauncher(needReload)
    }
}