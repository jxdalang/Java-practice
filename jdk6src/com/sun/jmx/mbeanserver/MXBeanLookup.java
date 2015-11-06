/*
 * @(#)MXBeanLookup.java	1.15 07/09/11
 *
 * Copyright 2006 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.jmx.mbeanserver;

import static com.sun.jmx.mbeanserver.Util.*;
import java.util.Map;
import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.security.AccessController;
import javax.management.InstanceAlreadyExistsException;
import javax.management.JMX;
import javax.management.MBeanServerConnection;
import javax.management.MBeanServerInvocationHandler;
import javax.management.ObjectName;

/**
 * @since 1.6
 */

/*
 * This class handles the mapping between MXBean references and
 * ObjectNames.  Consider an MXBean interface like this:
 *
 * public interface ModuleMXBean {
 *     ProductMXBean getProduct();
 *     void setProduct(ProductMXBean product);
 * }
 *
 * This defines an attribute called "Product" whose originalType will
 * be ProductMXBean and whose openType will be ObjectName.  The
 * mapping happens as follows.
 *
 * When the MXBean's getProduct method is called, it is supposed to
 * return a reference to another MXBean, or a proxy for another
 * MXBean.  The MXBean layer has to convert this into an ObjectName.
 * If it's a reference to another MXBean, it needs to be able to look
 * up the name under which that MXBean has been registered in this
 * MBeanServer; this is the purpose of the mxbeanToObjectName map.  If
 * it's a proxy, it can check that the MBeanServer matches and if so
 * extract the ObjectName from the proxy.
 *
 * When the setProduct method is called on a proxy for this MXBean,
 * the argument can be either an MXBean reference (only really logical
 * if the proxy has a local MBeanServer) or another proxy.  So the
 * mapping logic is the same as for getProduct on the MXBean.
 *
 * When the MXBean's setProduct method is called, it needs to convert
 * the ObjectName into an object implementing the ProductMXBean
 * interface.  We could have a lookup table that reverses
 * mxbeanToObjectName, but this could violate the general JMX property
 * that you cannot obtain a reference to an MBean object.  So we
 * always use a proxy for this.  However we do have an
 * objectNameToProxy map that allows us to reuse proxy instances.
 *
 * When the getProduct method is called on a proxy for this MXBean, it
 * must convert the returned ObjectName into an instance of
 * ProductMXBean.  Again it can do this by making a proxy.
 *
 * From the above, it is clear that the logic for getX on an MXBean is
 * the same as for setX on a proxy, and vice versa.
 */
public class MXBeanLookup {
    private MXBeanLookup(MBeanServerConnection mbsc) {
	this.mbsc = mbsc;
    }

    static MXBeanLookup lookupFor(MBeanServerConnection mbsc) {
        synchronized (mbscToLookup) {
            WeakReference<MXBeanLookup> weakLookup = mbscToLookup.get(mbsc);
            MXBeanLookup lookup = (weakLookup == null) ? null : weakLookup.get();
            if (lookup == null) {
                lookup = new MXBeanLookup(mbsc);
                mbscToLookup.put(mbsc, new WeakReference<MXBeanLookup>(lookup));
            }
            return lookup;
        }
    }

    synchronized <T> T objectNameToMXBean(ObjectName name, Class<T> type) {
        WeakReference<Object> wr = objectNameToProxy.get(name);
	if (wr != null) {
	    Object proxy = wr.get();
	    if (type.isInstance(proxy))
		return type.cast(proxy);
	}
	InvocationHandler handler =
	    new MBeanServerInvocationHandler(mbsc, name);
	T proxy = JMX.newMXBeanProxy(mbsc, name, type);
	objectNameToProxy.put(name, new WeakReference<Object>(proxy));
	return proxy;
    }

    synchronized ObjectName mxbeanToObjectName(Object mxbean) {
        if (mxbean instanceof Proxy) {
	    InvocationHandler ih = Proxy.getInvocationHandler(mxbean);
	    if (ih instanceof MBeanServerInvocationHandler) {
                MBeanServerInvocationHandler mbsih =
                        (MBeanServerInvocationHandler) ih;
                if (mbsih.getMBeanServerConnection().equals(mbsc))
                    return mbsih.getObjectName();
            }
            return null;
        } else
            return mxbeanToObjectName.get(mxbean);
    }

    synchronized void addReference(ObjectName name, Object mxbean)
    throws InstanceAlreadyExistsException {
        ObjectName existing = mxbeanToObjectName.get(mxbean);
        if (existing != null) {
            String multiname = AccessController.doPrivileged(
                    new GetPropertyAction("jmx.mxbean.multiname"));
            if (!"true".equalsIgnoreCase(multiname)) {
                throw new InstanceAlreadyExistsException(
                        "MXBean already registered with name " + existing);
            }
        }
        mxbeanToObjectName.put(mxbean, name);
    }

    synchronized boolean removeReference(ObjectName name, Object mxbean) {
        if (name.equals(mxbeanToObjectName.get(mxbean))) {
            mxbeanToObjectName.remove(mxbean);
            return true;
        } else
            return false;
        /* removeReference can be called when the above condition fails,
         * notably if you try to register the same MXBean twice.
         */
    }

    private final MBeanServerConnection mbsc;
    private final WeakIdentityHashMap<Object, ObjectName>
        mxbeanToObjectName = WeakIdentityHashMap.make();
    private final Map<ObjectName, WeakReference<Object>>
        objectNameToProxy = newMap();
    private static WeakIdentityHashMap<MBeanServerConnection,
                                       WeakReference<MXBeanLookup>>
        mbscToLookup = WeakIdentityHashMap.make();
}
