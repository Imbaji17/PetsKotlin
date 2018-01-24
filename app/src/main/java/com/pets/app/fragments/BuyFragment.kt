package com.pets.app.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.*

import com.pets.app.R
import com.pets.app.activities.adoption.BuySellFilterActivity
import com.pets.app.adapters.FunZoneAdapter
import java.util.ArrayList


/**
 * Created by admin on 23/01/18.
 */

class BuyFragment : Fragment(), View.OnClickListener {

    private var viewFlipper: ViewFlipper? = null
    private var rlForLoadingScreen: RelativeLayout? = null
    private var recyclerView: RecyclerView? = null
    private var llForNoResult: LinearLayout? = null
    private var llForOfflineScreen: LinearLayout? = null
    private var llForRecyclerView: LinearLayout? = null
    private var llLoadMore: LinearLayout? = null
    private var tvNoResult: TextView? = null
    private var adapter: FunZoneAdapter? = null
    private var listItems = ArrayList<Any>()
    private var layoutManager: LinearLayoutManager? = null
    private var btnRetry: Button? = null
    private val RC_FILTER: Int = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_buy, container, false)
//        initView()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_filter, menu)
        super.onCreateOptionsMenu(menu, inflater)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_filter -> {
                BuySellFilterActivity.startActivity(activity, RC_FILTER)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun initView(view: View) {
        viewFlipper = view.findViewById(R.id.viewFlipper)
        rlForLoadingScreen = view.findViewById(R.id.rlForLoadingScreen)
        recyclerView = view.findViewById(R.id.recyclerView)
        llForNoResult = view.findViewById(R.id.llForNoResult)
        llForOfflineScreen = view.findViewById(R.id.llForOfflineScreen)
        llForRecyclerView = view.findViewById(R.id.llForRecyclerView)
        llLoadMore = view.findViewById(R.id.linLoadMore)
        btnRetry = view.findViewById(R.id.btnRetry)
        tvNoResult = view.findViewById(R.id.tvNoResult)
        btnRetry?.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {

        }
    }


}
