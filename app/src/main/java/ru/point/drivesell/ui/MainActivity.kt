package ru.point.drivesell.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import ru.point.common.navigation.BottomBarManager
import ru.point.common.navigation.NavigatorProvider
import ru.point.drivesell.R
import ru.point.drivesell.databinding.ActivityMainBinding
import ru.point.drivesell.utils.NavigatorImpl

class MainActivity : AppCompatActivity(), BottomBarManager, NavigatorProvider {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(binding.navHostFragment) { v, insets ->
            val bars = insets.getInsets(
                WindowInsetsCompat.Type.systemBars()
                        or WindowInsetsCompat.Type.displayCutout()
            )
            v.updatePadding(
                left = bars.left,
                top = bars.top,
                right = bars.right,
            )
            WindowInsetsCompat.CONSUMED
        }
    }

    override fun onStart() {
        super.onStart()
        NavigationUI.setupWithNavController(
            binding.bottomNavigation,
            this.findNavController(R.id.nav_host_fragment)
        )
    }

    override fun hide() {
        binding.bottomNavigation.isVisible = false
    }

    override fun show() {
        binding.bottomNavigation.isVisible = true
    }

    override fun getNavigator(navController: NavController) =
        NavigatorImpl(navController)
}