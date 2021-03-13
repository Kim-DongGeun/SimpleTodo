package com.example.simpletodo

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class CalendarStateAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    private val pageCount = Int.MAX_VALUE
    val fragmentPosition = Int.MAX_VALUE / 2

    override fun getItemCount(): Int {
        return Int.MAX_VALUE
    }

    override fun createFragment(position: Int): Fragment {
        val calendarLayoutFragment = CalendarLayoutFragment()
        calendarLayoutFragment.pageIndex = position
        return calendarLayoutFragment
    }
}