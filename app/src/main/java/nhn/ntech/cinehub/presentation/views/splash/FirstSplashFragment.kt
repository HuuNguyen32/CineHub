package nhn.ntech.cinehub.presentation.views.splash

import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.graphics.drawable.PaintDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RectShape
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import nhn.ntech.cinehub.R
import nhn.ntech.cinehub.databinding.FragmentFirstSplashBinding
import androidx.core.graphics.toColorInt
import dagger.hilt.android.AndroidEntryPoint
import nhn.ntech.cinehub.utils.GradientBackgroundUtil
import nhn.ntech.cinehub.utils.OnNavigationListener

@AndroidEntryPoint
class FirstSplashFragment : Fragment() {

    private lateinit var binding: FragmentFirstSplashBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentFirstSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        GradientBackgroundUtil.gradientBackground(gradientView = binding.backgroundOverlay)

        binding.btnSplash1.setOnClickListener {
            (activity as? OnNavigationListener)?.onContinueClick()
        }
    }

}