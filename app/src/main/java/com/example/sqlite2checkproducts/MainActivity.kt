package com.example.sqlite2checkproducts

import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Email
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    var users: MutableList<User> = mutableListOf(
        User(24, "Roman", "rocky2448")
    )
    var listAdapter: MyListAdapter? = null
    val dataBase = DBHelper(this)
    private lateinit var userIdET: EditText
    private lateinit var userNameET: EditText
    private lateinit var userEmailET: EditText
    private lateinit var listViewLV: ListView
    private lateinit var saveBTN: Button
    private lateinit var updateBTN: Button
    private lateinit var deleteBTN: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        userIdET = findViewById(R.id.userIdET)
        userNameET = findViewById(R.id.userNameET)
        userEmailET = findViewById(R.id.userEmailET)
        listViewLV = findViewById(R.id.listViewLV)
        saveBTN = findViewById(R.id.saveBTN)
        updateBTN = findViewById(R.id.updateBTN)
        deleteBTN = findViewById(R.id.deleteBTN)
        viewDataAdapter()

        saveBTN.setOnClickListener{
            saveRecord()
        }


    }



    override fun onResume() {
        super.onResume()
        updateBTN.setOnClickListener {
            updateRecord()
        }
        deleteBTN.setOnClickListener {
            deleteRecord()
        }
    }

    private fun updateRecord() {
        val dialogBuilder = AlertDialog.Builder(this)
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.update_dialog, null)
        dialogBuilder.setView(dialogView)
        val editId = dialogView.findViewById<EditText>(R.id.updateIdET)
        val editName = dialogView.findViewById<EditText>(R.id.updateNameET)
        val editEmail = dialogView.findViewById<EditText>(R.id.updateEmailET)

        dialogBuilder.setTitle("Обновить запись")
        dialogBuilder.setMessage("Введите данные ниже")
        dialogBuilder.setPositiveButton("Обновить") { _, _ ->
            val updateId = editId.text.toString()
            val updateName = editName.text.toString()
            val updateEmail = editEmail.text.toString()
            if (updateId.trim() != "" && updateName.trim() != "" && updateEmail.trim() != "") {
                val user = User(Integer.parseInt(updateId), updateName, updateEmail)
                dataBase.updateUser(user)
                viewDataAdapter()
                Toast.makeText(applicationContext, "Запись обновлена", Toast.LENGTH_SHORT).show()
            }
        }
        dialogBuilder.setNegativeButton("Отмена") {dialog, which -> }
        dialogBuilder.create().show()
    }

    private fun viewDataAdapter() {
        users = dataBase.readUser()
        listAdapter = MyListAdapter(this, users)
        listViewLV.adapter = listAdapter
        listAdapter?.notifyDataSetChanged()
    }

    private fun saveRecord() {
        val id = userIdET.text.toString()
        val name = userNameET.text.toString()
        val email = userEmailET.text.toString()
        if (id.trim() != "" && name.trim() != "" && email.trim() != "") {
            val user = User(Integer.parseInt(id), name, email)
            users.add(user)
            Toast.makeText(applicationContext, "Запись добавлена", Toast.LENGTH_SHORT).show()
            userIdET.text.clear()
            userNameET.text.clear()
            userEmailET.text.clear()
            viewDataAdapter()
        }

    }

    private fun deleteRecord() {
        val dialogBuilder = AlertDialog.Builder(this)
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.delete_dialog, null)
        dialogBuilder.setView(dialogView)

        val chooseDeleteId = dialogView.findViewById<EditText>(R.id.deleteIdET)

        dialogBuilder.setTitle("Удалить запись")
        dialogBuilder.setMessage("Введите id")
        dialogBuilder.setPositiveButton("Удалить") { _, _ ->
            val deleteId = chooseDeleteId.text.toString()
            if (deleteId.trim() != "") {
                val user = User(Integer.parseInt(deleteId), "", "")
                dataBase.deleteUser(user)
                viewDataAdapter()
                Toast.makeText(applicationContext, "Запись удалена", Toast.LENGTH_SHORT).show()
            }
        }
        dialogBuilder.setNegativeButton("Отмена") { _, _ -> }
        dialogBuilder.create().show()
    }
}