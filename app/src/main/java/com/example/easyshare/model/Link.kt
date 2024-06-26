package com.example.easyshare.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Link(var title:String, var linkString:String, @PrimaryKey(autoGenerate = true) var id:Long = 0)
{

}