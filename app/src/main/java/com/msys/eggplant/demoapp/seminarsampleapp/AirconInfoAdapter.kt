package com.msys.eggplant.demoapp.seminarsampleapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

class AirconInfoAdapter(private val context: Context,
                        private val airconInfo: List<AirconInfo>,
                        private val onButtonClicked: (AirconInfo) -> Unit
) : BaseAdapter() {
    private val inflater = LayoutInflater.from(context)

    private val airconStatus = arrayListOf<Int>(android.R.drawable.presence_busy, android.R.drawable.presence_online)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: createView(parent)

        val status = getItem(position).status
        val location = getItem(position).location

        val viewHolder = view.tag as ViewHolder
        viewHolder.status.setImageResource(airconStatus[airconInfo[position].status])
        viewHolder.location.text = airconInfo[position].location
        viewHolder.info.text = airconInfo[position].mode + "　" + airconInfo[position].temp + "℃"

        viewHolder.row.setOnClickListener {
            // エアコン操作画面へ遷移
            onButtonClicked(airconInfo[position])
        }

        return view
    }

    override fun getItem(position: Int) = airconInfo[position]

    override fun getItemId(position: Int) = position.toLong()

    override fun getCount() = airconInfo.size

    private class ViewHolder(view: View) {
        val status = view.findViewById<ImageView>(R.id.aircon_status)
        val location = view.findViewById<TextView>(R.id.aircon_location)
        val info = view.findViewById<TextView>(R.id.aircon_info)
        val row = view.findViewById<LinearLayout>(R.id.list_row)
    }

    private fun createView(parent: ViewGroup?) : View {
        val view = inflater.inflate(R.layout.aircon_list_row, parent, false)
        view.tag = ViewHolder(view)
        return view
    }

}