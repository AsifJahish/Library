package com.example.myprojectlibrary.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.myprojectlibrary.Model.Books
import com.example.myprojectlibrary.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.nio.charset.Charset
import android.content.Context


class Insert : Fragment() {
    private lateinit var title: EditText
    private lateinit var author: EditText
    private lateinit var category: EditText
    private lateinit var publisher: EditText
    private lateinit var isbn: EditText
    private lateinit var place: EditText
    private lateinit var edition: EditText
    private lateinit var editior: EditText
    private lateinit var notes : EditText
    private lateinit var condition :EditText
    private lateinit var cover :EditText
    private lateinit var langauge: EditText
    private lateinit var location: EditText
    private lateinit var translator: EditText
    private lateinit var saveBtn: Button
    private lateinit var importBtn: Button

    private lateinit var dbref: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_insert, container, false)



        importBtn= view.findViewById(R.id.buttonSave)

        saveBtn= view.findViewById(R.id.save)

        dbref = FirebaseDatabase.getInstance().reference.child("books")

        saveBtn.setOnClickListener{
            saveBookdata()
        }

        importBtn.setOnClickListener{
            importFile(requireContext())
        }
        return view
    }

    private fun saveBookdata() {

        val titletv = title.text.toString()
        val authortv = author.text.toString()
        val categorytv = category.text.toString()
        val publishertv = publisher.text.toString()
        val translatortv= translator.text.toString()
        val locationtv= location.text.toString()
        val isbntv= isbn.text.toString()
        val editiontv= edition.text.toString()
        val editortv= editior.text.toString()
        val placetv= place.text.toString()
        val notestv= notes.text.toString()
        val languagetv= langauge.text.toString()
        val covertv= cover.text.toString()
        val conditiontv= condition.text.toString()

        if (isbntv.isEmpty()) {
            isbn.error = "Please enter the ISBN"
        }

        if (titletv.isEmpty()) {
            title.error = "please write the title"
        }
        if (authortv.isEmpty()) {
            author.error = "please write the authorName"
        }
        if (categorytv.isEmpty()) {
            category.error = "please write the category"
        }
        if (publishertv.isEmpty()) {
            publisher.error = "please write the publisher"
        }


        val bookRef = dbref.child(isbntv).key!!

        val books= Books(isbntv, titletv, authortv, categorytv, publishertv, translatortv, locationtv,editiontv, editortv, placetv,
        notestv, languagetv, covertv,conditiontv)

        dbref.child(bookRef.toString()).setValue(books)
            .addOnCompleteListener{
                Toast.makeText(requireContext(), "completed", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener{err->
                Toast.makeText(requireContext(), "error ${err.message}", Toast.LENGTH_SHORT).show()
            }
    }



    private fun importFile(context: Context) {
        val fileName = "file.json" // Replace with your JSON file name
        val jsonString = readJsonFile(context, fileName)

        if (jsonString.isNullOrEmpty()) {
            // Handle error: Unable to read JSON file
            return
        }

        try {
            val jsonArray = JSONArray(jsonString)
            val booksRef = FirebaseDatabase.getInstance().reference.child("books")

            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)

                val isbn = jsonObject.optString("isbn", "")
                val title = jsonObject.optString("title", "")
                val author = jsonObject.optString("author", "")
                val publisher = jsonObject.optString("publisher", "")
                val placeOfPublication = jsonObject.optString("place", "")
                val editor = jsonObject.optString("editor", "")
                val edition = jsonObject.optString("edition", "")
                val category = jsonObject.optString("category", "")
                val language = jsonObject.optString("language", "")
                val translator = jsonObject.optString("translator", "")
                val coverType = jsonObject.optString("coverType", "")
                val condition = jsonObject.optString("condition", "")
                val shelfLocation = jsonObject.optString("shelf", "")
                val notes = jsonObject.optString("notes", "")

                val bookData = Books(
                    author = author,
                    category = category,
                    publisher = publisher,
                    title = title,
                    condition = condition,
                    coverType = coverType,
                    edition = edition,
                    editor = editor,
                    isbn = isbn,
                    language = language,
                    notes = notes,
                    place = placeOfPublication,
                    shelf = shelfLocation,
                    translator = translator
                )

                booksRef.child(isbn).setValue(bookData)
                    .addOnSuccessListener {
                        Toast.makeText(context, "Data imported successfully", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener { error ->
                        Toast.makeText(context, "Error importing data: ${error.message}", Toast.LENGTH_SHORT).show()
                    }
            }
        } catch (e: JSONException) {
            // Handle error: Invalid JSON format
            e.printStackTrace()
        }
    }


    private fun readJsonFile(context: Context, fileName: String): String? {
        return try {
            val inputStream: InputStream = context.assets.open(fileName)
            val reader = BufferedReader(InputStreamReader(inputStream))
            val stringBuilder = StringBuilder()
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                stringBuilder.append(line)
            }
            reader.close()
            stringBuilder.toString()
        } catch (e: IOException) {
            // Handle error: Unable to read file
            null
        }
    }
}