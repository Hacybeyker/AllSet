package com.hacybeyker.allset.view.recycler.viewtype

import android.app.Activity
import android.content.Intent
import android.util.Log
import com.hacybeyker.allset.BaseActivity
import com.hacybeyker.allset.data.Item
import com.hacybeyker.allset.databinding.ActivityRecyclerViewTypeBinding
import com.hacybeyker.allset.view.recycler.viewtype.adapter.PersonVOAdapter
import com.hacybeyker.allset.view.recycler.viewtype.data.PersonData
import com.hacybeyker.allset.view.recycler.viewtype.vo.PersonVO

class RecyclerViewTypeActivity : BaseActivity(), PersonVOAdapter.OnItemSelectedListener {

    private lateinit var binding: ActivityRecyclerViewTypeBinding
    private val adapter: PersonVOAdapter by lazy { PersonVOAdapter(this) }
    private lateinit var item: Item

    companion object {

        private val tag = RecyclerViewTypeActivity::class.java.simpleName

        fun newStartActivity(activity: Activity, data: Item) {
            val intent = Intent(activity, RecyclerViewTypeActivity::class.java)
            intent.putExtra(tag, data)
            activity.startActivity(intent)
        }
    }

    override fun initView() {
        binding = ActivityRecyclerViewTypeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getIntentData()
    }

    override fun setupView() {
        activeToolbar = true
        titleToolbar = item.name
        adapter.items = PersonData.fetchPersons()
        binding.rvViewType.adapter = adapter

    }

    private fun getIntentData() {
        if (intent != null) {
            val dataTemp = intent.getParcelableExtra<Item>(tag)
            if (dataTemp != null)
                item = dataTemp
        }
    }


    override fun onItemSelected(item: PersonVO) {
        Log.d("TAG", "Here - onItemSelected: ")
    }

}