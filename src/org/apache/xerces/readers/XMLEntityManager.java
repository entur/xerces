/*
 * The Apache Software License, Version 1.1
 *
 *
 * Copyright (c) 1999,2000 The Apache Software Foundation.  All rights 
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer. 
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:  
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Xerces" and "Apache Software Foundation" must
 *    not be used to endorse or promote products derived from this
 *    software without prior written permission. For written 
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache",
 *    nor may "Apache" appear in their name, without prior written
 *    permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation and was
 * originally based on software copyright (c) 1999, International
 * Business Machines, Inc., http://www.apache.org.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 */

package org.apache.xerces.readers;

import java.io.IOException;
import org.apache.xerces.framework.XMLComponent;
import org.apache.xerces.framework.XMLComponentManager;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

/**
 * @author Stubs generated by DesignDoc on Mon Sep 18 18:23:16 PDT 2000
 * @version $Id$
 */
public class XMLEntityManager
    implements XMLComponent {

    //
    // Data
    //

    private static XMLEntityManager fEntityManagerInstance = new XMLEntityManager();


    /** fEntityResolver */
    protected EntityResolver fEntityResolver;

    /** fEntityReader */
    protected XMLEntityScanner fEntityReader;

    //
    // Constructors
    //

    /**
     * 
     */
    private XMLEntityManager() {

    }

    //
    // Methods
    //

    /**
     * addGeneralEntity
     * 
     * @param name 
     * @param publicId 
     * @param systemId 
     * @param baseSystemId 
     */
    public void addGeneralEntity(String name, String publicId, String systemId, String baseSystemId) {
    } // addGeneralEntity

    /**
     * addGeneralEntity
     * 
     * @param name 
     * @param text 
     */
    public void addGeneralEntity(String name, String text) {
    } // addGeneralEntity

    /**
     * addParameterEntity
     * 
     * @param name 
     * @param publicId 
     * @param systemId 
     * @param baseSystemId 
     */
    public void addParameterEntity(String name, String publicId, String systemId, String baseSystemId) {
    } // addParameterEntity

    /**
     * addParameterEntity
     * 
     * @param name 
     * @param text 
     */
    public void addParameterEntity(String name, String text) {
    } // addParameterEntity

    /**
     * resolveEntity
     * 
     * @param publicId 
     * @param systemId 
     * @param baseSystemId 
     * 
     * @return 
     */
    public InputSource resolveEntity(String publicId, String systemId, String baseSystemId)
        throws IOException, SAXException {
        return null;
    } // resolveEntity

    /**
     * startEntity
     * 
     * @param entityName 
     * @param parameter 
     */
    public void startEntity(String entityName, boolean parameter) {
    } // startEntity

    /**
     * startEntity
     * 
     * @param inputSource 
     */
    public void startEntity(InputSource inputSource) {
    } // startEntity

    /**
     * getEntityScanner
     * 
     * @return 
     */
    public XMLEntityScanner getEntityScanner() {
        return XMLEntityScanner.getEntityScanner();
    } // getEntityScanner


    /**
     * XMLEntityManager accessor
     * 
     * @return  Returns singlenton instance of  entity manager.
     */
    static public XMLEntityManager getEntityManager() {
        return fEntityManagerInstance;//return the only instance of it
    }


    //
    // XMLComponent methods
    //

    /**
     * reset
     * 
     * @param configurationManager 
     */
    public void reset(XMLComponentManager configurationManager)
        throws SAXException {
    } // reset

    /**
     * setFeature
     * 
     * @param featureId 
     * @param state 
     */
    public void setFeature(String featureId, boolean state)
        throws SAXNotRecognizedException, SAXNotSupportedException {
    } // setFeature

    /**
     * setProperty
     * 
     * @param propertyId 
     * @param value 
     */
    public void setProperty(String propertyId, Object value)
        throws SAXNotRecognizedException, SAXNotSupportedException {
    } // setProperty

    /* Unit test section 
    public static void main( String argv[] ) {
        System.out.println( "XMLEntityManager = " + XMLEntityManager.getEntityManager() );
        System.out.println( "XMLEntityManager again = " + XMLEntityManager.getEntityManager() ); 
        XMLEntityManager   ent = XMLEntityManager.getEntityManager();

        ent.getEntityScanner();
        
        ent = new XMLEntityManager();


    }

   */

} // class XMLEntityManager
