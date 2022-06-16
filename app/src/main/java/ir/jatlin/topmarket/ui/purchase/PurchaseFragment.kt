package ir.jatlin.topmarket.ui.purchase

import android.os.Bundle
import android.view.View
import androidx.core.graphics.Insets
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.doOnLayout
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import ir.jatlin.topmarket.R
import ir.jatlin.topmarket.databinding.FragmentPurchaseBinding
import ir.jatlin.topmarket.ui.util.doOnApplyWindowInsets
import ir.jatlin.topmarket.ui.util.topInset
import ir.jatlin.topmarket.ui.util.viewBinding

@AndroidEntryPoint
class PurchaseFragment : Fragment(R.layout.fragment_purchase) {

    private val binding by viewBinding(FragmentPurchaseBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
    }

    private fun initViews() {

        binding.root.doOnApplyWindowInsets { v, insets, padding, _ ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.updatePadding(
                top = insets.topInset() + padding.top
            )
            WindowInsetsCompat.Builder().setInsets(
                WindowInsetsCompat.Type.systemBars(),
                Insets.of(
                    systemBars.left,
                    0,
                    systemBars.right,
                    systemBars.bottom
                )
            ).build()
        }


        val pagerAdapter = PurchasePageAdapter(childFragmentManager, lifecycle)

        val tabLayout = binding.purchaseTabLayout
        val viewPager = binding.purchaseViewPager.apply {
            adapter = pagerAdapter
            doOnLayout { currentItem = pagerAdapter.itemCount - 1 }
        }

        val tabTitles = resources.getStringArray(R.array.purchase_tabs_titles)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabTitles[position]

        }.attach()


    }
}