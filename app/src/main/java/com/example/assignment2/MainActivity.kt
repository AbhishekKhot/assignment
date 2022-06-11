package com.example.assignment2

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val firebaseFirestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ProgressBarMain.visibility = View.VISIBLE

        val list = mutableListOf<Product>()
        val adapter = ProductAdapter(list)

        RecyclerViewMain.apply {
            this.layoutManager = LinearLayoutManager(this@MainActivity)
            this.adapter = adapter
        }

        firebaseFirestore.collection("Products").get()
            .addOnCompleteListener {
                list.clear()
                for (snapshot in it.result) {

                    val product = Product(
                        snapshot.getString("id"),
                        snapshot.getString("productName"),
                        snapshot.getString("productCategory"),
                        snapshot.getString("productPrice"),
                        snapshot.getString("productGST"),
                        snapshot.getString("productDeliveryCharges"),
                        snapshot.getString("productImage"),
                    )
                    list.add(product)
                }
                ProgressBarMain.visibility = View.INVISIBLE
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            }
    }
}