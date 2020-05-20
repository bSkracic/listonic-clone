package com.example.listonicclone

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

//<summary>
//Definition of data model for recycler view
//<end>
data class SingleList(val name: String, val count: Int, val bought: Int, val checked: Boolean)

class MainActivity : AppCompatActivity(), OnItemClickListener {

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        this.title = "Moji popisi"

        val listOfLists = this.getSharedPreferences("LIST_OF_LISTS", Context.MODE_PRIVATE).all.keys
        if(listOfLists.isNotEmpty()){
            this.greeting_message.visibility = View.GONE
            this.greeting_text.visibility = View.GONE
        }else{
            recycler_view.visibility = View.GONE
        }

        //set floating button listener
        val fab: View = findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            openFragment()
        }
        setListView()
    }

    override fun onItemClicked(list: SingleList) {
        val intent = Intent(this, GroceryListView::class.java).apply {
            putExtra("LIST_NAME", list.name)
        }
        startActivity(intent)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home){
            setListView()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDeleteClicked(list: SingleList) {
        this.getSharedPreferences("LIST_OF_LISTS", Context.MODE_PRIVATE).edit().remove(list.name).apply()
        setListView()
    }

    private fun setListView() {
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        val listOfLists: MutableList<SingleList> = mutableListOf()
        val sharedPreferences = this.getSharedPreferences("LIST_OF_LISTS", Context.MODE_PRIVATE)
        val keys = sharedPreferences.all.keys
        for(key in keys){
            val groceryString = sharedPreferences.getString(key, "")!!
            val groceryList = GroceryList(sharedPreferences.getString(key, "")!!, key)
            listOfLists.add(SingleList(groceryList.listName, groceryList.countOfItems, groceryList.countOfBought, groceryList.checked))
        }

        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.adapter = GroceryListAdapter(listOfLists, this, this.supportActionBar!!)
    }

    private fun openFragment() {
        var mainFragment: CreateNewList = CreateNewList()

        supportFragmentManager.beginTransaction()
            .add(R.id.framgent_container, mainFragment).setCustomAnimations(R.anim.enter_from_right, R.anim.exit_from_right)
            .commit()
    }

}

