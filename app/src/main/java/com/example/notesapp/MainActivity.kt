package com.example.notesapp

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar


class MainActivity : AppCompatActivity(),INotesRVAdapter {
    private lateinit var viewModel: NoteViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var recyclerView=findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager=LinearLayoutManager(this)
        val adapter=notesRVAdapter(this,this)

        recyclerView.adapter=adapter

        viewModel= ViewModelProvider(this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)).get(NoteViewModel::class.java)
        viewModel.allNotes.observe(this, Observer {list->
            list?.let {
                adapter.updatedList(it)
            }
        })

        val itemTouchHelper = ItemTouchHelper(object: ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT)
        {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false;
            }

            @SuppressLint("NotifyDataSetChanged", "ShowToast")
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            deleteNote(adapter.getNoteAtposition(viewHolder.adapterPosition))
                adapter.notifyItemRemoved(direction)
                adapter.notifyDataSetChanged()
            }
        })
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }


    private fun deleteNote(noteAtposition: Note) {
        viewModel.deleteNote(noteAtposition)

    }

    override fun onItemClicked(note: Note) {
        viewModel.deleteNote(note)
    }


    fun submitData(view: View) {
       val input = findViewById<EditText>(R.id.input)
        val noteText = input.text.toString()
        if(noteText.isNotEmpty())
        {
            viewModel.insertNote(Note(noteText))
        }
        input.text.clear()
    }
}