package zlc.season.butterflydemo

import android.app.Application
import zlc.season.butterfly.Butterfly
import zlc.season.butterfly.ButterflyCore
//import zlc.season.compose.TestModule

class DemoApplication : Application() {
    override fun onCreate() {
        super.onCreate()
//        ButterflyCore.addModule(TestModule())
    }
}