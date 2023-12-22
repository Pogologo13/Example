package com.app.skillcinema.utils

import android.os.Bundle

interface BottomBarHandle {

    fun showBottomBar()

    fun hideBottomBar()

    fun uncheckAllItemsInBottomMenu()

    fun getArguments(args: Bundle)
}