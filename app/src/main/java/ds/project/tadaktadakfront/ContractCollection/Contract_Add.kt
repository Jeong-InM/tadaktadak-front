package ds.project.tadaktadakfront.ContractCollection

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import ds.project.tadaktadakfront.R
import kotlinx.android.synthetic.main.activity_contract_add.*

class Contract_Add : AppCompatActivity() {
    private lateinit var contractViewModel: ContractViewModel
    private var id: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contract_add)

        contractViewModel = ViewModelProviders.of(this).get(ContractViewModel::class.java)

        // intent null check & get extras
        if (intent != null && intent.hasExtra(EXTRA_CONTRACT_NAME) && intent.hasExtra(
                EXTRA_CONTRACT_NUMBER
            )
            && intent.hasExtra(EXTRA_CONTRACT_ID)) {
            add_edittext_name.setText(intent.getStringExtra(EXTRA_CONTRACT_NAME))
            add_edittext_number.setText(intent.getStringExtra(EXTRA_CONTRACT_NUMBER))
            id = intent.getLongExtra(EXTRA_CONTRACT_ID, -1)
        }

        add_button.setOnClickListener {
            val name = add_edittext_name.text.toString().trim()
            val number = add_edittext_number.text.toString()

            if (name.isEmpty() || number.isEmpty()) {
                Toast.makeText(this, "Please enter name and number.", Toast.LENGTH_SHORT).show()
            } else {
                val initial = name[0].toUpperCase()
                val contract = Contract(id, name, number, initial)
                contractViewModel.insert(contract)
                finish()
            }
        }
    }

    companion object {
        const val EXTRA_CONTRACT_NAME = "EXTRA_CONTRACT_NAME"
        const val EXTRA_CONTRACT_NUMBER = "EXTRA_CONTRACT_NUMBER"
        const val EXTRA_CONTRACT_ID = "EXTRA_CONTRACT_ID"
    }
}