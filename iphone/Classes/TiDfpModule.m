/**
 * Appcelerator Titanium Mobile
 * Copyright (c) 2010-2013 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 */

#import "TiDfpModule.h"
#import "TiBase.h"
#import "TiHost.h"
#import "TiUtils.h"
#import <AdSupport/AdSupport.h>

@implementation TiDfpModule

// this is generated for your module, please do not change it
-(id)moduleGUID
{
	return @"65901e3e-641c-4ffa-be99-ad84d18bc767";
}

// this is generated for your module, please do not change it
-(NSString*)moduleId
{
	return @"ti.dfp";
}

#pragma mark Lifecycle

-(void)startup
{
	// this method is called when the module is first loaded
	// you *must* call the superclass
	[super startup];
	
	NSLog(@"[INFO] DFP module loaded",self);
}

-(void)shutdown:(id)sender
{
	// this method is called when the module is being unloaded
	// typically this is during shutdown. make sure you don't do too
	// much processing here or the app will be quit forceably
	
	// you *must* call the superclass
	[super shutdown:sender];
}

#pragma mark Cleanup 

-(void)dealloc
{
	// release any resources that have been retained by the module
	[super dealloc];
}

#pragma mark Internal Memory Management

-(void)didReceiveMemoryWarning:(NSNotification*)notification
{
	// optionally release any resources that can be dynamically
	// reloaded once memory is available - such as caches
	[super didReceiveMemoryWarning:notification];
}

#pragma Public APIs


/**
 * Returns an alphanumeric string unique to each device, used only for serving advertisements.
 * Discussion:
 * This identifier may change—for example, if the user erases the device—so you should not cache it.
 * If the value is nil, wait and get the value again later. This happens, for example, after the device has been restarted but before the user has unlocked the device.
 * Availability
 * Available in iOS 6.0 and later.
 * Declared In
 * ASIdentifierManager.h
 */
-(NSString *) advertisingId {
    Class ASIdentifierManagerClass = NSClassFromString(@"ASIdentifierManager");
    if (ASIdentifierManagerClass) {
        id identifierManager = [ASIdentifierManagerClass sharedManager];
        if ([ASIdentifierManagerClass instancesRespondToSelector:@selector(advertisingIdentifier)]) {
            id adID = [identifierManager performSelector:@selector(advertisingIdentifier)];
            return [adID performSelector:@selector(UUIDString)]; // you can use this sUDID as an alternative to UDID
        }
    }
    return @"";
}


/**
 * Returns a Boolean value that indicates whether the user has limited ad tracking.
 * Discussion:
 * Check the value of this property before performing any advertising tracking. If the value is TRUe, use the advertising identifier only for the following purposes: frequency capping, conversion events, estimating the number of unique users, security and fraud detection, and debugging.
 * Availability:
 * Available in iOS 6.0 and later.
 * Declared In:
 * ASIdentifierManager.h
 */
- (id) adTrackingDisabled
{
    Class advertisingManagerClass = NSClassFromString(@"ASIdentifierManager");
    if ([advertisingManagerClass respondsToSelector:@selector(sharedManager)]){
        id advertisingManager = [[advertisingManagerClass class] performSelector:@selector(sharedManager)];
        
        if ([advertisingManager respondsToSelector:@selector(isAdvertisingTrackingEnabled)]){
            return NUMBOOL(![advertisingManager performSelector:@selector(isAdvertisingTrackingEnabled)]);
        }
    }
    return NUMBOOL(YES);
}

@end
