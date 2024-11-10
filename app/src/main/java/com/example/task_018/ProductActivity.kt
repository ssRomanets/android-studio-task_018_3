package com.example.task_018

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import kotlin.system.exitProcess
import android.widget.Toast
import androidx.annotation.Nullable

class ProductActivity : AppCompatActivity() {

    var product: Product? = null
    var photoProductUri: Uri? = null
    private val GALLERY_REQUEST = 382

    private lateinit var toolbarMain: Toolbar
    private lateinit var imageViewEditIV: ImageView
    private lateinit var productNameET: EditText
    private lateinit var productPriceET: EditText
    private lateinit var productDescriptionET: EditText

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)

        toolbarMain = findViewById(R.id.toolbarMain)
        setSupportActionBar(toolbarMain)
        title = "Активация продукта."

        imageViewEditIV = findViewById(R.id.imageViewEditIV)
        imageViewEditIV.setOnClickListener{
            val photoPickerIntent = Intent(Intent.ACTION_PICK)
            photoPickerIntent.type = "image/*"
            startActivityForResult(photoPickerIntent, GALLERY_REQUEST )
        }

        productNameET = findViewById(R.id.productNameET)
        productPriceET = findViewById(R.id.productPriceET)
        productDescriptionET = findViewById(R.id.productDescriptionET)

        product = intent.extras?.getSerializable("product") as Product
        photoProductUri = (Uri.parse(product?.image));

        imageViewEditIV.setImageURI(photoProductUri)
        productNameET.setText(product?.name)
        productPriceET.setText(product?.price)
        productDescriptionET.setText(product?.description)
    }

    override fun onActivityResult(requestedCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestedCode, resultCode, data)
        imageViewEditIV = findViewById(R.id.imageViewEditIV)
        when (requestedCode) {
            GALLERY_REQUEST -> if (resultCode === RESULT_OK) {
                photoProductUri = data?.data
                imageViewEditIV.setImageURI(photoProductUri)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.full_menu_name, menu)
        return  true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.backMenuMain->{
                val products = intent.getSerializableExtra("products")
                val item = intent.extras?.getInt("position")

                val newProduct = Product(
                    product?.name.toString(),
                    product?.price.toString(),
                    product?.description.toString(),
                    photoProductUri.toString()
                )

                val  list: MutableList<Product> = products as MutableList<Product>
                if (item != null) {
                    swap(item, newProduct, products)
                }

                val intentBack = Intent(this, ShopActivity::class.java)
                intentBack.putExtra("list", list as ArrayList<Product>)
                intentBack.putExtra("newCheck", false)
                startActivity(intentBack)
            }
            R.id.exitMenuMain->{
                moveTaskToBack(true);
                Toast.makeText(this,"Программа завершена", Toast.LENGTH_LONG).show()
                exitProcess(-1)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun swap(item: Int, product: Product, products: MutableList<Product>) {
        products.add(item + 1, product)
        products.removeAt(item)
    }
}