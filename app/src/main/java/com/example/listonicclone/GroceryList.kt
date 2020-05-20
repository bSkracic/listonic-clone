package com.example.listonicclone

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class GroceryList {
    //<summary>
    // mapOfItems holds Items with Item name as key, intended for making changes on the list
    // listOfItems holds Items, intended for array adapter
    //<end>
    var listName: String = ""
    var mapOfItems: MutableMap<String, GroceryItem> = mutableMapOf()
    var listOfItems: MutableList<GroceryItem> = mutableListOf()
    var countOfItems: Int = 0
    var countOfBought: Int = 0
    var checked: Boolean = false

    constructor(list_string: String, list_name:String){
        val items = list_string.split('\n')
        listName = list_name
        checked = items[0].toBoolean()
        countOfItems = items.size - 1

        if(items.size > 1)
        {
            for(i in 1 until items.size) {
                val values = items[i]
                val item: GroceryItem = GroceryItem(values)
                mapOfItems[item.name] = item
                listOfItems.add(item)
                if(item.bought){
                    countOfBought += 1
                }
            }
        }
    }

    fun toDataString(): String{
        var listString = "$checked"
        val listOfKeys = mapOfItems.keys.toList()
        for(i in 0 until listOfKeys.size){
            val tempKey = listOfKeys[i]
            listString += "\n" + mapOfItems[tempKey]?.toDataString()
        }
        return listString
    }

}

//Definition of custom view holder for items of Grocery Lists
class GroceryListViewHolder(itemView: View, val supportActionBar: androidx.appcompat.app.ActionBar) : RecyclerView.ViewHolder(itemView) {
    private var mNameText: TextView? = null
    private var mAmountText: TextView? = null
    private var mCheckBox: CheckBox? = null
    private var mDelete: ImageView? = null

    init {
        mNameText = itemView.findViewById(R.id.name_text)
        mAmountText = itemView.findViewById(R.id.amount_text)
        mCheckBox = itemView.findViewById(R.id.checkbox)
        mDelete = itemView.findViewById(R.id.delete)
    }

    fun bind(list: SingleList, clickListener: OnItemClickListener) {
        mNameText?.text = list.name
        mAmountText?.text = "${list.bought} / ${list.count}"
        mCheckBox?.isChecked = list.checked
        mDelete?.visibility = View.GONE

        itemView.setOnClickListener {
            clickListener.onItemClicked(list)
        }

        itemView.setOnLongClickListener{
            mDelete?.visibility = View.VISIBLE
            mAmountText?.visibility = View.INVISIBLE
            supportActionBar.setDisplayHomeAsUpEnabled(true)
            return@setOnLongClickListener true
        }

        mDelete?.setOnClickListener{
            clickListener.onDeleteClicked(list)
        }
    }
}

//Definition of custom list adapter for recycler view
class GroceryListAdapter(var listOfLists:MutableList<SingleList>, private val itemClickListener: OnItemClickListener, val supportActionBar: androidx.appcompat.app.ActionBar):RecyclerView.Adapter<GroceryListViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, position: Int): GroceryListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_item,parent,false)
        return GroceryListViewHolder(view, supportActionBar!!)
    }

    override fun getItemCount(): Int {
        return listOfLists.size
    }

    override fun onBindViewHolder(myHolder: GroceryListViewHolder, position: Int) {
        val list = listOfLists[position]
        myHolder.bind(list, itemClickListener)
    }
}

//Custom interface to replace default OnItemClickListener
interface OnItemClickListener{
    fun onItemClicked(list: SingleList)
    fun onDeleteClicked(list: SingleList)
}