package com.otuscoursework

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ActivityScenario.launch
import androidx.test.core.app.ApplicationProvider
import com.otuscoursework.ui.main.MainActivity
import org.junit.After
import org.junit.Before

open class TestBase {
    lateinit var scenario: ActivityScenario<MainActivity>

    @Before
    fun before() {
        setup()
    }

    @After
    fun tearDown() {
        scenario.close()
    }

    private fun setup() {
        val intent = Intent(ApplicationProvider.getApplicationContext(), MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        scenario = launch<MainActivity>(intent)
    }
}