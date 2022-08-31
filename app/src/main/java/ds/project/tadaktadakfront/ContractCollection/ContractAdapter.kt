package ds.project.tadaktadakfront.ContractCollection

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import ds.project.tadaktadakfront.R


class ContractAdapter(val contractItemClick: (Contract)->Unit, val contractLongItemClick: (Contract) -> Unit)
    : RecyclerView.Adapter<ContractAdapter.ViewHolder>(){
    private var contracts: List<Contract> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, i: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_contract, parent, false)
        return ViewHolder(view)

    }

    override fun getItemCount(): Int {
        return contracts.size
    }
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int)
    { viewHolder.bind(contracts[position])
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        private val nameTv = itemView.findViewById<TextView>(R.id.item_tv_name)
        private val numberTv = itemView.findViewById<TextView>(R.id.item_tv_number)
        private val initialTv = itemView.findViewById<TextView>(R.id.item_tv_initial)

        fun bind(contract: Contract) {
            nameTv.text = contract.name
            numberTv.text = contract.number
            initialTv.text = contract.initial.toString()

            itemView.setOnClickListener {
                contractItemClick(contract)
            }

            itemView.setOnLongClickListener {
                contractLongItemClick(contract)
                true
            }
        }
    }
    fun setContracts(contracts: List<Contract>) {
        this.contracts = contracts
        notifyDataSetChanged()
    }


}