package nhn.ntech.cinehub.presentation.views.splash

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import nhn.ntech.cinehub.R
import nhn.ntech.cinehub.databinding.ActivitySpashBinding
import nhn.ntech.cinehub.presentation.adapters.SplashAdapter
import nhn.ntech.cinehub.utils.OnNavigationListener

@AndroidEntryPoint
class SpashActivity : AppCompatActivity(), OnNavigationListener {

    private lateinit var binding: ActivitySpashBinding
    private lateinit var adapter: SplashAdapter
    private lateinit var viewPager2: ViewPager2


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySpashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setPadding()

        // Set adapter for viewpager
        viewPager2 = binding.viewPagerSplash
        adapter = SplashAdapter(this)
        viewPager2.adapter = adapter

        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                updateIndicators(position)
            }

        })

    }

    private fun setPadding() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, 0)
            insets
        }
    }

    private fun updateIndicators(position: Int) {
        val activeColor = ContextCompat.getColor(this, R.color.aqua)
        val inactiveColor = ContextCompat.getColor(this, R.color.gray_70)

        binding.indicator2.setBackgroundColor(if (position == 1) activeColor else inactiveColor)
        binding.indicator3.setBackgroundColor(if (position == 2) activeColor else inactiveColor)

        binding.layoutIndicator.visibility = if (position == 2) View.GONE else View.VISIBLE
    }

    override fun onContinueClick() {
        val currentItem = viewPager2.currentItem
        val totalItems = viewPager2.adapter?.itemCount ?: 0

        if (currentItem < totalItems - 1){
            viewPager2.setCurrentItem(currentItem + 1, true)
        } else{
            navigateToHome()
        }
    }

    private fun navigateToHome() {

    }
}