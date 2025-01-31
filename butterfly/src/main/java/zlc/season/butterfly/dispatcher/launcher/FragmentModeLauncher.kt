package zlc.season.butterfly.dispatcher.launcher

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import zlc.season.butterfly.AgileRequest
import zlc.season.butterfly.backstack.BackStackEntryManager

interface FragmentModeLauncher {
    fun FragmentActivity.launch(backStackEntryManager: BackStackEntryManager, request: AgileRequest): Fragment
}