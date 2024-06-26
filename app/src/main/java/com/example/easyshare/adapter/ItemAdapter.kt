package com.example.easyshare.adapter

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.easyshare.MainActivity
import com.example.easyshare.R
import com.example.easyshare.ShareActivity
import com.example.easyshare.model.Link
import com.google.android.material.card.MaterialCardView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.util.regex.Pattern


class ItemAdapter(private var dataSet: List<Link>) :
    RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleTextView: TextView = view.findViewById(R.id.title)
        val card:MaterialCardView = view.findViewById(R.id.linkCard)
        val icon:ImageView = view.findViewById(R.id.icon)
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.titleTextView.text = dataSet[position].title

        viewHolder.card.setOnClickListener {
            val intent = Intent(viewHolder.itemView.context, ShareActivity::class.java)
            intent.putExtra("title", dataSet[position].title)
            intent.putExtra("linkString", dataSet[position].linkString)
            intent.putExtra("id", dataSet[position].id)
            viewHolder.itemView.context.startActivity(intent)
        }

        viewHolder.icon.setOnClickListener {
            val clipboard: ClipboardManager? = viewHolder.icon.context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?
            val clip = ClipData.newPlainText("link", dataSet[position].linkString)
            clipboard?.setPrimaryClip(clip)
        }


        viewHolder.card.setOnLongClickListener {
            val alertDialog = MaterialAlertDialogBuilder(viewHolder.itemView.context)
                .setTitle("Delete link: ${dataSet[position].title}")
                .setMessage("Link will be lost")
                .setPositiveButton("Delete") { dialog, which ->
                    val linkDao = MainActivity.database.linkDao()
                    deleteLink(position,viewHolder.itemView.context)
                }
                .setNegativeButton("Cancel") { dialog, which ->
                    dialog.dismiss()
                }
                .create()
            alertDialog.show()
            return@setOnLongClickListener true
        }

    }

    fun deleteLink(position: Int,context: Context)
    {
        MainActivity.database.linkDao().delete(dataSet[position])
//        dataSet.removeAt(position)
//        notifyItemRemoved(position)
//        notifyItemRangeChanged(position,dataSet.size)
        Toast.makeText(context,"Link Deleted", Toast.LENGTH_LONG).show()
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

    fun updateDataset(dataSet:List<Link>)
    {
        this.dataSet=dataSet
        notifyDataSetChanged()
    }

}

fun validateLink(title:String,linkString:String,context: Context):Boolean
{
    if(title.isEmpty())
    {
        val alertDialog = MaterialAlertDialogBuilder(context)
            .setTitle("Invalid")
            .setMessage("Title cannot be empty")
            .create()
        alertDialog.show()
        return false
    }
    if(linkString.isEmpty())
    {
        val alertDialog = MaterialAlertDialogBuilder(context)
            .setTitle("Invalid")
            .setMessage("Link cannot be empty")
            .create()
        alertDialog.show()
        return false
    }
    fun isUrl(string: String): Boolean {
        // Regular expression for URL
        val urlRegex = "^(https?|ftp)://[^\\s/$.?#].\\S*$"
        val pattern = Pattern.compile(urlRegex, Pattern.CASE_INSENSITIVE)
        val matcher = pattern.matcher(string)
        return matcher.matches()
    }
    if(!isUrl(linkString))
    {
        val alertDialog = MaterialAlertDialogBuilder(context)
            .setTitle("Invalid")
            .setMessage("Link is invalid")
            .create()
        alertDialog.show()
        return false
    }

    return true
}

fun addLink(link: Link,context: Context):Boolean
{
    link.title = link.title.trim()
    link.linkString = link.linkString.trim()
    if(!validateLink(link.title, link.linkString,context))
    {
        return false
    }

    link.id = MainActivity.database.linkDao().insert(link)
//        dataSet.add(link)
//        notifyItemInserted(dataSet.size-1)
    Toast.makeText(context,"Adding Link successful",Toast.LENGTH_LONG).show()
    return true
}