package com.udacity.project4.locationreminders.reminderslist

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.util.concurrent.FakeTimeLimiter
import com.udacity.project4.locationreminders.MainCoroutineRule
import com.udacity.project4.locationreminders.data.FakeDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.AutoCloseKoinTest
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
@Config(sdk = [Build.VERSION_CODES.P])
class RemindersListViewModelTest : AutoCloseKoinTest(){

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    // Subject under test
    private lateinit var reminderViewModel: RemindersListViewModel

    // Use a fake repository to be injected into the viewmodel
    private lateinit var reminderRepository: FakeDataSource


    // Set the main coroutines dispatcher for unit testing.
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setupReminderViewModel(){
        // We initialise the repository with no tasks
        reminderRepository = FakeDataSource()

        reminderViewModel = RemindersListViewModel(ApplicationProvider.getApplicationContext(),reminderRepository)
    }

    @Test
    fun saveReminder() = runBlockingTest{
        return@runBlockingTest reminderRepository.saveReminder(
            ReminderDTO(
                "title1",
                "description1",
                "location1",
                1.0,
                1.0
            )
        )
    }

    @Test
    fun test_shouldReturnError () = runBlockingTest  {
        reminderRepository.setReturnError(true)
        saveReminder()
        reminderViewModel.loadReminders()

        assertThat(
            reminderViewModel.showSnackBar.value, `is`("reminders not found")
        )
    }

    @Test
    fun loadReminder_displayContent() = runBlockingTest{
//        reminderViewModel.loadReminders()
//        assertThat(reminderViewModel.showNoData.getOrAwaitValue(), `is` (true))

        mainCoroutineRule.pauseDispatcher()
        saveReminder()
        reminderViewModel.loadReminders()

        assertThat(reminderViewModel.showLoading.value, `is`(true))

        mainCoroutineRule.resumeDispatcher()
        assertThat(reminderViewModel.showLoading.value, `is`(false))
    }
}

