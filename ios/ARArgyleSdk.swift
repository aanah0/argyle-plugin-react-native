import Foundation
import Argyle

@objc(ARArgyleSdk)
class ARArgyleSdk: RCTEventEmitter {
    
    var tokenHandler: ((String) -> ())? = nil
    
    override func supportedEvents() -> [String]! {
        return ["onAccountConnected", "onAccountRemoved", "onUserCreated", "onError", "onTokenExpired"]
    }
    
    override func constantsToExport() -> [AnyHashable : Any]! {
        return [
            "accountId": "",
            "userId": "",
            "token": "",
            "error": "",
        ]
    }
    
    override static func requiresMainQueueSetup() -> Bool {
        return false
    }
    
    @objc(dataPartners:)
    func dataPartners(dataPartners: [String]?) {
        _ = Argyle.shared.dataPartners(dataPartners ?? [])
    }
    
    @objc(loginWith:apiHost:token:)
    func loginWith(pluginKey: String, apiHost: String, token: String) -> Void {
        _ = Argyle.shared.loginWith(pluginKey: pluginKey, apiHost: apiHost + "/v1")
            Argyle.shared.updateToken(token)
        .resultListener(self)

    }
    
    @objc(start)
    func start() {
        DispatchQueue.main.sync {
            let argyle = Argyle.shared.updateToken("").controller
            argyle.modalPresentationStyle = .fullScreen
            
            UIApplication.shared.keyWindow?.rootViewController?.present(argyle, animated: true, completion: nil)
        }
    }
    
    @objc(updateToken:)
    func updateToken(newToken: String) {
        if (tokenHandler != nil) {
            tokenHandler!(newToken)
            tokenHandler = nil
        }
        else {
         _ = Argyle.shared.updateToken(newToken)
        }
    }

}

extension ARArgyleSdk: ArgyleResultListener {

    func onError(error: ArgyleErrorType) {
        print("APP: onError(error: \(error))")
        
        var translatedError = "SERVER_ERROR"
        
        switch error {
        case .NO_CONNECTION:
            translatedError = "NO_CONNECTION"
            break
        case .EXPIRED_TOKEN:
            translatedError = "EXPIRED_TOKEN"
        break
        default:
            translatedError = "SERVER_ERROR"
        }
        
        sendEvent(withName: "onError", body: ["error": translatedError])
    }

    func onAccountConnected(accountId: String, userId: String) {
        print("APP: onAccountConnected(accountId: \(accountId), userId: \(userId))")
        sendEvent(withName: "onAccountConnected", body: ["accountId": accountId, "userId": userId])
    }

    func onAccountRemoved(accountId: String, userId: String) {
        print("APP: onAccountRemoved(accountId: \(accountId), userId: \(userId))")
        sendEvent(withName: "onAccountRemoved", body: ["accountId": accountId, "userId": userId])
    }

    func onUserCreated(token: String, userId: String) {
        print("APP: onUserCreated((token: \(token), userId: \(userId))")
        sendEvent(withName: "onUserCreated", body: ["token": token, "userId": userId])
    }

    func onTokenExpired(handler: @escaping (String) -> ()) {
        tokenHandler = handler
    }

}
