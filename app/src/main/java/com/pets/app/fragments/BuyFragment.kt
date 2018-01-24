package com.pets.app.fragments

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.*
import android.widget.*
import com.google.android.gms.location.places.ui.PlaceAutocomplete
import com.pets.app.R
import com.pets.app.activities.BuySellFilterActivity
import com.pets.app.adapters.BuyAdapter
import com.pets.app.common.*
import com.pets.app.model.Breed
import com.pets.app.model.PetsType
import com.pets.app.model.ProductResponse
import com.pets.app.model.`object`.PhotosInfo
import com.pets.app.utilities.*
import com.pets.app.webservice.RestClient
import com.pets.app.webservice.WebServiceBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.util.*


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
    private var adapter: BuyAdapter? = null
    private var listItems = ArrayList<Any>()
    private var layoutManager: GridLayoutManager? = null
    private var btnRetry: Button? = null
    private val RC_FILTER: Int = 2

    private var nextOffset = 0
    private var loading = true
    private var pastVisibleItems: Int = 0
    private var visibleItemCount: Int = 0
    private var totalItemCount: Int = 0
    private var productType: String = "BUY"
    private var petsTypeId: String = ""
    private var petsTypeStr: String? = ""
    private var categoryId: String = ""
    private var categoryStr: String? = ""
    private var sortPrice: String = ""


//    product_type=SELL&pets_type_id=0&product_category_id=1&next_offset

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view: View = inflater!!.inflate(R.layout.fragment_buy, container, false)
        initView(view)
        setAdapter()
        getProductList()
//        return inflater!!.inflate(R.layout.fragment_buy, container, false)
        return view
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


    private fun setAdapter() {

        val spanCount = 2 // 3 columns
        val spacing = 10 // 50px
        val includeEdge = true
        recyclerView!!.addItemDecoration(GridSpacingItemDecoration(spanCount, spacing, includeEdge))
        layoutManager = GridLayoutManager(activity, spanCount)

        recyclerView!!.layoutManager = layoutManager
        recyclerView!!.itemAnimator = DefaultItemAnimator()
        adapter = BuyAdapter(listItems, this, 0)
        recyclerView!!.adapter = adapter
    }

    private fun getProductList() {
        if (Utils.isOnline(activity)) {
            if (nextOffset == 0) {
                setLoadingLayout()
            } else {
                llLoadMore!!.visibility = View.VISIBLE
            }

            val timeStamp = TimeStamp.getTimeStamp()
            val language = Enums.Language.EN.name.toUpperCase()
            val userId = AppPreferenceManager.getUserID()
            val key = TimeStamp.getMd5(timeStamp + userId + Constants.TIME_STAMP_KEY)

            val apiClient = RestClient.createService(WebServiceBuilder.ApiClient::class.java)
            val call = apiClient.getProductList(userId, timeStamp, key, language, productType, petsTypeId, categoryId, nextOffset)
            call.enqueue(object : Callback<ProductResponse> {
                override fun onResponse(call: Call<ProductResponse>, response: Response<ProductResponse>?) {
                    loading = true
                    llLoadMore!!.visibility = View.GONE

                    if (response != null && response.isSuccessful && response.body() != null) {
                        nextOffset = response.body().nextOffset
                        if (response.body().list != null) {
                            listItems.addAll(response.body().list)
                            adapter!!.notifyDataSetChanged()
                        }
                    } else {
                        Utils.showErrorToast(response?.errorBody())
                    }
                    if (!listItems.isEmpty()) {
                        setMainLayout()
                    } else {
                        setNoDataLayout()
                    }
                }

                override fun onFailure(call: Call<ProductResponse>, t: Throwable) {
                    loading = true
                    llLoadMore!!.visibility = View.GONE
                    setNoDataLayout()
                }
            })
        } else {
            setOfflineLayout()
        }
    }

    private fun setOfflineLayout() {
        viewFlipper!!.displayedChild = viewFlipper!!.indexOfChild(llForOfflineScreen)
    }

    private fun setLoadingLayout() {
        viewFlipper!!.displayedChild = viewFlipper!!.indexOfChild(rlForLoadingScreen)
    }

    private fun setMainLayout() {
        viewFlipper!!.displayedChild = viewFlipper!!.indexOfChild(llForRecyclerView)
    }

    private fun setNoDataLayout() {
        tvNoResult?.text = getString(R.string.no_result_found)
        viewFlipper!!.displayedChild = viewFlipper!!.indexOfChild(llForNoResult)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_filter, menu)
        super.onCreateOptionsMenu(menu, inflater)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_filter -> {
                val intent = Intent(activity, BuySellFilterActivity::class.java)
                intent.putExtra(ApplicationsConstants.PETS_TYPE_ID, petsTypeId)
                intent.putExtra(ApplicationsConstants.PETS_TYPE_NAME, petsTypeStr)
                intent.putExtra(ApplicationsConstants.CATEGORY_ID, categoryId)
                intent.putExtra(ApplicationsConstants.CATEGORY, categoryStr)
                intent.putExtra(ApplicationsConstants.PRICE, sortPrice)
                startActivityForResult(intent, RC_FILTER)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            RC_FILTER -> {
                if (data != null) {
                    petsTypeId = data.getStringExtra(ApplicationsConstants.PETS_TYPE_ID)
                    petsTypeStr = data.getStringExtra(ApplicationsConstants.PETS_TYPE_NAME)
                    categoryId = data.getStringExtra(ApplicationsConstants.CATEGORY_ID)
                    categoryStr = data.getStringExtra(ApplicationsConstants.CATEGORY)
                    sortPrice = data.getStringExtra(ApplicationsConstants.PRICE)

                    listItems.clear()
                    adapter!!.notifyDataSetChanged()
                    nextOffset = 0
                    getProductList()
                }
            }
        }
    }
}
