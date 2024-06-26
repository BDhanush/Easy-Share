package com.example.easyshare

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.easyshare.adapter.ItemAdapter
import com.example.easyshare.adapter.addLink
import com.example.easyshare.databinding.ActivityMainBinding
import com.example.easyshare.databinding.AddLinkBinding
import com.example.easyshare.db.LinkDao
import com.example.easyshare.db.LinkDatabase
import com.example.easyshare.model.Link
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    companion object {
        lateinit var database: LinkDatabase
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        database =  Room.databaseBuilder(
            applicationContext,
            LinkDatabase::class.java,
            LinkDatabase.NAME
        ).allowMainThreadQueries().build()

        val linkDao: LinkDao = database.linkDao()

        val adapter = ItemAdapter(listOf())
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        linkDao.getAll().observe(this) { dataset ->
            adapter.updateDataset(dataset)
        }

        binding.addButton.setOnClickListener {
            val addLinkBinding = AddLinkBinding.inflate(layoutInflater)
            val addLinkView = addLinkBinding.root
            val alertDialog = MaterialAlertDialogBuilder(this)
                .setTitle("New Link")
                .setView(addLinkView)
                .setPositiveButton("Add") { dialog, which ->
                    val title = addLinkBinding.title.text.toString()
                    val link = addLinkBinding.link.text.toString()
                    addLink(Link(title,link),this)
                }
                .setNegativeButton("Cancel") { dialog, which ->
                    dialog.dismiss()
                }
                .create()
            alertDialog.show()
        }

    }

}