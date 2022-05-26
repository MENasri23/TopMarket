package ir.jatlin.topmarket.ui.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup

interface ViewHolderCreator<T> {

    fun createFrom(parent: ViewGroup, viewType: Int): BaseViewHolder<T>

    fun layoutInflater(parent: ViewGroup): LayoutInflater =
        LayoutInflater.from(parent.context)
}