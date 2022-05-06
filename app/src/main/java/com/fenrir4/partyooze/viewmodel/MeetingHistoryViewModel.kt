package com.fenrir4.partyooze.viewmodel

import com.fenrir4.partyooze.model.Meeting
import com.fenrir4.partyooze.repository.MeetingHistoryRepository
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MeetingHistoryViewModel(private val repository: MeetingHistoryRepository) : ViewModel() {

    var meetingHistoryLiveData: LiveData<List<Meeting>> = liveData(Dispatchers.IO) {
        emitSource(repository.getMeetingHistory()) // emitSource() automatically runs on the Main thread
    }

    fun addMeetingToDb(meeting: Meeting) {
        viewModelScope.launch {
            repository.addMeetingToDb(meeting)
        }
    }

}
