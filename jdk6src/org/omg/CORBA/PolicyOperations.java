/*
 * @(#)PolicyOperations.java	1.12 05/11/17
 *
 * Copyright 2006 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.omg.CORBA;


/**
* The interface for <tt>Policy</tt>.  For more information on 
* Operations interfaces, see <a href="doc-files/generatedfiles.html#operations">
* "Generated Files: Operations files"</a>.
*<P>
* org/omg/CORBA/PolicyOperations.java
* Generated by the IDL-to-Java compiler (portable), version "3.0"
* from ../../../../../src/share/classes/org/omg/PortableServer/corba.idl
* Saturday, July 17, 1999 12:26:20 AM PDT
*/

/**
 * Provides the operations for a <code>Policy</code> object.
 */
public interface PolicyOperations 
{

  /** 
   * Returns the constant value that corresponds to the 
   * type of the policy object.  The values of 
   * the policy objects are allocated by the OMG.
   * New values for PolicyType should be obtained from the OMG by
   * sending mail to request@omg.org.  In general the constant
   * values that are allocated are defined in conjunction with
   * the definition of the corresponding policy object.
   *
   * @return the constant value that corresponds to the type of
   * the policy object
   */
  int policy_type ();

  /** 
   * Returns a copy of the <code>Policy</code> object. The copy does not retain
   * any relationships that the policy had with any domain or object.
   *
   * @return a copy of the <code>Policy</code> object
   */
  org.omg.CORBA.Policy copy ();

  /** 
   * Destroys the <code>Policy</code> object.  It is the responsibility of
   * the <code>Policy</code> object to determine whether it can be destroyed.
   */
  void destroy ();
} // interface PolicyOperations
