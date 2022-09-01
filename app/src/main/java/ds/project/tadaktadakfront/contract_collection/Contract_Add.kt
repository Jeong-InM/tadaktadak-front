package ds.project.tadaktadakfront.contract_collection

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import ds.project.tadaktadakfront.R

class Contract_Add : AppCompatActivity() {
    private lateinit var nameEditText: EditText
    private lateinit var numberEditText: EditText
    private lateinit var saveButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contract_add)

        nameEditText = findViewById(R.id.add_edittext_name)
        numberEditText = findViewById(R.id.new_number_edittext)

        saveButton = findViewById(R.id.add_button)
        saveButton.setOnClickListener {
            val replyIntent = Intent()
            if (TextUtils.isEmpty(numberEditText.text)) {
                setResult(Activity.RESULT_CANCELED, replyIntent)
            } else {
                val name = nameEditText.text.toString()
                val number = numberEditText.text.toString()

                replyIntent.putExtra("name", name)
                replyIntent.putExtra("number", number)
                setResult(Activity.RESULT_OK, replyIntent)
            }
            finish()
        }
    }

}