package ds.project.tadaktadakfront

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ds.project.tadaktadakfront.contract_collection.Contract_Add
import ds.project.tadaktadakfront.contract_collection.model.entity.Contract
import ds.project.tadaktadakfront.contract_collection.view.adapter.ContractAdapter
import ds.project.tadaktadakfront.contract_collection.viewmodel.ContractViewModel
import ds.project.tadaktadakfront.contract_collection.viewmodel.ContractViewModelFactory
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


    // 1. Context를 할당할 변수를 프로퍼티로 선언(어디서든 사용할 수 있게)
    lateinit var mainActivity: MainActivity


    private val contractViewModel: ContractViewModel by viewModels {
        ContractViewModelFactory((activity?.application as ContractApplication).repository)
    }

    private lateinit var getResult: ActivityResultLauncher<Intent>



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

        val recyclerView = view.findViewById<RecyclerView>(R.id.contract_recyclerview)
        val adapter = ContractAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        contractViewModel.contracts.observe(this, Observer { contracts ->
            contracts.let { adapter.submitList(it) }
        })

        getResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                val name: String = it.data?.getStringExtra("name").toString()
                val number: String = it.data?.getStringExtra("number").toString()

                val contract = Contract(name, number)
                contractViewModel.insert(contract)
            } else {
                Toast.makeText(activity, "empty not saved", Toast.LENGTH_SHORT).show()
            }
        }
        view.add_button.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                val addButton = getView()?.findViewById<FloatingActionButton>(R.id.add_button)
                addButton?.setOnClickListener {
                    val intent = Intent(activity, Contract_Add::class.java)
                    getResult.launch(intent)
                }
            }
        })

        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        // 2. Context를 액티비티로 형변환해서 할당
        mainActivity = context as MainActivity
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