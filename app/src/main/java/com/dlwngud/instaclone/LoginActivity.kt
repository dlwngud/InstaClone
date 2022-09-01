package com.dlwngud.instaclone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.dlwngud.instaclone.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

private lateinit var binding: ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    var auth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        auth = FirebaseAuth.getInstance()

        binding.btnLogin.setOnClickListener {
            signinAndSignup()
        }
    }

    fun signinAndSignup() {
        auth?.createUserWithEmailAndPassword(binding.etEmail.text.toString().trim(),
            binding.etPassword.toString())
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // 계정 생성
                    moveToMainPage(task.result.user)
                } else if (task.exception?.message.isNullOrEmpty()) {
                    // 에러 표시
                    Toast.makeText(this, task.exception?.message, Toast.LENGTH_SHORT).show()
                } else {
                    // Login
                    signinEmail()
                }
            }
    }

    fun signinEmail() {
        auth?.signInWithEmailAndPassword(binding.etEmail.text.toString().trim(),
            binding.etPassword.toString())
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {

                } else {
                    // 에러 표시
                    Toast.makeText(this, task.exception?.message, Toast.LENGTH_SHORT).show()
                }
            }
    }

    fun moveToMainPage(user: FirebaseUser?) {
        if (user != null) {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}