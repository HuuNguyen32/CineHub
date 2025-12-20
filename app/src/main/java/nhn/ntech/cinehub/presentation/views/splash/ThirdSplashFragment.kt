package nhn.ntech.cinehub.presentation.views.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import nhn.ntech.cinehub.R
import nhn.ntech.cinehub.databinding.FragmentThirdSplashBinding
import nhn.ntech.cinehub.utils.GradientBackgroundUtil

class ThirdSplashFragment : Fragment() {

    private lateinit var binding: FragmentThirdSplashBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentThirdSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        GradientBackgroundUtil.gradientBackground(gradientView = binding.backgroundOverlay3)
    }
}