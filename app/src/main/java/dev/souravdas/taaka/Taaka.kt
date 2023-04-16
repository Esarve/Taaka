package dev.souravdas.taaka

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen

/**
 * Created by Sourav
 * On 4/15/2023 8:05 PM
 * For Taaka
 */
class Taaka: Application() {

    companion object {
        lateinit var context: Taaka
            private set
    }

    override fun onCreate() {
        super.onCreate()
        context = this
        AndroidThreeTen.init(this)
    }
}