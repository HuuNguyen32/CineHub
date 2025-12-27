package nhn.ntech.cinehub.presentation.views.splash

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.hilt.android.AndroidEntryPoint
import nhn.ntech.cinehub.R
import nhn.ntech.cinehub.databinding.FragmentSecondSplashBinding
import nhn.ntech.cinehub.utils.GradientBackgroundUtil
import nhn.ntech.cinehub.utils.OnNavigationListener

@AndroidEntryPoint
class SecondSplashFragment : Fragment() {

    private lateinit var binding: FragmentSecondSplashBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSecondSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        GradientBackgroundUtil.gradientBackground(gradientView = binding.backgroundOverlay2)

        binding.btnSplash2.setOnClickListener {
            (activity as? OnNavigationListener)?.onContinueClick()
        }
    }

}