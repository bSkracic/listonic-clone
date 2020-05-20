package com.example.listonicclone

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log.println
import android.view.*
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_grocery_list_view.*
import kotlinx.android.synthetic.main.content_grocery_list_view.*

class GroceryListView : AppCompatActivity(), OnGroceryItemClickListener {

    var groceryList: GroceryList? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grocery_list_view)
        setSupportActionBar(toolbar)

        //receive list name
        val listName = intent.getStringExtra("LIST_NAME")

        //check if list by received name exists
        if (listName == "") {
            Toast.makeText(this, "There is no such list, error occurred!", Toast.LENGTH_SHORT)
                .show()
            return;
        }

        fab.setOnClickListener { view ->
            val intent = Intent(this, ChooseItem::class.java)
            intent.putExtra("LIST_NAME", listName)
            startActivity(intent)
        }

        //load list
        this.title = listName
        val groceryListString = this?.getSharedPreferences("LIST_OF_LISTS", Context.MODE_PRIVATE)
            .getString(listName, "")

        if (groceryListString != "") {
            groceryList = GroceryList(groceryListString!!, listName)
            recycler_view_items.layoutManager = LinearLayoutManager(this)
            recycler_view_items.adapter = GroceryItemAdapter(groceryList!!.listOfItems, this, false)
        }

        if(groceryList?.mapOfItems?.isEmpty()!!){
            recycler_view_items.visibility = View.GONE
        }else {
            greeting_message2.visibility = View.GONE
            greeting_text2.visibility = View.GONE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.title == "Pokaži cijene") {
            recycler_view_items.adapter = GroceryItemAdapter(groceryList!!.listOfItems, this, true)
            item.title = "Pokaži ikone"
        } else if (item.title == "Pokaži ikone") {
            recycler_view_items.adapter = GroceryItemAdapter(groceryList!!.listOfItems, this, false)
            item.title = "Pokaži cijene"
        }

        if(item.title == "Pokaži ukupni trošak"){
            this.title = calculateSpending()
            item.title = "Sakrij ukupni trošak"
        }else if(item.title == "Sakrij ukupni trošak"){
            this.title = groceryList!!.listName
            item.title = "Pokaži ukupni trošak"
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    override fun onGroceryItemClicked(item: GroceryItem) {
        val intent = Intent(this, EditItem::class.java)
        intent.putExtra("LIST_NAME", groceryList!!.listName)
        intent.putExtra("ITEM_NAME", item.name)
        startActivity(intent)
    }

    override fun onGroceryCheckBoxClicked(item: GroceryItem) {
        groceryList!!.mapOfItems[item.name]!!.bought =
            !(groceryList!!.mapOfItems[item.name]!!.bought)
        this?.getSharedPreferences("LIST_OF_LISTS", Context.MODE_PRIVATE).edit()
            .putString(groceryList!!.listName, groceryList!!.toDataString()).apply()
        this.title = calculateSpending()
    }

    fun calculateSpending(): String{
        var returnString = ""
        var totalPrice: Float = 0.0f
        var paidPrice: Float = 0.0f
        val mapOfItems = groceryList!!.mapOfItems
        for(item in mapOfItems){
            totalPrice += item.value.price
            if(item.value.bought){
                paidPrice += item.value.price
            }
        }
        return "$paidPrice HRK / $totalPrice HRK"
    }
}




