package com.example.task_018

import android.annotation.SuppressLint
import android.content.Intent

import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ListView
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import java.util.ArrayList
import kotlin.system.exitProcess

class ShopActivity : AppCompatActivity() {

    private val GALLERY_REQUEST = 382
    var photoProductUri: Uri? = null
    var products: MutableList<Product> = mutableListOf()
    var listAdapter: ListAdapter? = null

    private lateinit var toolbarMain: Toolbar

    private lateinit var listViewLV: ListView
    private lateinit var productNameET: EditText
    private lateinit var productPriceET: EditText
    private lateinit var productDescriptionET: EditText
    private lateinit var editImageIV: ImageView
    private lateinit var productSaveBTN: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop)

        toolbarMain = findViewById(R.id.toolbarMain)
        setSupportActionBar(toolbarMain)
        title = "Продуктовый магазин."

        init()

        editImageIV.setOnClickListener{
            val photoPickerIntent = Intent(Intent.ACTION_PICK)
            photoPickerIntent.type = "image/*"
            startActivityForResult(photoPickerIntent, GALLERY_REQUEST )
        }

        productSaveBTN.setOnClickListener{
            createProduct()
            listAdapter = ListAdapter(this@ShopActivity, products)
            listViewLV.adapter = listAdapter
            listAdapter?.notifyDataSetChanged()
            clearEditFields()
            listAdapter?.notifyDataSetChanged()
        }
        listViewLV.onItemClickListener =
            AdapterView.OnItemClickListener{parent, view, position, id ->
                val product = listAdapter!!.getItem(position)

                val intent = Intent(this, ProductActivity::class.java)
                intent.putExtra("product", product)
                intent.putExtra("products", this.products as ArrayList<Product>)
                intent.putExtra("position", position)
                startActivity(intent)
            }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return  true
    }

    override fun onResume() {
        super.onResume()
        var check = intent.extras?.getBoolean("newCheck")?: true
        if (!check) {
            products = intent.getSerializableExtra("list") as MutableList<Product>
            listAdapter = ListAdapter(this, products)
            check = true
        }
        listViewLV.adapter = listAdapter
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.exitMenuMain->{
                moveTaskToBack(true);
                Toast.makeText(this,"Программа завершена", Toast.LENGTH_LONG).show()
                exitProcess(-1)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun init() {
        listViewLV = findViewById(R.id.listViewLV)
        productNameET = findViewById(R.id.productNameET)
        productPriceET = findViewById(R.id.productPriceET)
        productDescriptionET = findViewById(R.id.productDescriptionET)
        editImageIV = findViewById(R.id.editImageIV)
        productSaveBTN = findViewById(R.id.productSaveBTN)
    }

    private fun createProduct() {
        val productName = productNameET.text.toString()
        val productPrice = productPriceET.text.toString()
        val productDescription = productDescriptionET.text.toString()
        val productImage = photoProductUri.toString()
        val product = Product(productName, productPrice, productDescription, productImage)
        products.add(product)
    }

    private fun clearEditFields() {
        productNameET.text.clear()
        productPriceET.text.clear()
        productDescriptionET.text.clear()
        editImageIV.setImageResource(R.drawable.product_ic)
    }

    override fun onActivityResult(requestedCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestedCode, resultCode, data)
        editImageIV = findViewById(R.id.editImageIV)
        when (requestedCode) {
            GALLERY_REQUEST -> if (resultCode === RESULT_OK) {
                photoProductUri = data?.data
                editImageIV.setImageURI(photoProductUri)
            }
        }
    }
}