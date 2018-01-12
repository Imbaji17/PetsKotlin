package com.pets.app.initialsetup

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.pets.app.R
import com.pets.app.adapters.CountryListAdapter
import com.pets.app.common.ApplicationsConstants
import com.pets.app.interfaces.SimpleItemClickListener
import com.pets.app.mediator.AppTextWatcher
import com.pets.app.model.`object`.Country
import com.pets.app.utilities.Utils

class SelectCountryCodeActivity : BaseActivity(), View.OnClickListener, SimpleItemClickListener {

    private var edtSearch: EditText? = null
    private var imgSearch: ImageView? = null
    private var imgClear: ImageView? = null
    private var mRecyclerView: RecyclerView? = null
    private var countryList: ArrayList<Country>? = null
    private var adapter: CountryListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_country_code)

        initializeToolbar(this.getString(R.string.choose_country))
        initView()
        clickListeners()
    }

    private fun initView() {

        edtSearch = findViewById(R.id.edtSearch)
        imgSearch = findViewById(R.id.imgSearch)
        imgClear = findViewById(R.id.imgClear)
        mRecyclerView = findViewById(R.id.recyclerView)

        edtSearch!!.addTextChangedListener(object : AppTextWatcher() {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                super.onTextChanged(s, start, before, count)
                if (s?.isNotEmpty()!!) {
                    imgClear?.visibility = View.VISIBLE
                } else {
                    imgClear?.visibility = View.GONE
                }
                adapter?.countryFilter(s.toString())
            }
        })

        edtSearch!!.setOnEditorActionListener(TextView.OnEditorActionListener { textView, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                Utils.hideKeyboard(this)
                if (edtSearch?.text.toString().trim().isNotEmpty())
                    adapter?.countryFilter(edtSearch?.text.toString().trim())
                return@OnEditorActionListener true
            }
            false
        })

        val mLinearLayoutManager = LinearLayoutManager(this)
        mLinearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        mRecyclerView?.layoutManager = mLinearLayoutManager

        countryList = ArrayList()
        countryList?.addAll(Utils.getCountryList(this))
        adapter = CountryListAdapter(this, countryList!!)
        mRecyclerView?.adapter = adapter
        adapter?.setItemClick(this)
    }

    private fun clickListeners() {

        imgClear?.setOnClickListener(this)
    }

    override fun onClick(v: View?) {

        when (v?.id) {

            R.id.imgClear -> {
                edtSearch?.setText("")
            }
        }
    }

    override fun onItemClick(`object`: Any?) {

        if (`object` is Country) {
            val country = `object`
            val mIntent = Intent()
            mIntent.putExtra(ApplicationsConstants.DATA, country.phoneCode)
            setResult(Activity.RESULT_OK, mIntent)
            finish()
        }
    }
}
