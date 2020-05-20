package com.example.listonicclone

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

data class ItemToPick(val name: String, var isAddedToList: Boolean, var count: Int, var category: String) {}

class ItemToPickViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private var mItem: CheckBox? = null
    private var mAmount: TextView? = null
    private var mButton_plus: Button? = null
    private var mButton_minus: Button? = null

    init {
        mItem = itemView.findViewById(R.id.item)
        mButton_plus = itemView.findViewById(R.id.button_plus)
        mButton_minus = itemView.findViewById(R.id.button_minus)
        mAmount = itemView.findViewById(R.id.amount)
    }

    fun bind(pItem: ItemToPick, clickListener: OnItemToPickListener) {
        if(pItem.isAddedToList){
            mButton_minus?.visibility = View.VISIBLE
            mButton_plus?.visibility = View.VISIBLE
            mAmount?.text = pItem.count.toString()
        }else{
            mAmount?.visibility = View.GONE
            mButton_minus?.visibility = View.GONE
            mButton_plus?.visibility = View.GONE
        }

        mItem?.text = pItem.name
        mItem?.isChecked = pItem.isAddedToList

        mButton_plus?.setOnClickListener{
            pItem.count += 1
            mAmount?.text = pItem.count.toString()
            clickListener.onPlusButtonClicked(pItem)
        }

        mButton_minus?.setOnClickListener{
            if(pItem.count != 1){
                pItem.count -= 1
                mAmount?.text = pItem.count.toString()
            }
            clickListener.onMinusButtonClicked(pItem)
        }

        mItem?.setOnClickListener {
            pItem.isAddedToList = !(pItem.isAddedToList)
            if(pItem.isAddedToList){
                mButton_minus?.visibility = View.VISIBLE
                mButton_plus?.visibility = View.VISIBLE
                mAmount?.visibility = View.VISIBLE
                mAmount?.text = pItem.count.toString()
            }else{
                mAmount?.visibility = View.GONE
                mButton_minus?.visibility = View.GONE
                mButton_plus?.visibility = View.GONE
            }
            clickListener.onItemToPickClicked(pItem)
        }
    }
}

//Definition of custom list adapter for recycler view
class ItemToPickAdapter(var listOfItemsToPick:MutableList<ItemToPick>, private val itemClickListener: OnItemToPickListener):
    RecyclerView.Adapter<ItemToPickViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ItemToPickViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_item_to_choose,parent,false)
        return ItemToPickViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listOfItemsToPick.size
    }

    override fun onBindViewHolder(myHolder: ItemToPickViewHolder, position: Int) {
        val list = listOfItemsToPick[position]
        myHolder.bind(list, itemClickListener)
    }
}

//Custom interface to replace default OnItemClickListener
interface OnItemToPickListener{
    fun onItemToPickClicked(pItem: ItemToPick)
    fun onMinusButtonClicked(pItem: ItemToPick)
    fun onPlusButtonClicked(pItem: ItemToPick)
}