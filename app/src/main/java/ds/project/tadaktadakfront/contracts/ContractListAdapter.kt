package ds.project.tadaktadakfront.contracts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ds.project.tadaktadakfront.R

class ContractListAdapter : ListAdapter<Contract, ContractListAdapter.ContractViewHolder>(CONTRACTS_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContractViewHolder {
        return ContractViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ContractViewHolder, position: Int) {
       val current = getItem(position)
        holder.bind(current.content)
    }

    class ContractViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val ContractItemView: TextView =itemView.findViewById(R.id.textView)

        fun bind(text: String?){
            ContractItemView.text = text
        }
        companion object {
            fun create(parent: ViewGroup): ContractViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.recyclerview_item, parent, false)
                return ContractViewHolder(view)
            }
        }
    }

    companion object {
        private val CONTRACTS_COMPARATOR = object : DiffUtil.ItemCallback<Contract>() {
            override fun areItemsTheSame(oldItem: Contract, newItem: Contract): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: Contract, newItem: Contract): Boolean {
                return oldItem.content == newItem.content
            }
        }
    }
}