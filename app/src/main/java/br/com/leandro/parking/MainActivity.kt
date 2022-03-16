package br.com.leandro.parking

import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.GravityCompat
import androidx.viewpager.widget.ViewPager
import br.com.leandro.parking.databinding.ActivityMainBinding
import br.com.leandro.parking.ui.main.SectionsPagerAdapter
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setViewPagerAdapter()

        binding.let {
            it.navigationView.setNavigationItemSelectedListener(this)

            it.menu.setOnClickListener {
                if (binding.drawerLayout.isDrawerOpen(GravityCompat.END))
                    binding.drawerLayout.closeDrawer(GravityCompat.END)
                binding.drawerLayout.openDrawer(GravityCompat.END)

                val closeMenu = findViewById<ImageView>(R.id.closeMenu)
                closeMenu.setOnClickListener { binding.drawerLayout.closeDrawer(GravityCompat.END) }
            }
        }
    }

    private fun setViewPagerAdapter() {
        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        val viewPager: ViewPager = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabLayout
        tabs.setupWithViewPager(viewPager)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.entrance -> {
                with(binding) {
                    tabLayout.selectTab(tabLayout.getTabAt(0))
                    drawerLayout.closeDrawer(GravityCompat.END)
                }
            }
            R.id.exit -> {
                with(binding) {
                    tabLayout.selectTab(tabLayout.getTabAt(1))
                    drawerLayout.closeDrawer(GravityCompat.END)
                }
            }
        }
        return false
    }
}