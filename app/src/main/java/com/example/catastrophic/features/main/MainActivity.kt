package com.example.catastrophic.features.main

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.catastrophic.R
import com.example.catastrophic.App
import com.example.catastrophic.features.base.BaseActivity
import com.example.catastrophic.features.adapters.ImageListAdapter
import com.example.catastrophic.util.EndlessRecyclerViewScrollListener
import com.example.catastrophic.util.PercentageCheckRecyclerViewScrollListener
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : BaseActivity() {
    override fun injectActivity() {
        (application as App).appComponent?.inject(this)
    }

    private lateinit var vm: MainViewModel
    private lateinit var imageListAdapter: ImageListAdapter

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override val layoutResource: Int
        get() = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        vm = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)

        initrvList()

        vm.loadImageList()
    }

    private fun initrvList() {
        var gridLayoutManager = GridLayoutManager(this, 3)
        rvData.layoutManager = gridLayoutManager
        imageListAdapter = ImageListAdapter(this)
        rvData.adapter = imageListAdapter
        rvData.addOnScrollListener(object : EndlessRecyclerViewScrollListener(rvData?.layoutManager!!) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                vm.loadMore()
            }

        })

        rvData.addOnScrollListener(object : PercentageCheckRecyclerViewScrollListener(gridLayoutManager) {
            var showOverlay = mutableListOf<Int>()

            override fun onViewChecked(position: Int, percentage: Double) {

                if(percentage >= 80)
                    showOverlay.add(position)
            }

            override fun onComplete(totalChecked: Int, firstPosition: Int, lastPosition: Int) {

                rvData.post {
                    imageListAdapter.updateOverlay(showOverlay, firstPosition, lastPosition)
                    showOverlay.clear()
                }
            }

        })

        vm.imageListLiveData.observe(this, Observer {
            imageListAdapter.updateData(it)
        })

    }
}
