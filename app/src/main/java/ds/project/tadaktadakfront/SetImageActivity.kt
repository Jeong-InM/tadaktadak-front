package ds.project.tadaktadakfront

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.korean.KoreanTextRecognizerOptions
import java.io.File


class SetImageActivity : AppCompatActivity() {


    private lateinit var textRecognizer: TextRecognizer
    lateinit var textView: TextView

    private lateinit var nameEditText: EditText
    private lateinit var numberEditText: EditText
    private lateinit var addressEditText: EditText
    private lateinit var saveButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_image)

        textRecognizer = TextRecognition.getClient(KoreanTextRecognizerOptions.Builder().build())

        val currentPhotoPath: String? = intent.getStringExtra("path")
        val uriSelected = Uri.parse(intent.getStringExtra("path"))
        val uri: Uri = File(currentPhotoPath).toUri()
        Log.v("tag", "successI")

        val imageView: ImageView = findViewById(R.id.set_iv)
        Glide.with(this@SetImageActivity)
            .load(currentPhotoPath)
            .error(ColorDrawable(Color.RED))
            .placeholder(R.drawable.ic_launcher_foreground)
            .into(imageView)


        nameEditText=findViewById(R.id.add_edittext_name)
        numberEditText=findViewById(R.id.add_edittext_number)
        addressEditText=findViewById(R.id.add_edittext_address)
        saveButton=findViewById(R.id.button_save)

        saveButton.setOnClickListener{

            if (!TextUtils.isEmpty(numberEditText.text)) {
                val name = nameEditText.text.toString()
                val number = numberEditText.text.toString()
                val address = addressEditText.text.toString()

                startActivity(Intent(this, MainActivity::class.java).apply {
                    putExtra("name", name)
                    putExtra("number", number)
                    putExtra("address", address)
                })
            }
        }
    }

}

