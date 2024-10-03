package com.randev.kmmgmaps

import cocoapods.GoogleMaps.GMSServices
import cocoapods.netfox.NFX
import kotlinx.cinterop.ExperimentalForeignApi
import platform.UIKit.UIApplication
import platform.UIKit.UIApplicationDelegateProtocol
import platform.darwin.NSObject
import kotlin.experimental.ExperimentalObjCName

/**
 * @author Raihan Arman
 * @date 28/08/24
 */

@OptIn(ExperimentalObjCName::class)
@ObjCName(swiftName = "AppDelegateAdapter")
class AppDelegateAdapter{
    @OptIn(ExperimentalForeignApi::class)
    fun application(
        application: UIApplication,
        didFinishLaunchingWithOptions: Map<Any?, *>?
    ): Boolean {
        println("first launch from kotlin")

        NFX.sharedInstance().start()
        GMSServices.provideAPIKey("AIzaSyDRSMTBiIczeoU9T8WufRsynJbNp8Yh8Hw")
        return true
    }
}