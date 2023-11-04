package com.enigma.enigmamediav2.utils

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.widget.Toast
import com.enigma.enigmamediav2.R
import java.text.ParseException
import java.text.SimpleDateFormat

object CommonUtils {

    fun showLoading(view: View, state: Boolean) {
        view.visibility = if (state) View.VISIBLE else View.GONE
    }

    fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    @SuppressLint("SimpleDateFormat")
    fun formatter(date: String): String? {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        val outputFormat = SimpleDateFormat("dd MMMM yyyy")

        return try {
            val dateFormat = inputFormat.parse(date)
            val formattedDate = dateFormat?.let { outputFormat.format(it) }
            formattedDate ?: ""
        } catch (e: ParseException) {
            e.printStackTrace()
            ""
        }
    }

    fun getRandomAvatar(): Int {
        val avatarResourceDrawable = arrayOf(
            R.drawable.avatar1,
            R.drawable.avatar2,
            R.drawable.avatar3,
            R.drawable.avatar4
        )

        val randomIndex = (avatarResourceDrawable.indices).random()
        return avatarResourceDrawable[randomIndex]
    }
}