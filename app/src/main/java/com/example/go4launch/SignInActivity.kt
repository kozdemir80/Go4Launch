package com.example.go4launch
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.go4launch.databinding.SignInActivityBinding


 private lateinit var binding: SignInActivityBinding
class SignInActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sign_in_activity)
        binding = SignInActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}