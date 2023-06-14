package com.example.tanimate.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.example.tanimate.R


class CalculatorModalActivity : AppCompatActivity() {
    private lateinit var jenisPeralatanTanamanSpinner: Spinner
    private lateinit var modalEditText: EditText
    private lateinit var buttonPredict: Button
    private lateinit var textViewOutput: TextView

    private val jenisPeralatanTanamanOptions = arrayOf(
        "Arit gagang besi",
        "Arit semprot tanaman besar elektrik",
        "Alat semprot tanaman besar",
        "Alat semprot tanaman kecil",
        "Pacul gagang besi",
        "Pacul gagang kayu",
        "Traktor dengan mesin penggerak",
        "Traktor tanpa mesin penggerak"
    )

    private val jenisPeralatanTanaman = mapOf(
        "Arit gagang besi" to 55000,
        "Arit semprot tanaman besar elektrik" to 585000,
        "Alat semprot tanaman besar" to 299000,
        "Alat semprot tanaman kecil" to 37500,
        "Pacul gagang besi" to 60000,
        "Pacul gagang kayu" to 35000,
        "Traktor dengan mesin penggerak" to 12475000,
        "Traktor tanpa mesin penggerak" to 9150000
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculator_modal)

        jenisPeralatanTanamanSpinner = findViewById(R.id.spinner_jenis_peralatan_tanaman)
        modalEditText = findViewById(R.id.biaya_pupuk)
        buttonPredict = findViewById(R.id.bt_calcModal)
        textViewOutput = findViewById(R.id.tvOutput_modal)

        val jenisTanamanAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, jenisPeralatanTanamanOptions)
        jenisTanamanAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        jenisPeralatanTanamanSpinner.adapter = jenisTanamanAdapter

        buttonPredict.setOnClickListener {
            val jenisTanaman = jenisPeralatanTanamanSpinner.selectedItem.toString()
            val pupuk = modalEditText.text.toString().toFloat()

            val modal = jenisPeralatanTanaman[jenisTanaman] ?: 0
            val totalModal = (pupuk*15000) + modal

            textViewOutput.text = "Jumlah Modal Yang Anda Perlukan Adalah : Rp " + totalModal.toString()

        }
    }
}
