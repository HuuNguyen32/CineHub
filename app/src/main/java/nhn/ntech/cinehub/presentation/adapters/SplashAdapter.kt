package nhn.ntech.cinehub.presentation.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import nhn.ntech.cinehub.presentation.views.splash.FirstSplashFragment
import nhn.ntech.cinehub.presentation.views.splash.SecondSplashFragment
import nhn.ntech.cinehub.presentation.views.splash.ThirdSplashFragment

class SplashAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> FirstSplashFragment()
            1 -> SecondSplashFragment()
            else -> ThirdSplashFragment()
        }
    }

    override fun getItemCount(): Int {
        return 3
    }
}