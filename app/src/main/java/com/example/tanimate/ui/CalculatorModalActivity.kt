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

class CalculatorModalActivity : AppCompatActivity() {
    private lateinit var jenisBibitTanamanSpinner: Spinner
    private lateinit var modalEditText: EditText
    private lateinit var buttonPredict: Button
    private lateinit var textViewOutput: TextView

    private val jenisBibitTanamanOptions = arrayOf("Arit gagang besi", "Alat semprot tanaman besar", "Alat semprot tanaman kecil", "Pacul gagang besi", "Pacul gagang kayu", "Traktor dengan mesin penggerak", "Traktor tanpa mesin penggerak") // Ganti dengan pilihan yang sesuai

    private lateinit var interpreter: Interpreter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculator_modal)

        jenisBibitTanamanSpinner = findViewById(R.id.spinner_jenis_bibit_tanaman)
        modalEditText = findViewById(R.id.biaya_pupuk)
        buttonPredict = findViewById(R.id.bt_calcModal)
        textViewOutput = findViewById(R.id.tvOutput_modal)

        // Mengisi spinner dengan opsi jenis tanaman
        val jenisTanamanAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, jenisBibitTanamanOptions)
        jenisTanamanAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        jenisBibitTanamanSpinner.adapter = jenisTanamanAdapter


        buttonPredict.setOnClickListener {
            val jenisTanaman = jenisBibitTanamanSpinner.selectedItem.toString()
            val lahan = modalEditText.text.toString().toFloat()

//            val output = runInference(jenisTanaman, lahan)
//
//            // Tampilkan output di TextView atau elemen tampilan lainnya
//            textViewOutput.text = output.toString()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        // Bebaskan sumber daya setelah selesai menggunakan model
        interpreter.close()
    }
}