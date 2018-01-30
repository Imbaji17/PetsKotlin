package com.pets.app.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pets.app.R
import com.pets.app.interfaces.SimpleItemClickListener
import com.pets.app.model.Breed
import com.pets.app.model.Category
import com.pets.app.model.PetsType
import com.pets.app.model.`object`.PetDetails
import com.pets.app.model.`object`.PetSitterDetails
import com.pets.app.viewholders.*

/**
 * Created by BAJIRAO on 16 January 2018.
 */
class CommonAdapter(mContext: Context, mList: ArrayList<Any>) : RecyclerView.Adapter<RecyclerViewViewHolder>() {

    private var mContext: Context
    private var mList: ArrayList<Any>
    private val PET_TYPE: Int = 1
    private val BREED_TYPE: Int = 2
    private val MY_PETS: Int = 3
    private val PRODUCT_CATEGORY: Int = 4
    private val PET_SITTER: Int = 5
    private lateinit var itemClick: SimpleItemClickListener

    init {
        this.mContext = mContext
        this.mList = mList
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerViewViewHolder {
        var viewHolder: RecyclerViewViewHolder? = null
        val inflater = LayoutInflater.from(parent?.context)
        when (viewType) {

            PET_TYPE -> {
                val view: View = inflater.inflate(R.layout.item_select_type, parent, false)
                viewHolder = PetsTypeVH(mContext, view, itemClick)
            }
            BREED_TYPE -> {
                val view: View = inflater.inflate(R.layout.item_select_type, parent, false)
                viewHolder = BreedTypeVH(mContext, view, itemClick)
            }
            MY_PETS -> {
                val view: View = inflater.inflate(R.layout.item_my_pets, parent, false)
                viewHolder = MyPetsVH(mContext, view, itemClick)
            }
            PRODUCT_CATEGORY -> {
                val view: View = inflater.inflate(R.layout.item_select_type, parent, false)
                viewHolder = CategoryVH(mContext, view, itemClick)
            }
            PET_SITTER -> {
                val view: View = inflater.inflate(R.layout.item_pet_sitter, parent, false)
                viewHolder = PetSitterVH(mContext, view, itemClick)
            }
        }
        return viewHolder!!
    }

    override fun onBindViewHolder(holder: RecyclerViewViewHolder?, position: Int) {
        holder?.onBindView(mList[position], position)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun getItemViewType(position: Int): Int {

        if (mList[position] is PetsType) {
            return PET_TYPE
        } else if (mList[position] is Breed) {
            return BREED_TYPE
        } else if (mList[position] is PetDetails) {
            return MY_PETS
        } else if (mList[position] is Category) {
            return PRODUCT_CATEGORY
        } else if (mList[position] is PetSitterDetails) {
            return PET_SITTER
        }
        return -1
    }

    fun setItemClick(itemClick: SimpleItemClickListener) {
        this.itemClick = itemClick
    }
}
