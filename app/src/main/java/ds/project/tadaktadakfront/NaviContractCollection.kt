package ds.project.tadaktadakfront

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ds.project.tadaktadakfront.ContractCollection.Contract
import ds.project.tadaktadakfront.ContractCollection.ContractAdapter
import ds.project.tadaktadakfront.ContractCollection.ContractViewModel
import ds.project.tadaktadakfront.ContractCollection.Contract_Add
import kotlinx.android.synthetic.main.fragment_navi_contract_collection.*
import kotlinx.android.synthetic.main.fragment_navi_contract_collection.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [NaviContractCollection.newInstance] factory method to
 * create an instance of this fragment.
 */
class NaviContractCollection : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var contractViewModel: ContractViewModel



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }


    @SuppressLint("FragmentLiveDataObserve")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_navi_contract_collection, container, false)

        // Set contractItemClick & contractItemLongClick lambda

        val adapter = ContractAdapter({ contract ->
            val intent = Intent(context, Contract_Add::class.java)
            intent.putExtra(Contract_Add.EXTRA_CONTRACT_NAME, contract.name)
            intent.putExtra(Contract_Add.EXTRA_CONTRACT_NUMBER, contract.number)
            intent.putExtra(Contract_Add.EXTRA_CONTRACT_ID, contract.id)
            startActivity(intent)
        }, { contract ->
            deleteDialog(contract)
        })



        val lm = LinearLayoutManager(context)

        val mrecycleview = getView()?.findViewById<RecyclerView>(R.id.main_recycleview)


        mrecycleview?.adapter = adapter
        mrecycleview?.layoutManager = lm
        mrecycleview?.setHasFixedSize(true)

        contractViewModel = ViewModelProviders.of(this).get(ContractViewModel::class.java)
        contractViewModel.getAll().observe(viewLifecycleOwner, Observer<List<Contract>> { contracts ->
            adapter.setContracts(contracts!!)
        })


        view.main_button.setOnClickListener (object : View.OnClickListener {
            override fun onClick(p0: View?) {
                val add = getView()?.findViewById<Button>(R.id.main_button)
                add?.setOnClickListener{
                    val intent = Intent(activity, Contract_Add::class.java)
                    startActivity(intent)
                }
            }

        })



        return view
    }



    private fun deleteDialog(contract: Contract) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage("Delete selected contract?")
            .setNegativeButton("NO") { _, _ -> }
            .setPositiveButton("YES") { _, _ ->
                contractViewModel.delete(contract)
            }
        builder.show()
    }

    companion object {

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment NaviContractCollection.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            NaviContractCollection().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}