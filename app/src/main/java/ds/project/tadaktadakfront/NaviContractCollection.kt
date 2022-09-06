package ds.project.tadaktadakfront

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ds.project.tadaktadakfront.contracts.*

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
    lateinit var mainActivity: MainActivity
    private val newcontractActivityRequestCode = 1
    private val contractViewModel: ContractViewModel by viewModels {
        ContractViewModelFactory((mainActivity.application as ContractsApplication).repository)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_navi_contract_collection, container, false)

        val recyclerView : RecyclerView= view.findViewById(R.id.recyclerview)
        val adapter = ContractListAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(activity?.applicationContext)

        contractViewModel.allContracts.observe(viewLifecycleOwner) { contracts ->
            contracts.let { adapter.submitList(it) }
        }

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intentData: Intent?) {
        super.onActivityResult(requestCode, resultCode, intentData)

        if (requestCode == newcontractActivityRequestCode && resultCode == Activity.RESULT_OK) {
            val name: String = arguments?.getString("name").toString()
            val number: String = arguments?.getString("number").toString()
            val address: String = arguments?.getString("address").toString()

            val contract = Contract(null, name, number, address)
            contractViewModel.insert(contract)
        } else {
            Toast.makeText(
                mainActivity.applicationContext,
                R.string.empty_not_saved,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun onAttach(context: Context) { //메인 context 자유롭게 사용
        super.onAttach(context)
        mainActivity=context as MainActivity
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
