package com.reactlibrary

import android.app.Activity
import android.util.Log
import com.argyle.Argyle
import com.argyle.ArgyleConfig
import com.argyle.ArgyleErrorType
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.bridge.WritableMap
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.bridge.Arguments
import com.facebook.react.modules.core.DeviceEventManagerModule


/**
 * Created by yanfeng on 2017/10/12.
 */

class ARArgyleSdkModule(context: ReactApplicationContext) : ReactContextBaseJavaModule(context) {

    private var reactContext: ReactApplicationContext = context
    private var tokenHandler: ((String) -> Unit)? = null

    private var _fullHost = ""
    private var _pluginKey = ""
    private var _token = ""
    private var _partners: Array<String>? = null

    val callbackListener = object : Argyle.ArgyleResultListener {
        override fun onTokenExpired(handler: (String) -> Unit) {
            tokenHandler = handler
        }

        override fun onAccountConnected(accountId: String, userId: String) {
            val params = Arguments.createMap()
            params.putString("accountId", accountId)
            params.putString("userId", userId)
            sendEvent("onAccountConnected", params)
        }

        override fun onAccountRemoved(accountId: String, userId: String) {
            val params = Arguments.createMap()
            params.putString("accountId", accountId)
            params.putString("userId", userId)
            sendEvent("onAccountRemoved", params)
        }

        override fun onError(error: ArgyleErrorType) {
            super.onError(error)
            Log.d("Result", "onError: error: $error")
    
            var translatedError = "SERVER_ERROR"

            if (error === ArgyleErrorType.NO_CONNECTION) {
                translatedError = "NO_CONNECTION"
            }
            else if (error === ArgyleErrorType.EXPIRED_TOKEN) {
                translatedError = "EXPIRED_TOKEN"
            }

            val params = Arguments.createMap()
            params.putString("error", translatedError)
            sendEvent("onError", params)
        }

        override fun onUserCreated(userToken: String, userId: String) {
            _token = userToken
            val params = Arguments.createMap()
            params.putString("token", userToken)
            params.putString("userId", userId)
            sendEvent("onUserCreated", params)
            // return ["onAccountConnected", "onAccountRemoved", "onUserCreated", "onError", "onTokenExpired"]
        }
    }

    override fun getName(): String {
        return "ARArgyleSdk"
    }

    // private fun sendEvent(reactContext: ReactContext, eventName: String, params: WritableMap) {
    private fun sendEvent(eventName: String, params: WritableMap) {
        reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java).emit(eventName, params)
    }

    @ReactMethod
    fun start() {

        val activity = getCurrentActivity()

        if (activity == null) {
            // errorCallback(ERROR_NO_ACTIVITY, ERROR_NO_ACTIVITY_MESSAGE)
            return
        }
        else {
            initArgyle()
            Argyle.instance.startSDK(activity)
        }
        
    }

    private fun initArgyle() {
        val argyle = Argyle.instance

        var builder = ArgyleConfig.Builder()

        if (_token.equals("", ignoreCase = true)) {
            builder
            .loginWith(_pluginKey, _fullHost)
            .setCallbackListener(callbackListener)
        }
        else {
            builder
            .loginWith(_pluginKey, _fullHost, _token)
            .setCallbackListener(callbackListener)
        }

        if (_partners !== null) {
            builder.dataPartners(_partners)
        }

        val config = builder.build()
        argyle.init(config)
    }

    @ReactMethod
    fun dataPartners(partners: ReadableArray?) {
        if (partners === null) {
            _partners = null
        }
        else {
            val partnersArray = partners.toArrayList()
        
            var tmp = Array<String>(partnersArray.size) { "" }
            partnersArray.forEachIndexed { index, element ->
                tmp[index] = element.toString()
            }
            _partners = tmp
        }
        initArgyle()
    }

    @ReactMethod
    fun updateToken(newToken: String) {
        if (tokenHandler !== null) {
            tokenHandler?.invoke(newToken)
            tokenHandler = null
        }
        else {
            _token = newToken
            initArgyle()
        }
    }

    @ReactMethod
    fun loginWith(pluginKey: String, apiHost: String, token: String) {
        _fullHost = apiHost + "/v1/"
        _pluginKey = pluginKey
        _token = token

        initArgyle()
    }

}
