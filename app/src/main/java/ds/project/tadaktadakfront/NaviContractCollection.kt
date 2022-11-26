package ds.project.tadaktadakfront

import android.Manifest
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.browser.customtabs.CustomTabsClient.getPackageName
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ds.project.tadaktadakfront.contract.*
import ds.project.tadaktadakfront.contract.model.entity.Contract
import ds.project.tadaktadakfront.contract.view.adapter.ContractAdapter
import ds.project.tadaktadakfront.contract.view.callback.ContractApplication.Companion.db
import kotlinx.android.synthetic.main.fragment_navi_contract_collection.*
import kotlinx.android.synthetic.main.fragment_navi_contract_collection.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.apache.poi.hssf.usermodel.HSSFCell
import org.apache.poi.hssf.usermodel.HSSFRow
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import java.io.FileOutputStream

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
    private val workBook = HSSFWorkbook()
    private val WRITE_XLS_REQ_CODE = 1000

    lateinit var mainActivity: MainActivity
    private val newcontactActivityRequestCode = 1

    private lateinit var recyclerView : RecyclerView
    private lateinit var btnAllList: AppCompatButton
    private lateinit var btnAdd: FloatingActionButton
    private lateinit var adapter: ContractAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        return inflater.inflate(R.layout.fragment_navi_contract_collection, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initListener()
    }

    override fun onResume() {
        super.onResume()

        getContactList()
    }

    private fun initView() {
        btnAllList = requireView().findViewById(R.id.btn_all_list)
        btnAdd = requireView().findViewById(R.id.add_button)
        recyclerView = requireView().findViewById(R.id.contact_recyclerview)
        adapter = ContractAdapter()
        recyclerView.adapter = adapter
    }

    private fun initListener() {
        btnAllList.setOnClickListener {
            saveExcel()
        }
        btnAdd.setOnClickListener {
            startActivity(Intent(requireActivity(), NewContractActivity::class.java))
        }
    }

    private fun getContactList() {
        CoroutineScope(Dispatchers.IO).launch {
            db?.contractDao()?.getAll()?.collect {

                withContext(Dispatchers.Main) {
                    adapter.submitList(it)
                }
            }
        }
    }

    private fun saveExcel() {
        val sheet = workBook.createSheet() // 새로운 시트 생성
        var row: HSSFRow
        var cell: HSSFCell

        // 새로운 행 생성
        row = sheet.createRow(0)

        // 1번 셀 생성
        cell = row.createCell(0)
        // 1번 셀 값 입력
        cell.setCellValue("이름")
        // 2번 셀 생성
        cell = row.createCell(1)
        // 2번 셀 값 입력
        cell.setCellValue("전화번호")
        // 3번 셀 생성
        cell = row.createCell(2)
        // 3번 셀 값 입력
        cell.setCellValue("주소")

        val contactList = adapter.currentList as List<Contract>

        contactList.forEachIndexed { index, contract ->

            // 새로운 행 생성
            row = sheet.createRow(index + 1)

            // 1번 셀 생성
            cell = row.createCell(0)
            cell.setCellValue(contract.name)
            // 2번 셀 생성
            cell = row.createCell(1)
            cell.setCellValue(contract.number)
            // 3번 셀 생성
            cell = row.createCell(2)
            cell.setCellValue(contract.address)
        }

        startActivityForResult(Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "application/xls"
            putExtra(Intent.EXTRA_TITLE, "근로내역.xls")
        }, WRITE_XLS_REQ_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == RESULT_OK) {

            when(requestCode) {
                WRITE_XLS_REQ_CODE -> {
                    data?.data?.let { uri ->
                        val pfd = requireContext().contentResolver.openFileDescriptor(uri, "w");
                        val os = FileOutputStream(pfd?.fileDescriptor)
                        workBook.write(os)
                        Toast.makeText(requireContext(), "근로 내역이 저장되었습니다.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun onAttach(context: Context) { //메인 context 자유롭게 사용
        super.onAttach(context)
        mainActivity= context as MainActivity
    }

    companion object {
        @JvmStatic
        fun newInstance() = NaviContractCollection()
    }
}