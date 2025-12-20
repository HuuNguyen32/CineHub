package nhn.ntech.cinehub.presentation.views.splash

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import nhn.ntech.cinehub.R
import nhn.ntech.cinehub.databinding.ActivitySpashBinding
import nhn.ntech.cinehub.presentation.adapters.SplashAdapter

class SpashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySpashBinding
    private lateinit var adapter: SplashAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySpashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setPadding()

        adapter = SplashAdapter(this)
        binding.viewPagerSplash.adapter = adapter


    }

    private fun setPadding() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, 0)
            insets
        }
    }
}