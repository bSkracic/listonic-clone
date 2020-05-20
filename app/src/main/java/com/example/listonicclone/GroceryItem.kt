package com.example.listonicclone

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.RecyclerView


class GroceryItem {
    var name: String = ""
    var amount: Int = 0
    var price: Float = 0.0f
    var bought: Boolean = false
    var category: String = ""

    constructor(item_string: String) {
        val itemValues = item_string.split('\t')
        name = itemValues[0]
        amount = itemValues[1].toInt()
        price = itemValues[2].toFloat()
        bought = itemValues[3].toBoolean()
        category = itemValues[4]
    }

    fun toDataString(): String{
        val roundedPrice = "%.2f".format(price)
       return "$name\t$amount\t$roundedPrice\t$bought\t$category"
    }
}


class GroceryItemViewHolder(itemView: View, val showPrice: Boolean) : RecyclerView.ViewHolder(itemView) {
    private var mText_item_name: TextView? = null
    private var mText_item_amount: TextView? = null
    private var mText_item_price: TextView? = null
    private var mItem_bought_checkbox: CheckBox? = null
    private var mText_item_category: TextView? = null

    init {
        mText_item_name = itemView.findViewById(R.id.text_item_name)
        mText_item_amount = itemView.findViewById(R.id.text_item_amount)
        mText_item_price = itemView.findViewById(R.id.text_item_price)
        mItem_bought_checkbox = itemView.findViewById(R.id.checkbox_item_bought)
        mText_item_category = itemView.findViewById(R.id.text_item_category)
    }

    fun bind(item: GroceryItem, clickListener: OnGroceryItemClickListener) {
        mText_item_name?.text = item.name
        if(showPrice){
            mText_item_price?.visibility = View.VISIBLE
            mText_item_category?.visibility = View.GONE
            mText_item_price?.text = item.price.toString() + " HRK"

        }else{
            mText_item_price?.visibility = View.GONE
            mText_item_category?.visibility = View.VISIBLE
            mText_item_category?.text = item.category
        }

        if(item.amount > 1){
            mText_item_amount?.visibility = View.VISIBLE
            mText_item_amount?.text = item.amount.toString()
        }else{
            mText_item_amount?.visibility = View.GONE
        }

        mItem_bought_checkbox?.isChecked = item.bought

        itemView.setOnClickListener {
            clickListener.onGroceryItemClicked(item)
        }

        mItem_bought_checkbox?.setOnClickListener{
            clickListener.onGroceryCheckBoxClicked(item)
        }
    }
}

//Definition of custom list adapter for recycler view
class GroceryItemAdapter(var listOfItems: MutableList<GroceryItem>, private val itemClickListener: OnGroceryItemClickListener, val showPrice: Boolean):
    RecyclerView.Adapter<GroceryItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, position: Int): GroceryItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_item_list_view,parent,false)
        return GroceryItemViewHolder(view, showPrice)
    }

    override fun getItemCount(): Int {
        return listOfItems.size
    }

    override fun onBindViewHolder(myHolder: GroceryItemViewHolder, position: Int) {
        val item = listOfItems[position]
        myHolder.bind(item, itemClickListener)
    }
}

interface OnGroceryItemClickListener{
    fun onGroceryItemClicked(item: GroceryItem)
    fun onGroceryCheckBoxClicked(item: GroceryItem)
}

