package com.otuscoursework.arch.recycler

import com.hannesdorfmann.adapterdelegates4.AdapterDelegate
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter

class BaseDelegationAdapter(
    vararg adapterDelegates: AdapterDelegate<List<RecyclerViewItem>>
) : AsyncListDifferDelegationAdapter<RecyclerViewItem>(DiffUtilCallback) {

    init {
        adapterDelegates.forEach {
            delegatesManager.addDelegate(it)
        }
    }
}