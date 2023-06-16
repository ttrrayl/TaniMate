package com.example.tanimate

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tanimate.data.local.berita.berita
import com.example.tanimate.databinding.FragmentHomeBinding
import com.example.tanimate.databinding.FragmentNotesBinding


class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val list = ArrayList<berita>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment


        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        list.addAll(getListBerita())
        showRecyclerView()
        return root
    }

    private fun showRecyclerView() {
        binding.rvBeritaHome.layoutManager = LinearLayoutManager(context)
        val listBeritaAdapter = BeritaAdapter(list)
        binding.rvBeritaHome.adapter = listBeritaAdapter

        listBeritaAdapter.setOnItemClickCallback(object : BeritaAdapter.OnItemClickCallback{
            override fun onItemClicked(data: berita){
                showSelectedBerita(data)
            }
        })
    }

    private fun showSelectedBerita(Berita: berita) {
        var url = ""
        when(Berita.id){
            "no1" -> {
                url = "https://www.detik.com/edu/detikpedia/d-6657990/jamur-tiram-klasifikasi-habitat-cara-budi-daya-dan-manfaat"
            }
            "no2" -> {
                url = "https://www.liputan6.com/tekno/read/5291437/teknologi-decomposer-dinilai-bisa-capai-zero-waste-dan-kurangi-pembakaran-di-sektor-pertanian"
            }
            "no3" -> {
                url = "https://mediaindonesia.com/nusantara/585193/masyarakat-grogol-yogyakarta-rintis-agrowisata-tanaman-semangka"
            }
            "no4" -> {
                url = "https://mediaindonesia.com/ekonomi/584894/pertanian-cerdas-iklim-mampu-hemat-air-di-lahan-persawahan-hingga-21"
            }
            "no5" -> {
                url = "https://www.medcom.id/ekonomi/bisnis/5b2rrPeb-ini-cara-pupuk-indonesia-dukung-digitalisasi-sektor-pertanian"
            }
            "no6" -> {
                url = "https://www.medcom.id/ekonomi/makro/Obz00o0K-ini-komoditas-ekspor-indonesia-dari-sektor-pertanian-cekidot"
            }
            "no7" -> {
                url = "https://www.sindonews.com/topic/969/pertanian"
            }
            "no8" -> {
                url = "https://ekbis.sindonews.com/read/1115043/34/amerika-siapkan-rp123-miliar-untuk-latih-6500-petani-kakao-dan-kopi-kecil-di-indonesia-1685632017"
            }
        }
        goToBerita(url)
    }

    private fun goToBerita(url: String) {
        val openURL = Intent(Intent.ACTION_VIEW)
        openURL.data = Uri.parse(url)
        startActivity(openURL)
    }

    private fun getListBerita(): ArrayList<berita> {
        val dataPhoto = resources.obtainTypedArray(R.array.photo)
        val dataJudul = resources.getStringArray(R.array.judul_berita)
        val dataId = resources.getStringArray(R.array.id)
        val listBerita = ArrayList<berita>()
        for (i in dataJudul.indices){
            val berita = berita(dataId[i],dataPhoto.getResourceId(i,-1),dataJudul[i])
            listBerita.add(berita)
        }
        return listBerita
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}