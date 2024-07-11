package com.example.rickmorty.views

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.rickmorty.databinding.ActivityLoginPageBinding

class LoginPageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginButton.setOnClickListener {
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()

            if (validateCredentials(email, password)) {
                saveLoginState()
                navigateToCharacterListing()
            } else {
                Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show()
            }
        }

        // Check if the user is already logged in
        if (isLoggedIn()) {
            navigateToCharacterListing()
        }
    }

    private fun validateCredentials(email: String, password: String): Boolean {
        return email == "DemoPgm@gmail.com" && password == "12345@123"
    }

    private fun saveLoginState() {
        val sharedPreferences: SharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putBoolean("isLoggedIn", true)
        editor.apply()
    }

    private fun isLoggedIn(): Boolean {
        val sharedPreferences: SharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE)
        return sharedPreferences.getBoolean("isLoggedIn", false)
    }

    private fun navigateToCharacterListing() {
        val intent = Intent(this, HomePageActivity::class.java)
        startActivity(intent)
        finish()
    }
}
