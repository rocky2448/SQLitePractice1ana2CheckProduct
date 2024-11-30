package com.example.sqlite2checkproducts

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    var products: MutableList<Product> = mutableListOf()
    var listAdapter: MyListAdapter? = null
    val dataBase = DBHelper(this)
    var updateId: Int = 1
    private lateinit var toolbarMain: Toolbar
    private lateinit var productIdTV: TextView
    private lateinit var productNameET: EditText
    private lateinit var productWeightET: EditText
    private lateinit var productPriceET: EditText
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

        toolbarMain = findViewById(R.id.toolbarMain)
        productIdTV = findViewById(R.id.productIdTV)
        productNameET = findViewById(R.id.productNameET)
        productWeightET = findViewById(R.id.productWeightET)
        productPriceET = findViewById(R.id.productPriceET)
        listViewLV = findViewById(R.id.listViewLV)
        saveBTN = findViewById(R.id.saveBTN)
        updateBTN = findViewById(R.id.updateBTN)
        deleteBTN = findViewById(R.id.deleteBTN)

        viewDataAdapter()
        updateIdInScreen()

        setSupportActionBar(toolbarMain)
        title = "Корзина"
        toolbarMain.subtitle = "by Rocky"
        toolbarMain.setLogo(R.drawable.ic_toolbar)

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

    private fun viewDataAdapter() {
        products = dataBase.readProduct()
        listAdapter = MyListAdapter(this, products)
        listViewLV.adapter = listAdapter
        listAdapter?.notifyDataSetChanged()
        productIdTV.text = updateId.toString()
    }

    private fun updateIdInScreen() {
        updateId = products[products.size - 1].productId + 1
        productIdTV.text = updateId.toString()
    }

    private fun saveRecord() {
        if (products.isNotEmpty()) {
            updateId
        }else updateId = 1

        val name = productNameET.text.toString()
        val weight = productWeightET.text.toString()
        val price = productPriceET.text.toString()
        if (updateId.toString() != "" && name.trim() != "" && weight.trim() != "" && price.trim() != "") {
            val product = Product(updateId, name, weight, price)
            products.add(product)
            dataBase.addProduct(product)
            Toast.makeText(applicationContext, "Запись добавлена", Toast.LENGTH_SHORT).show()
            productIdTV.text = ""
            productNameET.text.clear()
            productWeightET.text.clear()
            productPriceET.text.clear()
            updateId++
            viewDataAdapter()
        }

    }

    private fun updateRecord() {
        val dialogBuilder = AlertDialog.Builder(this)
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.update_dialog, null)
        dialogBuilder.setView(dialogView)
        val editId = dialogView.findViewById<EditText>(R.id.updateIdET)
        val editName = dialogView.findViewById<EditText>(R.id.updateNameET)
        val editWeight = dialogView.findViewById<EditText>(R.id.updateWeightET)
        val editPrice = dialogView.findViewById<EditText>(R.id.updatePriceET)

        dialogBuilder.setTitle("Обновить запись")
        dialogBuilder.setMessage("Введите данные ниже")
        dialogBuilder.setPositiveButton("Обновить") { _, _ ->
            updateId = editId.text.toString().toInt()
            val updateName = editName.text.toString()
            val updateWeight = editWeight.text.toString()
            val updatePrice = editPrice.text.toString()
            if (updateId.toString().trim() != "" && updateName.trim() != "" && updateWeight.trim() != "" && updatePrice.trim() != "") {
                val product = Product(updateId, updateName, updateWeight, updatePrice)
                dataBase.updateProduct(product)
                viewDataAdapter()
                updateIdInScreen()
                Toast.makeText(applicationContext, "Запись обновлена", Toast.LENGTH_SHORT).show()
            }
        }
        dialogBuilder.setNegativeButton("Отмена") {dialog, which -> }
        dialogBuilder.create().show()
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
                val product = Product(Integer.parseInt(deleteId), "", "", "")
                dataBase.deleteProduct(product)
                viewDataAdapter()
                updateIdInScreen()
                Toast.makeText(applicationContext, "Запись удалена", Toast.LENGTH_SHORT).show()
            }
        }
        dialogBuilder.setNegativeButton("Отмена") { _, _ -> }
        dialogBuilder.create().show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.exitMenuMain -> finishAffinity()
        }
        return super.onOptionsItemSelected(item)
    }
}