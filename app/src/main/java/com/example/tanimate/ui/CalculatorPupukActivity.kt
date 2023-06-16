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

        val jenisTanamanAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, jenisTanamanOptions)
        jenisTanamanAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        jenisTanamanSpinner.adapter = jenisTanamanAdapter

        val jenisTanahAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, jenisTanahOptions)
        jenisTanahAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        jenisTanahSpinner.adapter = jenisTanahAdapter

        buttonPredict.setOnClickListener {
            val jenisTanaman = jenisTanamanSpinner.selectedItem.toString()
            val jenisTanah = jenisTanahSpinner.selectedItem.toString()
            val lahan = lahanEditText.text.toString().toFloat()

            val output = runInference(jenisTanaman, jenisTanah, lahan)

            textViewOutput.text = "Besar Pupuk SP36 Yang Anda Butuhkan Adalah (Kilogram) : " + output.toString()

        }

        interpreter = Interpreter(loadModelFile(this))
    }

    private fun loadModelFile(context: Context): MappedByteBuffer {
        val fileDescriptor = context.assets.openFd("mlmodel_3.tflite")
        val fileInputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = fileInputStream.channel
        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }


    private fun runInference(jenisTanaman: String, jenisTanah: String, lahan: Float): Float {

        val jenisTanamanValue = jenisTanamanOptions.indexOf(jenisTanaman).toFloat()
        val jenisTanahValue = jenisTanahOptions.indexOf(jenisTanah).toFloat()

        val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 3), DataType.FLOAT32)
        inputFeature0.loadArray(floatArrayOf(jenisTanamanValue, jenisTanahValue, lahan))

        val outputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 1), DataType.FLOAT32)

        val inputs = arrayOf(inputFeature0.buffer)
        val outputs = mutableMapOf<Int, Any>()
        outputs[0] = outputFeature0.buffer
        interpreter.runForMultipleInputsOutputs(inputs, outputs)

        return outputFeature0.getFloatValue(0)
    }


    override fun onDestroy() {
        super.onDestroy()
        interpreter.close()
    }
}
