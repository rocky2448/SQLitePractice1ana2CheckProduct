package com.example.sqlite2checkproducts

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class MyListAdapter(private val context: Context, userList: MutableList<User>) :
ArrayAdapter<User>(context, R.layout.list_item, userList){
    @SuppressLint("SetTextI18n")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val user = getItem(position)
        var view = convertView
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
        }
        val idText = view?.findViewById<TextView>(R.id.idTV)
        val nameText = view?.findViewById<TextView>(R.id.nameTV)
        val emailText = view?.findViewById<TextView>(R.id.emailTV)
        idText?.text = "id: ${user?.userId}"
        nameText?.text = "Имя: ${user?.userName}"
        emailText?.text = "Email: ${user?.userEmail}"
        return view!!
    }
}