/**
 * Appcelerator Titanium Mobile
 * Copyright (c) 2010-2013 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 */

#import "TiDfpViewProxy.h"

#import "TiUtils.h"

@implementation TiDfpViewProxy

-(void)requestAd:(id)args
{
    [[self view] performSelectorOnMainThread:@selector(refreshAd:)
                                  withObject:args waitUntilDone:NO];
}

@end
