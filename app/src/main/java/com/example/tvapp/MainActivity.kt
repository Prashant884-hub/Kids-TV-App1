package com.example.tvapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.GridView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var appGrid: GridView
    private lateinit var adapter: AppAdapter
    private lateinit var approvedApps: List<AppInfo>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val pinIntent = Intent(this, PINProtectionActivity::class.java)
        startActivity(pinIntent)

        setContentView(R.layout.activity_main)

        appGrid = findViewById(R.id.appGrid)
        approvedApps = getApprovedApps()

        if (approvedApps.isNotEmpty()) {
            adapter = AppAdapter(this, approvedApps)
            appGrid.adapter = adapter
        } else {
            Toast.makeText(this, "No Approved Apps Found!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getApprovedApps(): List<AppInfo> {
        val appList: MutableList<AppInfo> = ArrayList()
        val intent = Intent(Intent.ACTION_MAIN, null).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
        }
        val resolvedApps = packageManager.queryIntentActivities(intent, 0)

        for (info in resolvedApps) {
            val packageName = info.activityInfo.packageName
            val appName = info.loadLabel(packageManager).toString()
            Log.d("APP_DEBUG", "Installed: $appName - $packageName")

            if (isAppApproved(packageName)) {
                appList.add(AppInfo(appName, packageName))
            }
        }
        return appList
    }

    private fun isAppApproved(packageName: String): Boolean {
        return packageName in listOf(
            "com.google.android.youtube",
            "com.netflix.mediaclient",
            "com.disney.plus",
            "com.google.android.videos",
            "com.google.android.apps.messaging"
        )
    }
}