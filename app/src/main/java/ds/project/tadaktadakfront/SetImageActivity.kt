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
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
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

        textView = findViewById(R.id.image_text)

        // 2초 후 다음 액티비티로 넘김
        Handler(Looper.getMainLooper()).postDelayed({
            convertImagetoText(uriSelected)
            convertImagetoText(uri)
            Log.v("tag", "successT")


        }, 2000)

        val button = findViewById<Button>(R.id.button_save)
        button.setOnClickListener {
            val replyIntent = Intent()
            if (TextUtils.isEmpty(textView.toString())) {
                setResult(Activity.RESULT_CANCELED, replyIntent)
            } else {
                val content = textView.text.toString()
                replyIntent.putExtra(EXTRA_REPLY, content)
                setResult(Activity.RESULT_OK, replyIntent)
            }
            finish()
        }



    } // end onCreate

    private fun convertImagetoText(imageUri: Uri?) {
        try {

            var inputImg = InputImage.fromFilePath(applicationContext, imageUri!!)
            Log.v("tag", "successC")
            val result: Task<Text> = textRecognizer.process(inputImg)
                .addOnSuccessListener {
                    Log.v("tag", "success")
                    textView.text = it.text

                }.addOnFailureListener {
                    textView.text = "Error : ${it.message}"

                }
        } catch (e: Exception) {

        }
    }

        companion object {
            const val EXTRA_REPLY = "ds.project.tadaktadakfront.contractlistsql.REPLY"

        }
    }

