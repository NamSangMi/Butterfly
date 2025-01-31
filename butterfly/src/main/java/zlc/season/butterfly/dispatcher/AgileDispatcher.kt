package zlc.season.butterfly.dispatcher

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import zlc.season.butterfly.Action
import zlc.season.butterfly.AgileRequest
import zlc.season.butterfly.ButterflyHelper
import zlc.season.butterfly.backstack.BackStackEntryManager
import zlc.season.butterfly.group.GroupEntryManager
import zlc.season.butterfly.logw

class AgileDispatcher {
    companion object {
        private const val COMPOSABLE_CLASS = "zlc.season.butterfly.compose.AgileComposable"
        private const val COMPOSE_DISPATCHER_CLASS = "zlc.season.butterfly.compose.ComposeDispatcher"
    }

    private val dispatcherMaps = LinkedHashMap<Class<*>, InnerDispatcher>()

    private val backStackEntryManager = BackStackEntryManager()
    private val groupEntryManager = GroupEntryManager()

    init {
        dispatcherMaps.apply {
            putAll(
                listOf(
                    Action::class.java to ActionDispatcher,
                    Activity::class.java to ActivityDispatcher(backStackEntryManager),
                    DialogFragment::class.java to DialogFragmentDispatcher(backStackEntryManager),
                    Fragment::class.java to FragmentDispatcher(backStackEntryManager, groupEntryManager)
                )
            )
            try {
                val agileComposableCls = Class.forName(COMPOSABLE_CLASS)
                val composeDispatcherCls = Class.forName(COMPOSE_DISPATCHER_CLASS)
                val composableDispatcher = composeDispatcherCls.getConstructor(BackStackEntryManager::class.java, GroupEntryManager::class.java)
                    .newInstance(backStackEntryManager, groupEntryManager) as InnerDispatcher
                put(agileComposableCls, composableDispatcher)
            } catch (e: Exception) {
                e.logw()
            }

            put(Any::class.java, NoneDispatcher)
        }
    }

    suspend fun dispatch(request: AgileRequest): Flow<Result<Bundle>> {
        if (request.className.isEmpty()) {
            "Agile --> class not found!".logw()
            return flowOf(Result.failure(IllegalStateException("Agile class not found!")))
        }

        val fragmentActivity = ButterflyHelper.fragmentActivity
        return if (fragmentActivity == null) {
            findDispatcher(request).dispatch(request)
        } else {
            findDispatcher(request).dispatch(fragmentActivity, request)
        }
    }

    fun retreat(bundle: Bundle): AgileRequest? {
        val fragmentActivity = ButterflyHelper.fragmentActivity
        if (fragmentActivity == null) {
            "Agile --> FragmentActivity not found".logw()
            return null
        }

        val topEntry = backStackEntryManager.removeTopEntry(fragmentActivity) ?: return null
        findDispatcher(topEntry.request).retreat(fragmentActivity, topEntry, bundle)

        dispatcherMaps.values.forEach { it.onRetreat(fragmentActivity, topEntry) }

        return topEntry.request
    }

    private fun findDispatcher(request: AgileRequest): InnerDispatcher {
        val cls = Class.forName(request.className)
        return dispatcherMaps[dispatcherMaps.keys.find { it.isAssignableFrom(cls) }]!!
    }
}