package com.example.listonicclone

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_choose_item.*
import kotlinx.android.synthetic.main.activity_main.*

class ChooseItem : AppCompatActivity() , OnItemToPickListener{

    var groceryList: GroceryList? = null
    val itemsToPick: MutableList<ItemToPick>? = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_item)
        val listName = intent.getStringExtra("LIST_NAME")
        val listString = this.getSharedPreferences("LIST_OF_LISTS", Context.MODE_PRIVATE).getString(listName, "")

        // Hard-coded, could be replaced with API request
        itemsToPick?.add(ItemToPick("mlijeko", false, 1, String(Character.toChars(0x1F95B))))
        itemsToPick?.add(ItemToPick("kefir", false, 1, String(Character.toChars(0x1F95B))))
        itemsToPick?.add(ItemToPick("jogurt", false, 1, String(Character.toChars(0x1F95B))))
        itemsToPick?.add(ItemToPick("maslac", false, 1, String(Character.toChars(0x1F95B))))
        itemsToPick?.add(ItemToPick("kruh", false, 1, String(Character.toChars(0x1F35E))))
        itemsToPick?.add(ItemToPick("buhtla", false, 1, String(Character.toChars(0x1F35E))))
        itemsToPick?.add(ItemToPick("žemlja", false, 1, String(Character.toChars(0x1F35E))))
        itemsToPick?.add(ItemToPick("croissant", false, 1, String(Character.toChars(0x1F35E))))
        itemsToPick?.add(ItemToPick("piletina", false, 1, String(Character.toChars(0x1F357))))
        itemsToPick?.add(ItemToPick("puretina", false, 1, String(Character.toChars(0x1F357))))
        itemsToPick?.add(ItemToPick("svinjetina", false, 1, String(Character.toChars(0x1F357))))
        itemsToPick?.add(ItemToPick("teletina", false, 1, String(Character.toChars(0x1F357))))
        itemsToPick?.add(ItemToPick("kava", false, 1, String(Character.toChars(0x2615))))
        itemsToPick?.add(ItemToPick("čaj", false, 1, String(Character.toChars(0x2615))))
        itemsToPick?.add(ItemToPick("mozzarella", false, 1, String(Character.toChars(0x1F9C0))))
        itemsToPick?.add(ItemToPick("cheddar", false, 1, String(Character.toChars(0x1F9C0))))
        itemsToPick?.add(ItemToPick("feta sir", false, 1, String(Character.toChars(0x1F9C0))))
        itemsToPick?.add(ItemToPick("posni sir", false, 1, String(Character.toChars(0x1F9C0))))
        itemsToPick?.add(ItemToPick("limun", false, 1, String(Character.toChars(0x1F34B))))
        itemsToPick?.add(ItemToPick("naranča", false, 1, String(Character.toChars(0x1F34B))))
        // Hard-coded

        if(listString != ""){
            groceryList = GroceryList(listString!!, listName)
        }

        for(item: ItemToPick in itemsToPick!!){
            if(groceryList?.mapOfItems!!.containsKey(item.name)){
                item.isAddedToList = true
                item.count = groceryList?.mapOfItems?.get(item.name)?.amount!!
            }
        }

        recycler_view_choose_item.layoutManager = LinearLayoutManager(this)
        recycler_view_choose_item.adapter = ItemToPickAdapter(itemsToPick, this)

        search_text.doOnTextChanged { text, start, count, after ->
            var alternativeItemsToPick = mutableListOf<ItemToPick>()
            if(text == ""){
                recycler_view_choose_item.adapter = ItemToPickAdapter(itemsToPick, this)
            }
            else{
                for(i in 0 until itemsToPick.size){
                    if(itemsToPick[i].name.contains(text!!, true)){
                        alternativeItemsToPick.add(itemsToPick[i])
                    }
                }
                recycler_view_choose_item.adapter = ItemToPickAdapter(alternativeItemsToPick, this)
            }

        }
    }

    override fun onItemToPickClicked(pItem: ItemToPick) {
        if(pItem.isAddedToList){
            val category = pItem.category
            val groceryItem =  GroceryItem("${pItem.name}\t1\t0.0\tfalse\t$category")
            groceryList?.mapOfItems?.set(pItem.name,groceryItem)
            groceryList?.listOfItems?.add(groceryItem)
        }else{
            groceryList?.mapOfItems?.remove(pItem.name)
        }
        this.getSharedPreferences("LIST_OF_LISTS", Context.MODE_PRIVATE).edit().putString(groceryList?.listName, groceryList?.toDataString()).apply()
    }

    override fun onPlusButtonClicked(pItem: ItemToPick) {
        groceryList?.mapOfItems?.get(pItem.name)!!.amount += 1
        this.getSharedPreferences("LIST_OF_LISTS", Context.MODE_PRIVATE).edit().putString(groceryList?.listName, groceryList?.toDataString()).apply()
    }

    override fun onMinusButtonClicked(pItem: ItemToPick) {
        if(groceryList?.mapOfItems?.get(pItem.name)!!.amount != 1){
            groceryList?.mapOfItems?.get(pItem.name)!!.amount -= 1
            this.getSharedPreferences("LIST_OF_LISTS", Context.MODE_PRIVATE).edit().putString(groceryList?.listName, groceryList?.toDataString()).apply()
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, GroceryListView::class.java)
        intent.putExtra("LIST_NAME", groceryList?.listName)
        startActivity(intent)
    }


}
