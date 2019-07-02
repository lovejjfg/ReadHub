/*
 * Copyright (c) 2017.  Joe
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lovejjfg.readhub.utils

import android.app.Activity
import android.content.Context
import android.os.Build
import androidx.core.os.bundleOf
import com.google.firebase.analytics.FirebaseAnalytics
import com.tencent.bugly.crashreport.CrashReport

/**
 * Created by joe on 2018/2/8.
 * Email: lovejjfg@gmail.com
 */
object FirebaseUtils {

    fun logEvent(context: Context, name: String) {
        logEvent(context, "tab点击", Pair("tab", name))
    }

    fun logEvent(context: Context, category: String, vararg pairs: Pair<String, Any?>) {
        try {
            val params = bundleOf(*pairs)
            FirebaseAnalytics.getInstance(context)?.logEvent(category, params)
        } catch (e: Exception) {
            CrashReport.postCatchedException(e)
        }
    }

    fun logSearchEvent(context: Context, searchKey: String) {
        logEvent(context, "搜索关键词", Pair("关键词", searchKey))
    }

    fun logSwipbackEvent(context: Context) {
        logEvent(context, "滑动关闭", Pair("activity", context::class.java.simpleName))
    }

    fun logEggCenter(context: Context) {
        logEvent(context, "彩蛋", Pair("type", "center"))
    }

    fun logEggAbout(context: Context) {
        logEvent(context, "彩蛋", Pair("type", "About"))
    }

    fun logEggText(context: Context, text: String) {
        logEvent(context, "彩蛋", Pair("type", "text"), Pair("name", text))
    }

    fun logEggRandom(context: Context) {
        logEvent(context, "彩蛋", Pair("type", "random"))
    }

    fun logScreen(context: Activity, screenName: String) {
        try {
            FirebaseAnalytics.getInstance(context).setCurrentScreen(context, "screen:$screenName", null)
        } catch (e: Exception) {
            CrashReport.postCatchedException(e)
        }
    }

    fun logMarketEvent(context: Activity) {
        logEvent(context, "应用市场", Pair("设备", Build.MODEL))
    }
}
