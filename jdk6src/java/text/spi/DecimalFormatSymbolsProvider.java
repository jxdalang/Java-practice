/*
 * @(#)DecimalFormatSymbolsProvider.java	1.2 05/11/17
 *
 * Copyright 2006 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package java.text.spi;

import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.spi.LocaleServiceProvider;

/**
 * An abstract class for service providers that
 * provide instances of the 
 * {@link java.text.DecimalFormatSymbols DecimalFormatSymbols} class.
 *
 * @since        1.6 
 * @version      @(#)DecimalFormatSymbolsProvider.java	1.2 05/11/17
 */
public abstract class DecimalFormatSymbolsProvider extends LocaleServiceProvider {

    /**
     * Sole constructor.  (For invocation by subclass constructors, typically
     * implicit.)
     */
    protected DecimalFormatSymbolsProvider() {
    }

    /**
     * Returns a new <code>DecimalFormatSymbols</code> instance for the 
     * specified locale.
     *
     * @param locale the desired locale
     * @exception NullPointerException if <code>locale</code> is null
     * @exception IllegalArgumentException if <code>locale</code> isn't
     *     one of the locales returned from 
     *     {@link java.util.spi.LocaleServiceProvider#getAvailableLocales() 
     *     getAvailableLocales()}.
     * @return a <code>DecimalFormatSymbols</code> instance.
     * @see java.text.DecimalFormatSymbols#getInstance(java.util.Locale)
     */
    public abstract DecimalFormatSymbols getInstance(Locale locale);
}
