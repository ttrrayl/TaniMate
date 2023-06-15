package com.example.tanimate.data.local.berita

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class berita(
    val id: String,
    val photo: Int,
    val judul: String
) : Parcelable
