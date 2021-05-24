package com.vivek.rzpcustomfragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var paymentFragment:PaymentFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val btn = findViewById<Button>(R.id.btn_open_fragment)
        val frameLayout = findViewById<FrameLayout>(R.id.frame_layout)
        btn.setOnClickListener {
            paymentFragment = PaymentFragment()
            val ft =
                supportFragmentManager.beginTransaction()
            ft.add(R.id.frame_layout, paymentFragment)
            ft.commit()
            btn.visibility = View.GONE
            frameLayout.visibility = View.VISIBLE
            paymentFragment.initialize(this)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        paymentFragment.onActivityResultFrag(requestCode,resultCode,data);
    }

    override fun onBackPressed() {
        super.onBackPressed()
        paymentFragment.onBackPressed()
    }
}