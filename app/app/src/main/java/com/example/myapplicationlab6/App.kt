package com.example.myapplicationlab6
import android.app.Application
import com.yandex.mapkit.MapKitFactory

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        // Укажи API-ключ
        MapKitFactory.setApiKey("fda37a4d-545e-418b-bfa9-0b7ebfee1d35")
    }
}