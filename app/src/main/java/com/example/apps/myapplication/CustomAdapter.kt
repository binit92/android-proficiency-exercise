package com.example.apps.myapplication

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso

class CustomAdapter(val context: Context, val entries: ArrayList<Sync.Entry>?) : BaseAdapter() {

    private val TAG = CustomAdapter::class::simpleName.toString()
    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
        /*if(inflater == null){
            inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater?
        }
        */

        val view: View?
        val holder: Holder
        if (convertView == null) {
            view = this.inflater.inflate(R.layout.list_layout, parent, false)
            holder = Holder(view)
            view.tag = holder
        } else {
            view = convertView
            holder = view.tag as Holder
        }
        //val rowView = inflater!!.inflate(R.layout.list_layout,parent,false)

        val entry: Sync.Entry = getItem(position)

        //val holder = Holder(rowView)
        val title: String? = entry.title
        Log.i(TAG, "getView: title" + title)
        holder.title.visibility = View.VISIBLE
        holder.title.text = title


        val desc: String? = entry.desc
        Log.i(TAG, "getView: desc: " + desc)
        if (desc != null && !desc.isEmpty() && !desc.equals("null")) {
            holder.desc.visibility = View.VISIBLE
            holder.desc.text = desc
        } else {
            holder.desc.visibility = View.GONE
        }

        val url: String? = entry.imageref
        Log.i(TAG, "getView: imageref: " + url)
        if (url != null && !url.isEmpty()) {
            holder.image.visibility = View.VISIBLE
            Picasso.get().isLoggingEnabled = true
            Picasso.get().load(url).into(holder.image)
        } else {
            holder.image.visibility = View.GONE
        }

        return view
    }

    override fun getItem(p0: Int): Sync.Entry {
        return entries!![p0]

    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getCount(): Int {
        return entries!!.size
    }

    class Holder(val view: View) {
        val title: TextView
        val desc: TextView
        val image: ImageView

        // initializer block
        init {
            title = view.findViewById(R.id.row_title)
            desc = view.findViewById(R.id.row_description)
            image = view.findViewById(R.id.row_image)
        }

    }
}