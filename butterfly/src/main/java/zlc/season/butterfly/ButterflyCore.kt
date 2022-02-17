package zlc.season.butterfly

import zlc.season.butterfly.annotation.Module

object ButterflyCore {
    private val moduleController by lazy { ModuleController() }

    fun init(vararg module: Module) {
        moduleController.addModule(*module)
    }

    fun addModule(module: Module) {
        moduleController.addModule(module)
    }

    fun removeModule(module: Module) {
        moduleController.removeModule(module)
    }

    fun queryAgile(scheme: String): String = moduleController.queryAgile(scheme)

    fun queryEvade(identity: String): EvadeRequest = moduleController.queryEvade(identity)
}