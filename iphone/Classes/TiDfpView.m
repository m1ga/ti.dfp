/**
 * Appcelerator Titanium Mobile
 * Copyright (c) 2010-2013 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 */

#import "TiDfpView.h"
#import "TiApp.h"
#import "TiUtils.h"

@implementation TiDfpView

#pragma mark -
#pragma mark Ad Lifecycle

-(void)refreshAd
{
    NSLog(@"[DEBUG] [TiDfpView] refreshAd");
    [self refreshAd: self.bounds];
}

-(void)refreshAd:(CGRect)bounds
{
    NSLog(@"[DEBUG] [TiDfpView] refreshAd: bounds");

    if (ad != nil) {
        [ad removeFromSuperview];
        RELEASE_TO_NIL(ad);
    }
    
    int width = [TiUtils intValue:[self.proxy valueForKey:@"width"] def:0];
    int height = [TiUtils intValue:[self.proxy valueForKey:@"height"] def:0];

    if ((width > 0) && (height > 0))
    {
        NSLog(@"[DEBUG] [TiDfpView] ad size: %dx%d", width, height);
        
        GADAdSize adsize = GADAdSizeFromCGSize(CGSizeMake(width, height));
        ad = [[DFPBannerView alloc] initWithAdSize:adsize];
    }
    else
    {
        NSLog(@"[DEBUG] [TiDfpView] ad size: SMART_BANNER");
        
        ad = [[DFPBannerView alloc] initWithFrame:bounds];
    }
    
    if ([TiUtils boolValue:[self.proxy valueForKey:@"suppressScroll"] def:NO])
    {
        for (UIWebView *webView in ad.subviews)
        {
            if ([webView isKindOfClass:[UIWebView class]])
            {
                for (id view in [webView.subviews[0] subviews])
                {
                    if ([view isKindOfClass:NSClassFromString(@"UIWebBrowserView")])
                    {
                        for (UIGestureRecognizer *recognizer in [view gestureRecognizers])
                        {
                            if ([recognizer isKindOfClass:NSClassFromString(@"UIWebTouchEventsGestureRecognizer")])
                            {
                                [view removeGestureRecognizer:recognizer];
                            }
                        }
                    }
                }
            }
        }
    }

    
    // Specify the ad's "unit identifier." This is your DFP AdUnitId.
    ad.adUnitID = [self.proxy valueForKey:@"adUnitId"];
    
    NSLog(@"[DEBUG] [TiDfpView] adUnitId: %@", ad.adUnitID);

    // Let the runtime know which UIViewController to restore after taking
    // the user wherever the ad goes and add it to the view hierarchy.
    ad.rootViewController = [[TiApp app] controller];
    
    // Initiate a generic request to load it with an ad.
    GADRequest* request = [GADRequest request];
    
    NSDictionary* location = [self.proxy valueForKey:@"location"];
    if (location != nil) {
        NSLog(@"[DEBUG] [TiDfpView] location set:");
        for (NSString* key in location)
        {
            NSLog(@"[DEBUG] [TiDfpView]  - %@: %@", key, [location objectForKey:key]);
        }
        [request setLocationWithLatitude:[[location valueForKey:@"latitude"] floatValue]
                               longitude:[[location valueForKey:@"longitude"] floatValue]
                                accuracy:[[location valueForKey:@"accuracy"] floatValue]];
    }

    bool has_extras = false;
    
    GADAdMobExtras *extras = [[[GADAdMobExtras alloc] init] autorelease];
    NSMutableDictionary *additionalParameters = [[[NSMutableDictionary alloc] init] autorelease];
    
    NSDictionary* customTargeting = [self.proxy valueForKey:@"customTargeting"];
    if (customTargeting != nil)
    {
        NSLog(@"[DEBUG] [TiDfpView] custom targeting set:");
        for (NSString* key in customTargeting)
        {
            NSLog(@"[DEBUG] [TiDfpView]  - %@: %@", key, [customTargeting objectForKey:key]);
        }
        [additionalParameters addEntriesFromDictionary: customTargeting];
        has_extras = true;
    }
    
    NSString* c;
    NSString *c2;
    c = [self.proxy valueForKey:@"adBackgroundColor"];
    if (c != nil)
    {
        c2 = [c stringByReplacingOccurrencesOfString:@"#" withString:@""];
        [additionalParameters setObject:c2 forKey:@"color_bg"];
        has_extras = true;
    }
    c = [self.proxy valueForKey:@"backgroundColorTop"];
    if (c != nil)
    {
        c2 = [c stringByReplacingOccurrencesOfString:@"#" withString:@""];
        [additionalParameters setObject:c2 forKey:@"color_bg_top"];
        has_extras = true;
    }
    c = [self.proxy valueForKey:@"borderColor"];
    if (c != nil)
    {
        c2 = [c stringByReplacingOccurrencesOfString:@"#" withString:@""];
        [additionalParameters setObject:c2 forKey:@"color_border"];
        has_extras = true;
    }
    c = [self.proxy valueForKey:@"linkColor"];
    if (c != nil)
    {
        c2 = [c stringByReplacingOccurrencesOfString:@"#" withString:@""];
        [additionalParameters setObject:c2 forKey:@"color_link"];
        has_extras = true;
    }
    c = [self.proxy valueForKey:@"textColor"];
    if (c != nil)
    {
        c2 = [c stringByReplacingOccurrencesOfString:@"#" withString:@""];
        [additionalParameters setObject:c2 forKey:@"color_text"];
        has_extras = true;
    }
    c = [self.proxy valueForKey:@"urlColor"];
    if (c != nil)
    {
        c2 = [c stringByReplacingOccurrencesOfString:@"#" withString:@""];
        [additionalParameters setObject:c2 forKey:@"color_url"];
        has_extras = true;
    }

    if (has_extras)
    {
        NSLog(@"[DEBUG] [TiDfpView] additional parameters set:");
        for (NSString* key in additionalParameters)
        {
            NSLog(@"[DEBUG] [TiDfpView]  - %@: %@", key, [additionalParameters objectForKey:key]);
        }
        extras.additionalParameters = additionalParameters;
        [request registerAdNetworkExtras:extras];
    }
    
    [self addSubview:ad];
    ad.delegate = self;
    [ad loadRequest:request];
}

-(void)frameSizeChanged:(CGRect)frame bounds:(CGRect)bounds
{
    [self refreshAd:bounds];
}

-(void)dealloc
{
    if (ad != nil) {
        [ad removeFromSuperview];
        RELEASE_TO_NIL(ad);
    }
    [super dealloc];
}

#pragma mark -
#pragma mark Ad Delegate

- (void)adViewDidReceiveAd:(GADBannerView *)view
{
    [self.proxy fireEvent:@"ad_received"];
}

- (void)adView:(GADBannerView *)view didFailToReceiveAdWithError:(GADRequestError *)error
{
    NSLog(@"[ERROR] [TiDfpView] FAILURE RECEIVING AD: %@", [error localizedDescription]);
    [self.proxy fireEvent:@"ad_not_received"];
}

- (void)adViewWillPresentScreen:(GADBannerView *)adView
{
    [self.proxy fireEvent:@"ad_opened"];
}

- (void)adViewWillDismissScreen:(GADBannerView *)adView
{
    [self.proxy fireEvent:@"before_ad_closed"];
}

- (void)adViewDidDismissScreen:(GADBannerView *)adView
{
    [self.proxy fireEvent:@"ad_closed"];
}

- (void)adViewWillLeaveApplication:(GADBannerView *)adView
{
    [self.proxy fireEvent:@"leave_application"];
}


@end
