package ir.jatlin.topmarket.ui.viewholder

import android.view.ViewGroup

interface ViewHolderCreator<T> {
    fun createFrom(parent: ViewGroup, viewType: Int): BaseViewHolder<T>
}