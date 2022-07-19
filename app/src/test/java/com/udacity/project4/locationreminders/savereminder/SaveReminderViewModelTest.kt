package com.udacity.project4.locationreminders.savereminder

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.udacity.project4.locationreminders.MainCoroutineRule
import com.udacity.project4.locationreminders.data.FakeDataSource
import com.udacity.project4.locationreminders.reminderslist.ReminderDataItem
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.AutoCloseKoinTest
import org.robolectric.annotation.Config

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.P])
class SaveReminderViewModelTest: AutoCloseKoinTest() {

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    // Subject under test
    private lateinit var reminderViewModel: SaveReminderViewModel

    // Use a fake repository to be injected into the viewmodel
    private lateinit var reminderRepository: FakeDataSource

    // Set the main coroutines dispatcher for unit testing.
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setupSaveViewModel() {
        // We initialise the repository with no tasks
        reminderRepository = FakeDataSource()

        reminderViewModel =
            SaveReminderViewModel(ApplicationProvider.getApplicationContext(), reminderRepository)
    }

    private fun saveFakeReminder(): ReminderDataItem {
        return ReminderDataItem(
            "title2",
            "description2",
            "location2",
            1.00,
            2.00
        )
    }


    @Test
    fun checkLoading_status() = runBlockingTest {
        mainCoroutineRule.pauseDispatcher()
        reminderViewModel.saveReminder(saveFakeReminder())

        MatcherAssert.assertThat(
            reminderViewModel.showLoading.value,
            CoreMatchers.`is`(true)
        )

        mainCoroutineRule.resumeDispatcher()
        MatcherAssert.assertThat(
            reminderViewModel.showLoading.value,
            CoreMatchers.`is`(false)
        )
    }
}
