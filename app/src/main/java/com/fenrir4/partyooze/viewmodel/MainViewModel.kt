package com.fenrir4.partyooze.viewmodel

import com.fenrir4.partyooze.model.Meeting
import com.fenrir4.partyooze.repository.MainRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MainViewModel(private val repository: MainRepository) : ViewModel() {

    fun addMeetingToDb(meeting: Meeting) {
        viewModelScope.launch {
            repository.addMeetingToDb(meeting)
        }
    }

}