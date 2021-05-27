package com.vivek.rzpcustomfragment

import android.content.Intent
import android.os.Bundle
import android.telecom.Call
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(), CallbackInterface{

    private lateinit var paymentFragment:PaymentFragment
    private lateinit var intermittentFragment: IntermittentFragment
    private lateinit var btn:Button
    private lateinit var frameLayout:FrameLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btn = findViewById<Button>(R.id.btn_open_fragment)
        frameLayout = findViewById<FrameLayout>(R.id.frame_layout)
        intermittentFragment = IntermittentFragment()
        paymentFragment = PaymentFragment()
        btn.setOnClickListener {
//            val ft =
//                supportFragmentManager.beginTransaction()
//            ft.add(R.id.frame_layout, intermittentFragment).addToBackStack("")
//            ft.commit()
//            intermittentFragment.setListener(this)
//            btn.visibility = View.VISIBLE
//            frameLayout.visibility = View.VISIBLE
            intermittentFragment.setListener(this)
            val ft =
                supportFragmentManager.beginTransaction()
            ft.add(R.id.frame_layout, intermittentFragment).addToBackStack("")
            ft.commit()
            btn.visibility = View.GONE
            frameLayout.visibility = View.VISIBLE
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

    override fun onResume() {
        super.onResume()
        Log.d("RESUME","RESUMING_DATA");
    }

    override fun changeInterface() {
        val ft =
            supportFragmentManager.beginTransaction()
        ft.add(R.id.frame_layout, paymentFragment).addToBackStack("")
        ft.commit()
        btn.visibility = View.GONE
        frameLayout.visibility = View.VISIBLE
        paymentFragment.initialize(this)
    }
}