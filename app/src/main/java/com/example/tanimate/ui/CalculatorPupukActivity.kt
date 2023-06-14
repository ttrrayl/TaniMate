package com.example.tanimate.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.example.tanimate.R
import org.tensorflow.lite.DataType
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.FileInputStream
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

class CalculatorPupukActivity : AppCompatActivity() {
    private lateinit var jenisTanamanSpinner: Spinner
    private lateinit var jenisTanahSpinner: Spinner
    private lateinit var lahanEditText: EditText
    private lateinit var buttonPredict: Button
    private lateinit var textViewOutput: TextView

    private val jenisTanamanOptions = arrayOf("padi", "jagung", "kacang hijau", "semangka", "melon", "apel", "jeruk", "kopi") // Ganti dengan pilihan yang sesuai
    private val jenisTanahOptions = arrayOf("lempung", "lempung berpasir", "pasir berlempung", "liat berpasir") // Ganti dengan pilihan yang sesuai

    private lateinit var interpreter: Interpreter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculator_pupuk)

        jenisTanamanSpinner = findViewById(R.id.spinner_jenis_tanaman)
        jenisTanahSpinner = findViewById(R.id.spinner_jenis_tanah)
        lahanEditText = findViewById(R.id.textIn_Lahan)
        buttonPredict = findViewById(R.id.bt_calcPupuk)
        textViewOutput = findViewById(R.id.tvOutput_pupuk)

        // Mengisi spinner dengan opsi jenis tanaman
        val jenisTanamanAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, jenisTanamanOptions)
        jenisTanamanAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        jenisTanamanSpinner.adapter = jenisTanamanAdapter

        // Mengisi spinner dengan opsi jenis tanah
        val jenisTanahAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, jenisTanahOptions)
        jenisTanahAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        jenisTanahSpinner.adapter = jenisTanahAdapter

        buttonPredict.setOnClickListener {
            val jenisTanaman = jenisTanamanSpinner.selectedItem.toString()
            val jenisTanah = jenisTanahSpinner.selectedItem.toString()
            val lahan = lahanEditText.text.toString().toFloat()

            val output = runInference(jenisTanaman, jenisTanah, lahan)

            // Tampilkan output di TextView atau elemen tampilan lainnya
            textViewOutput.text = output.toString()
        }

        // Inisialisasi Interpreter dan muat model dari file .tflite
        interpreter = Interpreter(loadModelFile(this))
    }

    private fun loadModelFile(context: Context): MappedByteBuffer {
        val fileDescriptor = context.assets.openFd("mlmodel_2.tflite")
        val fileInputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = fileInputStream.channel
        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }


    private fun runInference(jenisTanaman: String, jenisTanah: String, lahan: Float): Float {
        // Ubah jenisTanaman, jenisTanah, dan lahan menjadi nilai numerik yang sesuai dengan model multivariate linear regression

        // Menggunakan metode mapping manual
        val jenisTanamanValue = jenisTanamanOptions.indexOf(jenisTanaman).toFloat()
        val jenisTanahValue = jenisTanahOptions.indexOf(jenisTanah).toFloat()

        // Buat input tensor
        val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 3), DataType.FLOAT32)
        inputFeature0.loadArray(floatArrayOf(jenisTanamanValue, jenisTanahValue, lahan))

        // Buat output tensor
        val outputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 1), DataType.FLOAT32)

        // Run inference pada model TensorFlow Lite
        val inputs = arrayOf(inputFeature0.buffer)
        val outputs = mutableMapOf<Int, Any>()
        outputs[0] = outputFeature0.buffer
        interpreter.runForMultipleInputsOutputs(inputs, outputs)

        // Ambil nilai output
        return outputFeature0.getFloatValue(0)
    }


    override fun onDestroy() {
        super.onDestroy()
        // Bebaskan sumber daya setelah selesai menggunakan model
        interpreter.close()
    }
}
