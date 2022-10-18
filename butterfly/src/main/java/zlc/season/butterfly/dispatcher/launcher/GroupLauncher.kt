package zlc.season.butterfly.dispatcher.launcher

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import zlc.season.butterfly.AgileRequest
import zlc.season.butterfly.ButterflyHelper.createFragment
import zlc.season.butterfly.ButterflyHelper.findFragment
import zlc.season.butterfly.ButterflyHelper.hide
import zlc.season.butterfly.ButterflyHelper.show
import zlc.season.butterfly.containerId
import zlc.season.butterfly.group.FragmentGroupEntity
import zlc.season.butterfly.group.FragmentGroupManager

class GroupLauncher : FragmentGroupLauncher {
    override fun FragmentActivity.launch(fragmentGroupManager: FragmentGroupManager, request: AgileRequest): Fragment {
        val list = fragmentGroupManager.getGroupList(this, request)
        list.forEach { entity ->
            findFragment(entity.request)?.also { hide(it) }
        }

        val target = list.find { it.request.className == request.className }?.run { findFragment(request) }

        return if (target == null) {
            fragmentGroupManager.addEntity(this, FragmentGroupEntity(request))
            show(request)
        } else {
            if (target is OnFragmentNewArgument) {
                target.onNewArgument(request.bundle)
            }
            show(target)
            target
        }
    }

    private fun FragmentActivity.show(request: AgileRequest): Fragment {
        val fragment = createFragment(request)
        with(supportFragmentManager.beginTransaction()) {
            request.fragmentConfig.apply {
                setCustomAnimations(enterAnim, exitAnim, 0, 0)
                if (useReplace) {
                    replace(request.containerId(), fragment, request.uniqueId)
                } else {
                    add(request.containerId(), fragment, request.uniqueId)
                }
            }
            commitAllowingStateLoss()
        }
        return fragment
    }
}