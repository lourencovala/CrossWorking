package ps.crossworking.helper

import android.app.Application
import android.content.res.Resources
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class CrossWorkingApplication : Application() {
    companion object {
        lateinit var instance: Application
        lateinit var resourses: Resources
    }


    // MARK: - Lifecycle

    override fun onCreate() {
        super.onCreate()
        instance = this
        resourses = resources
    }
}
