package com.udacity.project4.locationreminders.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SmallTest;
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.reminderslist.ReminderDataItem

import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;

import kotlinx.coroutines.ExperimentalCoroutinesApi;
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Test

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
//Unit test the DAO
@SmallTest
class RemindersDaoTest {
    private lateinit var database : RemindersDatabase

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun initDatabase(){
        database = Room.inMemoryDatabaseBuilder(
            getApplicationContext(),
            RemindersDatabase::class.java
        ).build()
    }

    @After
    fun closeDatabase(){
        database.close()
    }

    @Test
    fun insertReminderAndGetById()= runBlockingTest{
        //GIVEN
        val reminderItem = ReminderDTO("Title", "Description", "Location", 10.0,10.0)
        database.reminderDao().saveReminder(reminderItem)

        //WHEN
        val loadReminder = database.reminderDao().getReminderById(reminderItem.id)

        //THEN
        assertThat(loadReminder as ReminderDTO, notNullValue())
        assertThat(loadReminder.title, `is` (reminderItem.title))
        assertThat(loadReminder.description, `is` (reminderItem.description))
        assertThat(loadReminder.location, `is` (reminderItem.location))
        assertThat(loadReminder.latitude.toString(), `is` (reminderItem.latitude.toString()))
        assertThat(loadReminder.longitude.toString(), `is` (reminderItem.longitude.toString()))
    }
}