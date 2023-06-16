package com.example.tanimate

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.content.Intent
import android.widget.Button
import com.example.tanimate.ui.CalculatorModalActivity
import com.example.tanimate.ui.CalculatorPupukActivity


class CalculateFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_calculate, container, false)

        val btCalcPupuk: Button = rootView.findViewById(R.id.bt_CalcPupuk)
        btCalcPupuk.setOnClickListener {
            val intent = Intent(activity, CalculatorPupukActivity::class.java)
            startActivity(intent)
        }

        val btCalcModal: Button = rootView.findViewById(R.id.bt_CalcModal)
        btCalcModal.setOnClickListener {
            val intent = Intent(activity, CalculatorModalActivity::class.java)
            startActivity(intent)
        }

        return rootView
    }

}