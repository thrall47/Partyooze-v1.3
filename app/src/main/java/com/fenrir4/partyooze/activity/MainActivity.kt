package com.fenrir4.partyooze.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.viewpager.widget.ViewPager
import coil.api.load
import com.afollestad.materialdialogs.LayoutMode
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.callbacks.onDismiss
import com.afollestad.materialdialogs.customview.customView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.signature.ObjectKey
import com.core.extensions.*
import com.facebook.login.LoginManager
import com.firebase.ui.auth.AuthUI
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.iid.FirebaseInstanceId
import com.fenrir4.partyooze.R
import com.fenrir4.partyooze.adapteritem.DemoFragmentStateAdapter
import com.fenrir4.partyooze.databinding.ActivityMainBinding
import com.fenrir4.partyooze.sharedpref.AppPref
import com.fenrir4.partyooze.sharedpref.SharedPrefData
import com.fenrir4.partyooze.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.dialog_profile.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity() {

    companion object {
        fun startActivity(context: Context) {
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        }
    }

    var email:String?=null
    var firstName:String?=null
    var lastName:String?=null
    var profileImage:String?=null

     lateinit var auth: FirebaseAuth

    private var sharedPrefData:SharedPrefData?=null

    private val viewModel by viewModel<MainViewModel>() // Lazy inject ViewModel
    private lateinit var binding: ActivityMainBinding

    private val minMeetingCodeLength = 10
    private var currentUser: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorOnPrimary));
        }

        binding.bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId)
            {
                R.id.home->{
                    binding.viewPager.currentItem=0
                    true
                }
                R.id.profile->{
                    binding.viewPager.currentItem=1
                     true
                }
            }
            false
        }

        val demoFragmentStateAdapter = DemoFragmentStateAdapter(supportFragmentManager)
        binding.viewPager.adapter = demoFragmentStateAdapter


        binding.viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int)
            {

            }


            override fun onPageSelected(position: Int) {
                binding.bottomNavigationView.menu.getItem(position).isChecked = true
            }
        })




        currentUser = FirebaseAuth.getInstance().currentUser
        sharedPrefData= SharedPrefData(this)
        auth=FirebaseAuth.getInstance()
        profileImage=intent.extras?.getString("auth_uid")
        firstName=intent.extras?.getString("first_name")
        lastName=intent.extras?.getString("last_name")
        email=intent.extras?.getString("email")


        setProfileIcon()
        onProfileClick()

    }


    private fun setProfileIcon() {
        sharedPrefData= SharedPrefData(this)
//        Log.d("woqhyroiu",sharedPrefData!!.getAuthId())
        binding.ivProfile.load(sharedPrefData!!.getImage())
//        Log.d("wdqdj",sharedPrefData!!.getSkip())
        if (sharedPrefData!!.getSkip().equals("Skip"))
        {
            binding.ivProfile.setImageResource(R.drawable.ic_account)
        }
        else{
            if (currentUser != null) {

                binding.ivProfile.load(sharedPrefData!!.getImage())
            }
            val requestOptions = RequestOptions()
            requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL)
                .signature(ObjectKey(System.currentTimeMillis()))
                .encodeQuality(70)
            requestOptions.priority(Priority.IMMEDIATE)
            requestOptions.skipMemoryCache(false)
            requestOptions.onlyRetrieveFromCache(true)
            requestOptions.priority(Priority.HIGH)
            requestOptions.placeholder(R.drawable.ic_account)
            requestOptions.isMemoryCacheable
            requestOptions.diskCacheStrategy(DiskCacheStrategy.DATA)
            requestOptions.diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            requestOptions.centerCrop()

        }


    }

    private fun onProfileClick() {
        sharedPrefData= SharedPrefData(this)
//        Log.d("joqwhfdo", sharedPrefData!!.getEmail())

        binding.ivProfile.setOnClickListener {
            val profileDialog = MaterialDialog(this, BottomSheet(LayoutMode.WRAP_CONTENT)).show {
                customView(R.layout.dialog_profile)
            }

            profileDialog.apply {

                    btnUserAuthenticationStatus.text = getString(R.string.all_btn_sign_out)
                    if (currentUser != null) {

//                        Log.d("fbTest","firstNamr $firstName")
//                        ivUserProfileDialog.load(sharedPrefData!!.getImage())
//                        Log.d("joqwhfdo", sharedPrefData!!.getEmail())
                        if (sharedPrefData!!.getName()!=null)
                            tvUserName.text = sharedPrefData!!.getName()
                        tvEmail.text = sharedPrefData!!.getEmail()

                    }

                    tvUserName.text = sharedPrefData!!.getName()
                    tvEmail.text = sharedPrefData!!.getEmail()
                    val requestOptions = RequestOptions()
                    requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL)
                        .signature(ObjectKey(System.currentTimeMillis()))
                        .encodeQuality(70)
                    requestOptions.priority(Priority.IMMEDIATE)
                    requestOptions.skipMemoryCache(false)
                    requestOptions.onlyRetrieveFromCache(true)
                    requestOptions.priority(Priority.HIGH)
                    requestOptions.placeholder(R.drawable.ic_account)
                    requestOptions.isMemoryCacheable
                    requestOptions.diskCacheStrategy(DiskCacheStrategy.DATA)

                    requestOptions.diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    requestOptions.centerCrop()
//                    Log.d("weojhfo",sharedPrefData!!.getAuthId())

//


                switchDarkMode.isChecked = !AppPref.isLightThemeEnabled

                // UserAuthenticationStatus button onClick
                btnUserAuthenticationStatus.setOnClickListener {
                    dismiss()
                    val mySharedPref: SharedPreferences? = context.getSharedPreferences("filename1", 0)
                    mySharedPref!!.edit().remove("name").commit()
                    mySharedPref!!.edit().remove("email").commit()
                    mySharedPref!!.edit().remove("authId").commit()
                    mySharedPref!!.edit().remove("skip").commit()
                    LoginManager.getInstance().logOut()


                    if (currentUser != null) {
                        // User is currently signed in
                        AuthUI.getInstance().signOut(this@MainActivity).addOnCompleteListener {
                            AuthenticationActivity.startActivity(this@MainActivity)
                            finish()
                        }
                    } else {
                        // User is not signed in
                        AuthenticationActivity.startActivity(this@MainActivity)
                        finish()
                    }
                }

                // Dark Mode Switch
                switchDarkMode.setOnCheckedChangeListener { compoundButton, isChecked ->
                    dismiss()

                    // Change theme after dismiss to prevent memory leak
                    onDismiss {
                        if (isChecked) setThemeMode(AppCompatDelegate.MODE_NIGHT_YES) else setThemeMode(
                            AppCompatDelegate.MODE_NIGHT_NO
                        )
                    }
                }

                // Send feedback onClick
                tvSendFeedback.setOnClickListener {
                    startEmailIntent(
                        getString(R.string.app_feedback_contact_email),
                        getString(R.string.profile_feedback_email_subject)
                    )
                }

                // Rate app onClick
                tvRateApp.setOnClickListener {
                    openAppInGooglePlay(applicationContext.packageName, R.color.colorSurface)
                }

                // Share app onClick
                tvShareApp.setOnClickListener {
                    startShareTextIntent(
                        getString(R.string.profile_share_app_title),
                        getString(R.string.profile_share_app_text, applicationContext.packageName)
                    )
                }

                // Privacy Policy onClick
                tvPrivacyPolicy.setOnClickListener {
                    openUrl(getString(R.string.app_privacy_policy_url), R.color.colorSurface)
                }

                // Terms of Service onClick
                tvTermsOfService.setOnClickListener {
                    openUrl(getString(R.string.app_terms_of_service_url), R.color.colorSurface)
                }
            }
        }
    }

    private fun setThemeMode(themeMode: Int) {
        AppCompatDelegate.setDefaultNightMode(themeMode)
        AppPref.isLightThemeEnabled = themeMode == AppCompatDelegate.MODE_NIGHT_NO
    }

}
