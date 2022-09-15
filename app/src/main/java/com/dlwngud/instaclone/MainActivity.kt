package com.dlwngud.instaclone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dlwngud.instaclone.databinding.ActivityLoginBinding
import com.dlwngud.instaclone.databinding.ActivityMainBinding
import com.dlwngud.instaclone.navigation.AlarmFragment
import com.dlwngud.instaclone.navigation.DetailViewFragment
import com.dlwngud.instaclone.navigation.GridFragment
import com.dlwngud.instaclone.navigation.UserFragment

private lateinit var binding: ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.bottomNav.setOnItemSelectedListener { it ->
            when (it.itemId) {
                R.id.action_home -> {
                    val detailViewFragment = DetailViewFragment()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_content, detailViewFragment).commit()
                    true
                }
                R.id.action_search -> {
                    val gridFragment = GridFragment()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_content, gridFragment).commit()
                    true
                }
                R.id.action_add_a_photo -> {
                    true
                }
                R.id.action_favorite_alarm -> {
                    val alarmFragment = AlarmFragment()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_content, alarmFragment).commit()
                    true
                }
                R.id.action_account -> {
                    val userFragment = UserFragment()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_content, userFragment).commit()
                    true
                }
                else -> false
            }
        }
    }
}