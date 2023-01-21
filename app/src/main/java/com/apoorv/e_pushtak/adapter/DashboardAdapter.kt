package com.apoorv.e_pushtak.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView
import com.apoorv.e_pushtak.R
import com.apoorv.e_pushtak.activity.DescriptionActivity
import com.apoorv.e_pushtak.modal.Book
import com.squareup.picasso.Picasso

class DashboardAdapter(val context:Context,private val itemList:ArrayList<Book>): RecyclerView.Adapter<DashboardAdapter
.DashboardViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_dashboard_single_row,parent,false)
        return DashboardViewHolder(view)
    }

    override fun onBindViewHolder(holder: DashboardViewHolder, position: Int) {
        val book = itemList[position]
        holder.textBookName.text = book.bookName
        holder.textBookAuthor.text = book.bookAuthor
        holder.txtBookPrice.text = book.bookPrice
        holder.txtRating.text = book.bookRating
//        holder.imgBookImage.setImageResource(book.bookImage)
        Picasso.get().load(book.bookImage).error(R.drawable.logo).into(holder.imgBookImage)
        holder.llContent.setOnClickListener {
            val intent = Intent(context,DescriptionActivity::class.java)
            intent.putExtra("book_id",book.bookId)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
    class DashboardViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textBookName: TextView = view.findViewById(R.id.txtbookName)
        val textBookAuthor:TextView = view.findViewById(R.id.txtNameOfAuthor)
        val txtBookPrice:TextView = view.findViewById(R.id.txtCost)
        val txtRating:TextView = view.findViewById(R.id.txtRating)
        val imgBookImage:ImageView = view.findViewById(R.id.imgBookImage)
        val llContent:LinearLayout = view.findViewById(R.id.llcontect)

    }
}
