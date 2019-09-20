import {
    NativeModules, NativeEventEmitter,
} from 'react-native'
const { ARArgyleSdk } = NativeModules

// export default class ArgyleSdk {
class ArgyleSdk {

    static errorCodes = {
        NO_CONNECTION: 'NO_CONNECTION',
        SERVER_ERROR: 'SERVER_ERROR',
        EXPIRED_TOKEN: 'EXPIRED_TOKEN',
    }

    static eventsEmitter = new NativeEventEmitter(ARArgyleSdk)
    static listeners = {}

    static loginWith(pluginKey, apiHost, token) {
        ARArgyleSdk.loginWith(pluginKey, apiHost, token)
    }

    static start() {
        ARArgyleSdk.start()
    }

    static updateToken(newToken) {
        ARArgyleSdk.updateToken(newToken)
    }

    static dataPartners(availablePartnersList) {
        ARArgyleSdk.dataPartners(availablePartnersList)
    }

    static removeListenerIfAdded(key) {
        if (ArgyleSdk.listeners[key]) {
            ArgyleSdk.eventsEmitter.removeListener(key, ArgyleSdk.listeners[key])
        }
    }

    static addListener(key, callback) {
        ArgyleSdk.removeListenerIfAdded(key)
        ArgyleSdk.listeners[key] = ArgyleSdk.eventsEmitter.addListener(key, callback)
    }

    static onAccountConnected(callback) {
        ArgyleSdk.addListener("onAccountConnected", callback)
    }

    static onAccountRemoved(callback) {
        ArgyleSdk.addListener("onAccountRemoved", callback)
    }

    static onUserCreated(callback) {
        ArgyleSdk.addListener("onUserCreated", callback)
    }

    static onError(callback) {
        ArgyleSdk.addListener("onError", res => callback(res.error))
    }

    static onTokenExpired(callback) {
        ArgyleSdk.addListener("onTokenExpired", callback)
    }

}

module.exports = ArgyleSdk
