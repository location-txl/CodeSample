package com.location.codesample.weight

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.location.codesample.R
import com.location.codesample.databinding.ActivityRefreshBinding
import com.location.codesample.weight.RefreshView.OnRefreshListener
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class RefreshTestActivity: AppCompatActivity() {
    companion object{
        private const val TAG = "RefreshTestActivity"
    }
    private val binding by lazy { ActivityRefreshBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val testAdapter = TestAdapter()
        with(binding.list) {
            layoutManager = LinearLayoutManager(this@RefreshTestActivity)
            adapter = testAdapter
            testAdapter.update((0..100).map { "item $it" })
        }
        binding.refresh.setOnRefreshListener(object : OnRefreshListener {
            override fun onRefresh() {
                Log.d(TAG, "onRefresh: ")
                lifecycleScope.launch{
                    delay(3000L)
                    val start = Random.nextInt()
                    testAdapter.update((start..start + 100).map { "item $it" })
                    binding.refresh.finishRefresh()
                }
            }
        })

    }
}

private class TestAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    private val list = mutableListOf<String>()


    fun update(list: List<String>){
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return object : RecyclerView.ViewHolder(TextView(parent.context)){}
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder.itemView as TextView).text = list[position]
    }

    override fun getItemCount(): Int  = list.size
}