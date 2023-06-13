package com.example.tanimate.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.example.tanimate.R
import com.example.tanimate.onboarding.screen.FirstScreenFragment
import com.example.tanimate.onboarding.screen.SecondScreenFragment

class ViewPageFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_view_page, container, false)
        val viewPager = view.findViewById<ViewPager2>(R.id.viewPagerScreen)

        val fragmentList = arrayListOf<Fragment>(
            FirstScreenFragment(),
            SecondScreenFragment()
        )

        val adapter = ViewPagerAdapter(
            fragmentList,
            childFragmentManager,
            lifecycle
        )

        viewPager.adapter = adapter

        return view
    }



}