package com.example.assignment2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.rv_item.view.*

class ProductAdapter(val productList: MutableList<Product>) :
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.rv_item, parent, false))
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productList[position]
        holder.itemView.apply {
            TextViewProductName.text = product.productName
            TextViewProductCategory.text = product.productCategory
            TextViewProductPrice.text = product.productPrice
            TextViewProductGst.text = product.productGST
            TextViewProductDeliveryCharges.text = product.productDeliveryCharges
        }
        Glide.with(holder.itemView).load(product.productImage).placeholder(R.drawable.image_placeholder)
            .into(holder.itemView.ImageViewProduct)
    }

    override fun getItemCount(): Int {
        return productList.size
    }
}