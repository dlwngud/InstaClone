package com.dlwngud.instaclone

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.dlwngud.instaclone.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.*

private lateinit var binding: ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    // Firebase Authentication 관리 클래스
    var auth: FirebaseAuth? = null

    // GoogleLogin 관리 클래스
    var googleSignInClient: GoogleSignInClient? = null
    var GOOGLE_LOGIN_CODE = 9001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // Firebase 로그인 통합 관리하는 Object 만들기
        auth = FirebaseAuth.getInstance()

        binding.btnLogin.setOnClickListener {
            signinAndSignup()
        }

        binding.btnGoogleLogin.setOnClickListener {
            // first step
            googleLogin()
        }

        //구글 로그인 옵션
        var gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("285938124602-9b8sn9p0kpm46btesjdgnbucupd3mkq3.apps.googleusercontent.com")
            .requestEmail()
            .build()
        //구글 로그인 클래스를 만듬
        googleSignInClient = GoogleSignIn.getClient(this, gso)

    }

    fun googleLogin() {
        val signInIntent = googleSignInClient?.signInIntent
        startActivityForResult(signInIntent, GOOGLE_LOGIN_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GOOGLE_LOGIN_CODE) {
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data!!)
            if (result!!.isSuccess) {
                val account = result.signInAccount
                // second step
                firebaseAuthWithGoogle(account!!)
            }

        }
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth?.signInWithCredential(credential)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    moveToMainPage(task.result?.user)
                } else {
                    // 에러 표시
                    Toast.makeText(this, task.exception?.message, Toast.LENGTH_SHORT).show()
                }
            }
    }

    fun signinAndSignup() {
        auth?.createUserWithEmailAndPassword(binding.etEmail.text.toString().trim(),
            binding.etPassword.toString())
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // 계정 생성
                    moveToMainPage(task.result?.user)
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
                    moveToMainPage(task.result?.user)
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