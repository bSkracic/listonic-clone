package com.example.listonicclone

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import androidx.core.widget.doOnTextChanged
import kotlinx.android.synthetic.main.activity_edit_item.*

class EditItem : AppCompatActivity() {

    var groceryList: GroceryList? = null
    var itemName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_item)

        val listName = intent.getStringExtra("LIST_NAME")
        itemName = intent.getStringExtra("ITEM_NAME")
        this.title = itemName
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        //receive grocery list from previous activity
        var groceryListString = this.getSharedPreferences("LIST_OF_LISTS", Context.MODE_PRIVATE).getString(listName, "")

        var categoryAdapterList = mutableListOf<String>()

        //Hard coded, could be replaced with API request
        val categoryMap:HashMap<String, String> = hashMapOf(
            Pair<String, String>("mliječni proizvodi", String(Character.toChars(0x1F95B))),
            Pair<String, String>("krušni proizvodi", String(Character.toChars(0x1F35E))),
            Pair<String, String>("citrusno voće", String(Character.toChars(0x1F34B))),
            Pair<String, String>("topli napitci", String(Character.toChars(0x2615))),
            Pair<String, String>("mesni proizvodi", String(Character.toChars(0x1F357))),
            Pair<String, String>("sirevi", String(Character.toChars(0x1F9C0)))
        )
        //Hard coded

        for(category in categoryMap){
            categoryAdapterList.add(category.value + " " + category.key)
        }

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, categoryAdapterList)
        this.spinner_categories.adapter = adapter

        if(groceryListString != " "){
            groceryList = GroceryList(groceryListString!!, listName)
            this.edit_price.setText(groceryList?.mapOfItems?.get(itemName)!!.price.toString())
            this.edit_amount.setText(groceryList?.mapOfItems?.get(itemName)!!.amount.toString())

            for(i in 0 until categoryAdapterList.size){
                val list = categoryAdapterList[i].split(' ')
                if(list[0] == groceryList?.mapOfItems?.get(itemName)!!.category){
                    this.spinner_categories.setSelection(i)
                    break
                }
            }

        }

        this.buttonMinus.setOnClickListener {
            if(this.groceryList!!.mapOfItems[itemName]?.amount != 1){
                this.groceryList!!.mapOfItems[itemName]?.amount = groceryList!!.mapOfItems[itemName]?.amount?.minus(1)!!
            }
            this.edit_amount.setText(groceryList!!.mapOfItems[itemName]?.amount.toString())
        }

        this.buttonPlus.setOnClickListener {
            this.groceryList!!.mapOfItems[itemName]?.amount = groceryList!!.mapOfItems[itemName]?.amount?.plus(1)!!
            this.edit_amount.setText(groceryList!!.mapOfItems[itemName]?.amount.toString())
        }
    }

    override fun onBackPressed() {
        saveChanges()
        super.onBackPressed()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home){
            saveChanges()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun saveChanges() {
        this.groceryList!!.mapOfItems[itemName]?.price = edit_price.text.toString().toFloat()
        if(this.multiply_price.isChecked){
            this.groceryList!!.mapOfItems[itemName]?.price = groceryList!!.mapOfItems[itemName]?.amount?.times(groceryList!!.mapOfItems[itemName]?.price!!)!!
        }

        this.groceryList!!.mapOfItems[itemName]?.amount = edit_amount.text.toString().toInt()

        val list = this.spinner_categories.selectedItem.toString().split(' ')
        this.groceryList!!.mapOfItems[itemName]?.category = list[0]

        this.getSharedPreferences("LIST_OF_LISTS", Context.MODE_PRIVATE).edit().putString(groceryList?.listName, groceryList?.toDataString()).apply()

        val intent = Intent(this, GroceryListView::class.java)
        intent.putExtra("LIST_NAME", groceryList?.listName)
        startActivity(intent)
    }



}

