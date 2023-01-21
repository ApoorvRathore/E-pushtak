package com.apoorv.e_pushtak.activity

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.room.Room
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.apoorv.e_pushtak.R
import com.apoorv.e_pushtak.database.BookDatabase
import com.apoorv.e_pushtak.database.BookEntity
import com.apoorv.e_pushtak.util.ConnectionManager
import com.squareup.picasso.Picasso
import org.json.JSONObject


class DescriptionActivity : AppCompatActivity() {
    private lateinit var toolbarText: TextView
    private lateinit var txtBookName: TextView
    private lateinit var imgBackbtn: ImageButton
    private lateinit var txtBookAuthor: TextView
    private lateinit var txtBookPrice: TextView
    private lateinit var txtBookRating: TextView
    private lateinit var btnAddToFav: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var txtBookDesc: TextView
    private lateinit var imgBookImage: ImageView
    private lateinit var progressLayout: RelativeLayout
    private lateinit var toolbar: Toolbar
    private var bookId: String? = "100"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_description)
        txtBookName = findViewById(R.id.txtbookName)
        txtBookAuthor = findViewById(R.id.txtNameOfAuthor)
        txtBookPrice = findViewById(R.id.txtCost)
        imgBackbtn = findViewById(R.id.imgBackbtn)
        txtBookRating = findViewById(R.id.txtBookRating)
        txtBookDesc = findViewById(R.id.txtBookDesc)
        btnAddToFav = findViewById(R.id.btnAddToFavourite)
        imgBookImage = findViewById(R.id.imgBookImage)
        progressBar = findViewById(R.id.progressBar)
        progressBar.visibility = View.VISIBLE
        progressLayout = findViewById(R.id.progressLayout)
        progressLayout.visibility = View.VISIBLE
        toolbar = findViewById(R.id.toolbar)
        toolbarText = findViewById(R.id.toolbarText)
        toolbarText.text = "Book Details"

        if (intent != null) {
            bookId = intent.getStringExtra("book_id")
        } else {
            finish()
            Toast.makeText(this@DescriptionActivity, "Some error occured", Toast.LENGTH_SHORT).show()
        }
        if (bookId == "100") {
            finish()
            Toast.makeText(this@DescriptionActivity, "Some unexpected error ocurred", Toast.LENGTH_SHORT).show()
        }
        val queue = Volley.newRequestQueue(this@DescriptionActivity)
        val url = "http://13.235.250.119/v1/book/get_book/"

        val jsonParams = JSONObject()
        jsonParams.put("book_id", bookId)
        if (ConnectionManager().checkConnectivity(this@DescriptionActivity)) {

            val jsonRequest = object : JsonObjectRequest(Method.POST, url, jsonParams, Response.Listener {

                try {
                    val success = it.getBoolean("success")
                    if (success) {
                        val bookJsonObject = it.getJSONObject("book_data")
                        progressLayout.visibility = View.GONE
                        val bookImageUrl = bookJsonObject.getString("image")
                        Picasso.get().load(bookJsonObject.getString("image")).into(imgBookImage)
                        txtBookName.text = bookJsonObject.getString("name")
                        txtBookAuthor.text = bookJsonObject.getString("author")
                        txtBookPrice.text = bookJsonObject.getString("price")
                        txtBookRating.text = bookJsonObject.getString("rating")
                        txtBookDesc.text = bookJsonObject.getString("description")

                        val bookEntity = BookEntity(
                            bookId?.toInt() as Int,
                            txtBookName.text.toString(),
                            txtBookAuthor.text.toString(),
                            txtBookPrice.text.toString(),
                            txtBookRating.text.toString(),
                            txtBookDesc.text.toString(),
                            bookImageUrl
                        )

                        btnAddToFav.setOnClickListener {
                            if (!DBAsyncTask(this@DescriptionActivity, bookEntity, 1).execute().get()) {
                                val async = DBAsyncTask(this@DescriptionActivity, bookEntity, 2).execute()
                                val result = async.get()
                                if (result) {
                                    Toast.makeText(this@DescriptionActivity, "Added to Favourite", Toast.LENGTH_SHORT)
                                        .show()
                                    btnAddToFav.text = "Remove from Favourite"
                                    val favColor = ContextCompat.getColor(this@DescriptionActivity, R.color.purple_700)
                                    btnAddToFav.setBackgroundColor(favColor)
                                } else {
                                    Toast.makeText(this@DescriptionActivity, "Some error ocurred ", Toast.LENGTH_SHORT)
                                        .show()
                                }
                            } else {
                                val async = DBAsyncTask(this@DescriptionActivity, bookEntity, 3).execute()
                                val result = async.get()
                                if (result) {
                                    Toast.makeText(
                                        this@DescriptionActivity, "Removed From Favourite", Toast
                                            .LENGTH_SHORT
                                    ).show()
                                    btnAddToFav.text = "Add To Favourite"
                                    val noFavColor =
                                        ContextCompat.getColor(this@DescriptionActivity, R.color.color_primary)
                                    btnAddToFav.setBackgroundColor(noFavColor)
                                } else {
                                    Toast.makeText(this@DescriptionActivity, "Some error ocurred ", Toast.LENGTH_SHORT)
                                        .show()
                                }
                            }
                        }
                    } else {
                        Toast.makeText(this@DescriptionActivity, "Some error occured", Toast.LENGTH_SHORT).show()

                    }
                } catch (e: Exception) {
                    Toast.makeText(
                        this@DescriptionActivity,
                        "Unexpected error ocurred due to ${e.message}",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }


            }, Response.ErrorListener {
                Toast.makeText(this@DescriptionActivity, "Volley error $it", Toast.LENGTH_SHORT).show()
            }) {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Content-type"] = "application/json"
                    headers["token"] = "9bf534118365f1"
                    return headers
                }
            }
            queue.add(jsonRequest)
        } else {
            val dialog = AlertDialog.Builder(this@DescriptionActivity)
            dialog.setTitle("Error")
            dialog.setMessage("Internet Connection not found")
            dialog.setPositiveButton("Open Setting") { _, _ ->
                val settingIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingIntent)
                finish()
            }
            dialog.setNegativeButton("Exit") { _, _ ->
                ActivityCompat.finishAffinity(this@DescriptionActivity)
            }
            dialog.create()
            dialog.show()
        }

        imgBackbtn.setOnClickListener {
            val intent = Intent(this@DescriptionActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    class DBAsyncTask(val context: Context, private val bookEntity: BookEntity, private val mode: Int) :
        AsyncTask<Void, Void, Boolean>() {
        /*
        Mode-1 -> Check the DB if the book is favourite or not
        Mode-2 -> Save the  book into DB as favourite
        Mode-3 -> Remove the favourite book
        * */

        private val db = Room.databaseBuilder(context, BookDatabase::class.java, "books-db").build()


        override fun doInBackground(vararg p0: Void?): Boolean {
            when (mode) {
                1 -> {
//                    Check the DB if the book is favourite or not
                    val book: BookEntity? = db.bookDao().getBookById(bookEntity.book_id.toString())
                    db.close()
                    return book != null
                }
                2 -> {
//                    Save the  book into DB as favourite
                    db.bookDao().insertBook(bookEntity)
                    return true
                }
                3 -> {
//                    Remove the favourite book
                    db.bookDao().deleteBook(bookEntity)
                    return true
                }
            }

            return false
        }

    }
}




