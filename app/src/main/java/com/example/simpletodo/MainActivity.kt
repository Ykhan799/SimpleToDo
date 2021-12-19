package com.example.simpletodo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {

    // initializes empty listOfTasks
    var listOfTasks = mutableListOf<String>()
    lateinit var adapter: TaskItemAdapter

    // Connects the UI with the backend
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val onLongClickListener = object : TaskItemAdapter.OnLongClickListener {
            override fun onItemLongClicked(position: Int) {
                // Remove the item from the list
                listOfTasks.removeAt(position)

                // Let the adapter know the list is changed
                adapter.notifyDataSetChanged()

                // Stores items to text file
                saveItems()

                // Displays to the user that the selected task is deleted
                Toast.makeText(applicationContext, "Task deleted from list", Toast.LENGTH_SHORT).show()

            }

        }

        // Populates the listOfTasks
        loadItems()
        
        // Lookup the recyclerview in activity layout
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        // Create adapter passing in the sample user data
        adapter = TaskItemAdapter(listOfTasks, onLongClickListener)
        // Attach the adapter to the recyclerview to populate items
        recyclerView.adapter = adapter
        // Set layout manager to position the items
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Sets up the add button for user to add items to the list
        val inputTextField = findViewById<EditText>(R.id.addTaskField)

        // Detects when the user clicks on the add button
        findViewById<Button>(R.id.button).setOnClickListener {
            // Gets the text entered by the user
            val userInputText = inputTextField.text.toString()

            // Add the user's text to the list
            listOfTasks.add(userInputText)

            // Tell the adapter position of recently inserted item
            adapter.notifyItemInserted(listOfTasks.size - 1)

            // Clear the Text field
            inputTextField.setText("")

            // Stores items to text file
            saveItems()

            // Displays to the user that the selected task is added to the list
            Toast.makeText(applicationContext, "Task added to list", Toast.LENGTH_SHORT).show()
        }
    }

    // Save the data user entered using a file

    // Function which gets the file
    fun getDataFile() : File {

        // Each line in the file represents a task
        return File(filesDir, "data.txt")
    }

    // Loads each line from the file from the data file
    fun loadItems() {
        try {
            listOfTasks = FileUtils.readLines(getDataFile(), Charset.defaultCharset())
        } catch(ioException: IOException) {
            ioException.printStackTrace()
        }
    }

    // Saves contents of tasks into a file
    fun saveItems() {
        try {
            FileUtils.writeLines(getDataFile(), listOfTasks)
        } catch (ioException: IOException) {
            ioException.printStackTrace()
        }
    }
}