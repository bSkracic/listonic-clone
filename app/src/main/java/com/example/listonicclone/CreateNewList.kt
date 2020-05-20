package com.example.listonicclone

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.NavUtils
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_create_new_list.*
import kotlinx.android.synthetic.main.fragment_create_new_list.view.*
import java.lang.Exception


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CreateNewList.newInstance] factory method to
 * create an instance of this fragment.
 */
class CreateNewList : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view: View = inflater!!.inflate(R.layout.fragment_create_new_list, container, false)

        view.create_button.setOnClickListener { view ->
            val sharedPref = activity?.getSharedPreferences("LIST_OF_LISTS", Context.MODE_PRIVATE)
            val listName = editName.text.toString()
            if(listName == ""){
                Toast.makeText(context, "Ime liste ne smije biti prazno!", Toast.LENGTH_SHORT).show()
            }
           else {
                if(sharedPref?.all?.containsKey(listName)!!){
                    Toast.makeText(context, "Lista toga imena već postoji!", Toast.LENGTH_SHORT).show()
                }else{
                    //put list name and new list in preferences named LIST_OF_LISTS
                    sharedPref?.edit()?.putString(listName, "false")!!.apply()
                    val intent = Intent(activity, GroceryListView::class.java)
                    intent.putExtra("LIST_NAME", listName)
                    startActivity(intent)
                }
            }
        }

        view.close.setOnClickListener{view->
            val intent = Intent(activity, MainActivity::class.java)
            startActivity(intent)
        }
        return view
    }

    fun isNameTaken(list: MutableList<String>, name: String): Boolean{
        for(i in 0 until list.size){
            if(list[i] == name){
                return true;
            }
        }
        return false;
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CreateNewList.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CreateNewList().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
