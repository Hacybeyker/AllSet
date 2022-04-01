package com.hacybeyker.allset.view.recycler

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.hacybeyker.allset.BaseActivity
import com.hacybeyker.allset.data.Movie
import com.hacybeyker.allset.data.MovieData
import com.hacybeyker.allset.databinding.ActivityRecyclerMainBinding
import com.hacybeyker.allset.view.recycler.adapter.RecyclerMainAdapter
import com.hacybeyker.allset.view.recycler.classic.StartSnapHelper


class RecyclerMainActivity : BaseActivity() {

    private lateinit var binding: ActivityRecyclerMainBinding
    private val adapter: RecyclerMainAdapter by lazy { RecyclerMainAdapter() }

    companion object {
        fun newStartActivity(activity: Activity, data: Any?) {
            val intent = Intent(activity, RecyclerMainActivity::class.java)
            activity.startActivity(intent)
        }
    }

    override fun initView() {
        binding = ActivityRecyclerMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun setupView() {
        binding.recyclerMain.adapter = adapter
        binding.recyclerMain.addItemDecoration(
            DividerItemDecoration(
                applicationContext,
                DividerItemDecoration.HORIZONTAL
            )
        )

        /*val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(binding.recyclerMain)*/

        /*val startSnapHelper = StartSnapHelper()
        startSnapHelper.attachToRecyclerView(binding.recyclerMain)*/

        val pagerSnapHelper = PagerSnapHelper()
        pagerSnapHelper.attachToRecyclerView(binding.recyclerMain)

        adapter.items = MovieData.fetchMovieData()
        val swipeHelper = object : SwipeHelper(applicationContext) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val item: Movie = adapter.items[position]
                Log.d("TAG", "Here - onSwiped: $item")

                /*val snackbar = Snackbar
                    .make(
                        binding.containerMain,
                        "Item was removed from the list.",
                        Snackbar.LENGTH_LONG
                    )
                snackbar.setAction("UNDO", object : OnClickListener() {
                    fun onClick(view: View?) {
                        mAdapter.restoreItem(item, position)
                        recyclerView.scrollToPosition(position)
                    }
                })

                snackbar.setActionTextColor(Color.YELLOW)
                snackbar.show()*/

            }

        }
        val itemTouchHelper = ItemTouchHelper(swipeHelper)
        itemTouchHelper.attachToRecyclerView(binding.recyclerMain)


        /*val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.Callback() {
            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {
                Log.d("TAG", "Here - getMovementFlags: ")
                return ItemTouchHelper.LEFT
            }

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                Log.d("TAG", "Here - onMove: ")
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                when (direction) {
                    ItemTouchHelper.LEFT -> Log.d("TAG", "Here - onSwiped: $direction")
                    else -> Log.d("TAG", "Here - onSwiped: $direction")
                }
            }

        })

        itemTouchHelper.attachToRecyclerView(binding.recyclerMain)*/

        //https://stackoverflow.com/questions/29134094/recyclerview-horizontal-scroll-snap-in-center

        //mostar un recycler horizontal y vertical
        //que tenga snaphelper, decorator y diffutil

    }
}