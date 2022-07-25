package com.example.logisticsestimate

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.logisticsestimate.databinding.ItemTermsBinding

class TermsData (val name: String, val content : String)

// 서치뷰를 위한 Filterable() implements
class TermsAdapter (val DataList: ArrayList<TermsData>, val context: Context): RecyclerView.Adapter<TermsAdapter.TermsViewHolder>(), Filterable {

    // 서치뷰 getFilter()에 필요한 List
    private var filteredList = DataList

    inner class TermsViewHolder(binding: ItemTermsBinding) : RecyclerView.ViewHolder(binding.root) {
        val termsName = binding.termsName
        val termsContent = binding.termsContent
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TermsViewHolder {
        val binding: ItemTermsBinding = ItemTermsBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return TermsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TermsViewHolder, position: Int) {

        val curData = filteredList!![position]

        holder.termsName.text = curData.name
        holder.termsContent.text = curData.content

        // RecyclerView 아이템 클릭 리스너
        holder.itemView.setOnClickListener {
            Toast.makeText(context, curData.name, Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount() = filteredList.size

    override fun getFilter(): Filter {
        return object: Filter() {
            override fun performFiltering(constraint: CharSequence): FilterResults {
                val charString = constraint.toString()
                filteredList = if(charString.isEmpty()) {
                    DataList
                }
                else {
                    val filteringList = ArrayList<TermsData>()

                    for (i in 0..DataList.size - 1) {
                        val tmpList = DataList[i].name.toLowerCase().replace(" ", "")
                        val tmpQuery = charString.toLowerCase().replace("  ( )", "")

                        if (tmpList.contains(tmpQuery)) {
                            filteringList.add(DataList[i])
                        }
                    }

                    if(filteringList.isEmpty()) {
                        Toast.makeText(context, "찾으시는 검색 결과가 없습니다.", Toast.LENGTH_SHORT).show()
                    }

                    filteringList
                }

                val filterResults = FilterResults()
                filterResults.values = filteredList

                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredList = results?.values as ArrayList<TermsData>

                println(filteredList.size)

                notifyDataSetChanged()
            }
        }
    }
}