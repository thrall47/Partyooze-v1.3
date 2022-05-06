package com.fenrir4.partyooze.activity

import com.fenrir4.partyooze.R
import com.fenrir4.partyooze.sharedpref.AppPref
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.github.appintro.AppIntro2
import com.github.appintro.AppIntroFragment
import com.github.appintro.AppIntroPageTransformerType

class AppIntroActivity : AppIntro2() {

    companion object {
        fun startActivity(context: Context) {
            val intent = Intent(context, AppIntroActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        addIntroFragments()
        setTransformer(AppIntroPageTransformerType.Parallax())
    }

    override fun onSkipPressed(currentFragment: Fragment?) {
        super.onSkipPressed(currentFragment)

        AppPref.isAppIntroShown = true
        AuthenticationActivity.startActivity(this)
        finish()
    }

    override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)

        AppPref.isAppIntroShown = true
        AuthenticationActivity.startActivity(this)
        finish()
    }

    private fun addIntroFragments() {
        addSlide(AppIntroFragment.newInstance(
            getString(R.string.app_intro_meeting_title),
            getString(R.string.app_intro_meeting_desc),
            imageDrawable = R.drawable.ic_partyoozeandroid_01,
            backgroundDrawable = R.drawable.ic_background
        ))

        addSlide(AppIntroFragment.newInstance(
            getString(R.string.app_intro_chat_title),
            getString(R.string.app_intro_chat_desc),
            imageDrawable = R.drawable.ic_partyoozeandroid_02,
            backgroundDrawable = R.drawable.ic_background
        ))

        addSlide(AppIntroFragment.newInstance(
            getString(R.string.app_intro_meeting_history_title),
            getString(R.string.app_intro_meeting_history_desc),
            imageDrawable = R.drawable.ic_partyoozeandroid_03,
            backgroundDrawable = R.drawable.ic_background
        ))
    }
}