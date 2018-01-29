package com.pets.app.activities

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.widget.NumberPicker
import android.widget.RelativeLayout
import android.widget.TextView
import com.pets.app.R
import com.pets.app.common.ApplicationsConstants
import com.pets.app.initialsetup.BaseActivity
import com.pets.app.model.Category
import com.pets.app.model.PetsType

class BuySellFilterActivity : BaseActivity(), View.OnClickListener {

    private var rlPetType: RelativeLayout? = null
    private var tvPetType: TextView? = null
    private var rlCategory: RelativeLayout? = null
    private var tvCategory: TextView? = null
    private var rlSortPrice: RelativeLayout? = null
    private var tvSortPrice: TextView? = null

    private var petsTypeId: String? = "";
    private var petsTypeStr: String? = ""
    private var categoryId: String? = "";
    private var categoryStr: String? = ""
    private var sortPrice: String? = ""
    private val RC_TYPE: Int = 102
    private val RC_CATEGORY: Int = 103

    companion object {
        private val TAG = BuySellFilterActivity::class.java.simpleName
        fun startActivity(activity: Activity, requestCode: Int, petsTypeId: String, petsTypeStr: String,
                          categoryId: String, categoryStr: String, sortPrice: String) {
            val intent = Intent(activity, BuySellFilterActivity::class.java)
            intent.putExtra(ApplicationsConstants.PETS_TYPE_ID, petsTypeId)
            intent.putExtra(ApplicationsConstants.PETS_TYPE_NAME, petsTypeStr)
            intent.putExtra(ApplicationsConstants.CATEGORY_ID, categoryId)
            intent.putExtra(ApplicationsConstants.CATEGORY, categoryStr)
            intent.putExtra(ApplicationsConstants.PRICE, sortPrice)
            activity.startActivityForResult(intent, requestCode)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buy_sell_filter)
        initializeToolbar(getString(R.string.buy_and_sell))
        init()
        initView()
        setValues()
    }

    private fun init() {
        petsTypeId = intent.getStringExtra(ApplicationsConstants.PETS_TYPE_ID)
        petsTypeStr = intent.getStringExtra(ApplicationsConstants.PETS_TYPE_NAME)
        categoryId = intent.getStringExtra(ApplicationsConstants.CATEGORY_ID)
        categoryStr = intent.getStringExtra(ApplicationsConstants.CATEGORY)
        sortPrice = intent.getStringExtra(ApplicationsConstants.PRICE)
    }

    private fun initView() {
        rlPetType = findViewById(R.id.rlPetType)
        tvPetType = findViewById(R.id.tvPetType)
        rlCategory = findViewById(R.id.rlCategory)
        tvCategory = findViewById(R.id.tvCategory)
        rlSortPrice = findViewById(R.id.rlSortPrice)
        tvSortPrice = findViewById(R.id.tvSortPrice)

        rlPetType!!.setOnClickListener(this)
        rlCategory!!.setOnClickListener(this)
        rlSortPrice!!.setOnClickListener(this)
    }

    private fun setValues() {
        if (!TextUtils.isEmpty(petsTypeStr))
            tvPetType!!.text = petsTypeStr

        if (!TextUtils.isEmpty(categoryStr))
            tvCategory!!.text = categoryStr

        if (!TextUtils.isEmpty(sortPrice))
            tvSortPrice!!.text = sortPrice
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_apply, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item!!.itemId
        when (id) {
            R.id.action_apply -> {

                if (!TextUtils.isEmpty(tvPetType!!.text.toString().trim()))
                    petsTypeStr = tvPetType!!.text.toString()

                if (!TextUtils.isEmpty(tvCategory!!.text.toString().trim()))
                    categoryStr = tvCategory!!.text.toString()

                if (!TextUtils.isEmpty(tvSortPrice!!.text.toString().trim()))
                    sortPrice = tvSortPrice!!.text.toString()

                val intent = Intent()
                intent.putExtra(ApplicationsConstants.PETS_TYPE_ID, petsTypeId)
                intent.putExtra(ApplicationsConstants.PETS_TYPE_NAME, petsTypeStr)
                intent.putExtra(ApplicationsConstants.CATEGORY_ID, categoryId)
                intent.putExtra(ApplicationsConstants.CATEGORY, categoryStr)
                intent.putExtra(ApplicationsConstants.PRICE, sortPrice)
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.rlPetType -> {
                SelectTypeActivity.startActivity(this, RC_TYPE, petsTypeId!!, 0, "")
            }
            R.id.rlCategory -> {
                SelectTypeActivity.startActivity(this, RC_CATEGORY, categoryId!!, 2, "")
            }

            R.id.rlSortPrice -> {
                showSortPriceDialog()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            RC_TYPE -> {
                if (data != null) {
                    val petsType = data.getSerializableExtra(ApplicationsConstants.DATA) as PetsType
                    if (petsType != null) {
                        petsTypeId = petsType.petsTypeId
                        tvPetType!!.text = petsType.typeName
                    }
                }
            }
            RC_CATEGORY -> {
                if (data != null) {
                    val category = data.getSerializableExtra(ApplicationsConstants.DATA) as Category
                    if (category != null) {
                        categoryId = category.productCategoryId
                        tvCategory!!.text = category.productCategoryName
                    }
                }
            }
        }
    }


    private fun showSortPriceDialog() {

        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val contentView = inflater.inflate(R.layout.single_dial_row, null)
        val mDialog = Dialog(this)
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        mDialog.setContentView(contentView)
        mDialog.setCancelable(true)
        mDialog.show()

        val fractionArr = resources.getStringArray(R.array.sort_price_arr)

        var numberPicker = mDialog.findViewById<NumberPicker>(R.id.numberPicker)
        numberPicker.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
        numberPicker.minValue = 0
        numberPicker.maxValue = fractionArr.size - 1
        numberPicker.displayedValues = fractionArr
        numberPicker.wrapSelectorWheel = false

        val tvCancel = mDialog.findViewById<TextView>(R.id.tvCancel)
        val tvDone = mDialog.findViewById<TextView>(R.id.tvDone)
        var tvTitle = mDialog.findViewById<TextView>(R.id.tvTitle)
        tvTitle.text = getString(R.string.sort_by)
        tvCancel.setOnClickListener { mDialog.cancel() }

        tvDone.setOnClickListener {
            val s = fractionArr[numberPicker.value]
            mDialog.cancel()
            tvSortPrice!!.text = s
        }
    }

}