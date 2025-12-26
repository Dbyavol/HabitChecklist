package com.example.habitchecklist.ui

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.habitchecklist.MainActivity
import com.example.habitchecklist.domain.HabitViewModel
import com.example.habitchecklist.ui.navigation.NavRoutes
import com.example.habitchecklist.ui.screens.HabitListScreen
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HabitListScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun habitListScreen_displaysTitle() {
        composeTestRule.setContent {
            // Note: This is a simplified test structure
            // In real scenario, you'd need to provide proper ViewModel and NavController
        }
        
        composeTestRule.onNodeWithText("Привычки").assertExists()
    }

    @Test
    fun habitListScreen_hasSettingsButton() {
        composeTestRule.onNodeWithContentDescription("Settings").assertExists()
    }

    @Test
    fun habitListScreen_hasAddButton() {
        composeTestRule.onNodeWithContentDescription("Add habit").assertExists()
    }
}

