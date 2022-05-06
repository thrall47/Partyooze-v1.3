package com.fenrir4.partyooze.di

import com.fenrir4.partyooze.repository.MeetingHistoryRepository
import com.fenrir4.partyooze.viewmodel.MeetingHistoryViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val meetingHistoryModule = module {

    single { MeetingHistoryRepository(get()) }
    viewModel { MeetingHistoryViewModel(get()) }

}
