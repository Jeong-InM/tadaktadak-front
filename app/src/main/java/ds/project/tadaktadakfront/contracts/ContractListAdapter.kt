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
        holder.bind(current)
    }

    class ContractViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val nameTv = itemView.findViewById<TextView>(R.id.item_tv_name)
        private val numberTv = itemView.findViewById<TextView>(R.id.item_tv_number)
        private val addressTv = itemView.findViewById<TextView>(R.id.item_tv_address)

        fun bind(contract: Contract){
            nameTv.text = contract.name
            numberTv.text=contract.number
            addressTv.text=contract.address
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
                return oldItem.number == newItem.number
            }
        }
    }
}