/*
 * @(#)ORBInitInfoExt.java	1.4 05/11/17
 *
 * Copyright 2006 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.corba.se.spi.legacy.interceptor;

import com.sun.corba.se.spi.orb.ORB ;

/** The interface defines an extension to the standard ORBInitInfo 
 * that gives access to the ORB being initialized.  Interceptors run
 * as the last stage of initialization of the ORB, so the ORB
 * instance returned by getORB is fully initialized.  Note that
 * this facility eventually shows up post-CORBA 3.0 as a result 
 * of the resolution of OMG core issue on accessing the ORB from
 * local objects.
 */
public interface ORBInitInfoExt 
{
    ORB getORB() ;
}
