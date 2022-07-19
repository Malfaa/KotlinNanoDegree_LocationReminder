package com.udacity.project4.locationreminders.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
//Medium Test to test the repository
@MediumTest
class RemindersLocalRepositoryTest {
    private lateinit var localRepo : RemindersLocalRepository
    private lateinit var database : RemindersDatabase

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup(){
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            RemindersDatabase::class.java
        ).build()

        localRepo = RemindersLocalRepository(
            database.reminderDao()
        )
    }

    @After
    fun cleanUp(){
        database.close()
    }

    @Test
    fun saveTask_retrievesTask() = runBlocking {
        // GIVEN - a new task saved in the database
        val reminder = ReminderDTO("title", "description", "location", 5.0,5.0)
        localRepo.saveReminder(reminder)


        //WHEN
        val result = localRepo.getReminder(reminder.id)
        result as Result.Success
        assertThat(result.data.id , notNullValue() )

        //THEN
        val loadedData = result.data

        assertThat(loadedData.id, `is`(reminder.id))
        assertThat(loadedData.title, `is`(reminder.title))
        assertThat(loadedData.description, `is`(reminder.description))
        assertThat(loadedData.location, `is`(reminder.location))
        assertThat(loadedData.latitude, `is`(reminder.latitude))
        assertThat(loadedData.longitude, `is`(reminder.longitude))
    }

    @Test
    fun testDataNotFound_returnError() = runBlocking {
        val result = localRepo.getReminder("11")
        val error =  (result is Result.Error)
        assertThat(error, `is`(true))
    }
}