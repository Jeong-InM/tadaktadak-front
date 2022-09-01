package ds.project.tadaktadakfront.contract_collection.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ds.project.tadaktadakfront.contract_collection.model.entity.Contract

import ds.project.tadaktadakfront.R


class ContractAdapter: ListAdapter<Contract, ContractAdapter.ContractViewHolder>(ContractComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContractViewHolder {
        return ContractViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ContractViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)
    }

    class ContractViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById (R.id.item_textview_name)
        private val numberTextView: TextView = itemView.findViewById(R.id.item_textview_number)
        private val initialTextView: TextView = itemView.findViewById(R.id.item_tv_initial)

        fun bind(contract: Contract) {
            nameTextView.text = contract.name
            numberTextView.text = contract.number
            initialTextView.text = contract.name[0].uppercase()
        }

        companion object {
            fun create(parent: ViewGroup): ContractViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_contract, parent, false)
                return ContractViewHolder(view)
            }
        }
    }

    class ContractComparator : DiffUtil.ItemCallback<Contract>() {
        override fun areItemsTheSame(oldItem: Contract, newItem: Contract): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Contract, newItem: Contract): Boolean {
            return oldItem.number == newItem.number
        }
    }
}