package ds.project.tadaktadakfront

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ds.project.tadaktadakfront.contract.*
import ds.project.tadaktadakfront.contract.model.entity.Contract
import ds.project.tadaktadakfront.contract.view.adapter.ContractAdapter
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

    lateinit var mainActivity: MainActivity
    private val newcontactActivityRequestCode = 1

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

        val recyclerView : RecyclerView= view.findViewById(R.id.contact_recyclerview)
        val adapter = ContractAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(activity?.applicationContext)

        val name: String = requireActivity().intent.getStringExtra("name").toString()
        val number: String = requireActivity().intent.getStringExtra("number").toString()
        val address: String = requireActivity().intent.getStringExtra("address").toString()

        adapter.submitList(arrayListOf(Contract(null, name, number, address)))

        view.add_button.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?){
                val add = getView()?.findViewById<FloatingActionButton>(R.id.add_button)
                add?.setOnClickListener{
                    val intent = Intent(activity, NewContractActivity::class.java)
                    startActivity(intent)
                }
            }
        })
        return view
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
