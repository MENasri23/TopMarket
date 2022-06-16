package ir.jatlin.topmarket.ui.purchase

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import ir.jatlin.topmarket.ui.purchase.cart.CartFragment
import ir.jatlin.topmarket.ui.purchase.nextcart.NextCartFragment

class PurchasePageAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return TABS_COUNT
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> NextCartFragment()
            else -> CartFragment()
        }
    }

    companion object {
        const val TABS_COUNT = 2
    }
}