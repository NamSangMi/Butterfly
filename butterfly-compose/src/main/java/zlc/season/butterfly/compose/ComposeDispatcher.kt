package zlc.season.butterfly.compose

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import zlc.season.butterfly.AgileRequest
import zlc.season.butterfly.ButterflyHelper.setActivityResult
import zlc.season.butterfly.backstack.BackStackEntry
import zlc.season.butterfly.backstack.BackStackEntryManager
import zlc.season.butterfly.compose.Utils.isActivityEntry
import zlc.season.butterfly.compose.Utils.isComposeEntry
import zlc.season.butterfly.dispatcher.InnerDispatcher
import zlc.season.butterfly.group.GroupEntryManager

class ComposeDispatcher(
    private val backStackEntryManager: BackStackEntryManager,
    private val groupEntryManager: GroupEntryManager
) : InnerDispatcher {
    private val composableLauncher = AgileComposableLauncher(backStackEntryManager, groupEntryManager)

    override fun retreat(activity: FragmentActivity, topEntry: BackStackEntry, bundle: Bundle) {
        with(activity) {
            val newTopEntry = backStackEntryManager.getTopEntry(this)
            if (newTopEntry == null || isActivityEntry(newTopEntry)) {
                if (topEntry.request.isRoot) {
                    setActivityResult(bundle)
                    finish()
                } else {
                    with(composableLauncher) {
                        clear()
                    }
                }
            }
        }
    }

    override fun onRetreat(activity: FragmentActivity, topEntry: BackStackEntry) {
        val newTopEntry = backStackEntryManager.getTopEntry(activity)
        if (newTopEntry != null && isComposeEntry(newTopEntry)) {
            with(composableLauncher) {
                activity.retreat(newTopEntry.request)
            }
        }
    }

    override suspend fun dispatch(activity: FragmentActivity, request: AgileRequest): Flow<Result<Bundle>> {
        with(composableLauncher) {
            activity.launch(request)
        }
        return emptyFlow()
    }
}