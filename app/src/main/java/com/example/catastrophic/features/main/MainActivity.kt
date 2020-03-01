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
import android.view.ScaleGestureDetector
import android.view.ScaleGestureDetector.OnScaleGestureListener


class MainActivity : BaseActivity() {
    override fun injectActivity() {
        (application as App).appComponent?.inject(this)
    }

    private lateinit var gridLayoutManager: GridLayoutManager
    private lateinit var scaleDetector: ScaleGestureDetector
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

        gridLayoutManager = GridLayoutManager(this, 3)
        rvData.layoutManager = gridLayoutManager
        imageListAdapter = ImageListAdapter(this)
        rvData.adapter = imageListAdapter
        rvData.addOnScrollListener(object :
            EndlessRecyclerViewScrollListener(rvData?.layoutManager!!) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                vm.loadMore()
            }

        })

        rvData.addOnScrollListener(object :
            PercentageCheckRecyclerViewScrollListener(gridLayoutManager) {
            var showOverlay = mutableListOf<Int>()

            override fun onViewChecked(position: Int, percentage: Double) {

                if (percentage >= 80)
                    showOverlay.add(position)
            }

            override fun onComplete(totalChecked: Int, firstPosition: Int, lastPosition: Int) {

                vm.updatOverlay(showOverlay)
                showOverlay.clear()
            }

        })

        scaleDetector = ScaleGestureDetector(this, object : OnScaleGestureListener {
            override fun onScaleEnd(detector: ScaleGestureDetector) {

            }

            override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
                return true
            }

            override fun onScale(detector: ScaleGestureDetector): Boolean {
                vm.pinchList(detector.scaleFactor)
                return false
            }

        })

        rvData.setOnTouchListener { v, event ->
            scaleDetector.onTouchEvent(event)
            super.onTouchEvent(event)
        }

        vm.imageListLiveData.observe(this, Observer {
            imageListAdapter.updateData(it)
        })

        vm.needtoZoomIn.observe(this, Observer {
            if (it) {
                setZoomInGridLayoutManager()
            } else {
                setStandardGridLayoutManager()
            }
        })

    }

    private fun setStandardGridLayoutManager() {
        gridLayoutManager.spanCount = 3
        gridLayoutManager.spanSizeLookup = GridLayoutManager.DefaultSpanSizeLookup()
    }

    private fun setZoomInGridLayoutManager() {
        gridLayoutManager.spanCount = 6
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (position % 5 == 0 || position % 5 == 1)
                    3
                else
                    2
            }

        }
    }
}
