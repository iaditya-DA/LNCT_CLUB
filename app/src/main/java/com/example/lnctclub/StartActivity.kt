package com.example.lnctclub

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class StartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_start)

        val clubLoginBtn = findViewById<LinearLayout>(R.id.clubLoginBtn)
        val studentLoginBtn = findViewById<LinearLayout>(R.id.studentLoginBtn)

        clubLoginBtn.setOnClickListener {
            startActivity(Intent(this, ClubLoginActivity::class.java))
        }

        studentLoginBtn.setOnClickListener {
            startActivity(Intent(this, StudentLoginActivity::class.java))
        }
    }
}
