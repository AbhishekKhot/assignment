package com.example.assignment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.HashMap

private const val REQUEST_CODE_IMAGE_PICK = 0

class MainActivity : AppCompatActivity() {

    private lateinit var imageUri: Uri
    private val firebaseFirestore = FirebaseFirestore.getInstance()
    private val storageReference = Firebase.storage.reference
    private val auth = FirebaseAuth.getInstance()
    private var isImageSelected = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ImageViewProduct.setOnClickListener {
            Intent(Intent.ACTION_GET_CONTENT).also {
                it.type = "image/*"
                startActivityForResult(it, REQUEST_CODE_IMAGE_PICK)
            }
        }


        ButtonUploadProduct.setOnClickListener { v ->
            val imageRef = storageReference.child("Product images").child(auth.uid.toString())
                .child(UUID.randomUUID().toString())

            if (isImageSelected) {
                imageRef.putFile(imageUri).addOnCompleteListener {
                    if (it.isSuccessful) {
                        imageRef.downloadUrl.addOnSuccessListener {
                            val product_map: HashMap<String, Any?> = HashMap()
                            val id = UUID.randomUUID().toString()
                            product_map["id"] = id
                            product_map["productName"] = EditTextProductName.text.toString()
                            product_map["productCategory"] = EditTextProductCategory.text.toString()
                            product_map["productPrice"] = EditTextProductPrice.text.toString()
                            product_map["productGST"] = EditTextProductGst.text.toString()
                            product_map["productDeliveryCharges"] =
                                EditTextProductDeliveryCharges.text.toString()
                            product_map["productImage"] = it.toString()

                            firebaseFirestore.collection("Products").document(id).set(product_map)
                                .addOnCompleteListener {
                                    Snackbar.make(v,
                                        "Successfully uploaded data to cloud database",
                                        Snackbar.LENGTH_SHORT).show()
                                }
                        }
                    } else {
                        Snackbar.make(v, it.exception?.message.toString(), Snackbar.LENGTH_SHORT)
                            .show()
                    }
                }
            } else {
                Snackbar.make(v, "Please select product image", Snackbar.LENGTH_SHORT).show()
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_IMAGE_PICK) {
            data?.data?.let {
                imageUri = it
                ImageViewProduct.setImageURI(it)
                isImageSelected = true
            }
        }
    }
}