#import "React/RCTBridgeModule.h"
#import "React/RCTEventEmitter.h"

@interface RCT_EXTERN_MODULE(ARArgyleSdk, RCTEventEmitter)

RCT_EXTERN_METHOD(loginWith:(NSString *)pluginKey apiHost:(NSString *)apiHost token:(NSString *)token)
RCT_EXTERN_METHOD(dataPartners: (NSArray *)dataPartners)
RCT_EXTERN_METHOD(updateToken:(NSString *)newToken)
RCT_EXTERN_METHOD(start)

@end
