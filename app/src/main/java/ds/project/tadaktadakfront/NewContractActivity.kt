package ds.project.tadaktadakfront

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import ds.project.tadaktadakfront.contract.model.entity.Contract
import ds.project.tadaktadakfront.contract.view.callback.ContractApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NewContractActivity:AppCompatActivity() {

    private lateinit var nameEditText: EditText
    private lateinit var numberEditText: EditText
    private lateinit var addressEditText: EditText
    private lateinit var saveButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_contract)

        nameEditText = findViewById(R.id.add_edittext_name)
        numberEditText = findViewById(R.id.add_edittext_number)
        addressEditText = findViewById(R.id.add_edittext_address)

        saveButton = findViewById(R.id.btn_save)

        saveButton.setOnClickListener {

            CoroutineScope(Dispatchers.IO).launch {
                val contact = Contract().apply {
                    name = nameEditText.text.toString()
                    number = numberEditText.text.toString()
                    address = addressEditText.text.toString()
                }
                ContractApplication.db?.contractDao()?.insert(contact)
                finish()
            }
        }
    }
}
