/*
 *
 *   Copyright (c) 2017.  Joe
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 */

package com.lovejjfg.readhub.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.text.TextUtils
import com.google.firebase.analytics.FirebaseAnalytics
import com.lovejjfg.readhub.data.Constants
import com.lovejjfg.readhub.view.*
import com.tencent.bugly.crashreport.CrashReport


/**
 * ReadHub
 * Created by Joe at 2017/8/5.
 */

object JumpUitl {

    fun jumpWeb(context: Context, url: String?) {
        try {
            if (TextUtils.isEmpty(url)) {
                return
            }
            val bundle = Bundle()
            bundle.putString("链接", url)
            FirebaseAnalytics.getInstance(context).logEvent("点击", bundle)
            val default = PreferenceManager
                    .getDefaultSharedPreferences(context)
                    .getBoolean("browser_use", false)

            if (!default) {
                val intent = Intent(context, WebActivity::class.java)
                intent.putExtra(Constants.URL, url)
                startActivity(context, intent)
            } else {
                val uri = Uri.parse(url)
                val intent = Intent(Intent.ACTION_VIEW, uri)
                startActivity(context, intent)
            }
        } catch (e: Throwable) {
            CrashReport.postCatchedException(e)
        }
    }

    fun jumpSetting(context: Context) {
        val intent = Intent(context, SettingsActivity::class.java)
        startActivity(context, intent)
    }

    fun jumpAbout(context: Context) {
        val intent = Intent(context, AboutActivity::class.java)
        startActivity(context, intent)
    }

    fun jumpTimeLine(context: Context?, id: String?) {
        val intent = Intent(context, TopicDetailActivity::class.java)
        intent.putExtra(Constants.ID, id)
        startActivity(context!!, intent)
    }

    fun jumpInstant(context: Context?, id: String?) {
        val intent = Intent(context, InstantActivity::class.java)
        intent.putExtra(Constants.ID, id)
        startActivity(context!!, intent)
    }

    fun backHome(context: Context?) {
        val intent = Intent()
        intent.action = Intent.ACTION_MAIN
        intent.addCategory(Intent.CATEGORY_HOME)
        context!!.startActivity(intent)
    }

    fun startActivity(context: Context, intent: Intent) {
        if (context is Activity) {
            val activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(context)
            ActivityCompat.startActivity(context, intent, activityOptions.toBundle())
        } else {
            context.startActivity(intent)
        }
    }


}
