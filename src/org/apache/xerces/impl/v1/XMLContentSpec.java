/*
 * The Apache Software License, Version 1.1
 *
 *
 * Copyright (c) 1999-2001 The Apache Software Foundation.  All rights 
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

package org.apache.xerces.impl.v1;

import org.apache.xerces.xni.QName;

/**
 * ContentSpec really exists to aid the parser classes in implementing
 * access to the grammar.
 * <p>
 * This class is used by the DTD scanner and the validator classes,
 * allowing them to be used separately or together.  This "struct"
 * class is used to build content models for validation, where it
 * is more efficient to fetch all of the information for each of
 * these content model "fragments" than to fetch each field one at
 * a time.  Since configurations are allowed to have validators
 * without a DTD scanner (i.e. a schema validator) and a DTD scanner
 * without a validator (non-validating processor), this class can be
 * used by each without requiring the presence of the other.
 * <p>
 * When processing element declarations, the DTD scanner will build
 * up a representation of the content model using the node types that
 * are defined here.  Since a non-validating processor only needs to
 * remember the type of content model declared (i.e. ANY, EMPTY, MIXED,
 * or CHILDREN), it is free to discard the specific details of the
 * MIXED and CHILDREN content models described using this class.
 * <p>
 * In the typical case of a validating processor reading the grammar
 * of the document from a DTD, the information about the content model
 * declared will be preserved and later "compiled" into an efficient
 * form for use during element validation.  Each content spec node
 * that is saved is assigned a unique index that is used as a handle
 * for the "left" or "right" fields of other content spec nodes.
 * <p>
 * A leaf node has a "qname" that either contains the qname information
 * or has a localpart value of null to indicate the special "#PCDATA" 
 * leaf type used in a mixed content model.
 * <p>
 * For a mixed content model, the content spec will be made up of
 * leaf and choice content spec nodes, with an optional "zero or more"
 * node.  For example, the mixed content declaration "(#PCDATA)" would
 * contain a single leaf node with a node value of -1.  A mixed content
 * declaration of "(#PCDATA|foo)*" would have a content spec consisting
 * of two leaf nodes, for the "#PCDATA" and "foo" choices, a choice node
 * with the "value" set to the index of the "#PCDATA" leaf node and the
 * "otherValue" set to the index of the "foo" leaf node, and a "zero or
 * more" node with the "value" set to the index of the choice node.  If
 * the content model has more choices, for example "(#PCDATA|a|b)*", then
 * there will be more corresponding choice and leaf nodes, the choice
 * nodes will be chained together through the "value" field with each
 * leaf node referenced by the "otherValue" field.
 * <p>
 * For element content models, there are sequence nodes and also "zero or
 * one" and "one or more" nodes.  The leaf nodes would always have a valid
 * string pool index, as the "#PCDATA" leaf is not used in the declarations
 * for element content models.
 *
 * @version $Id$
 */
public class XMLContentSpec {

    //
    // Constants
    //

    /** 
     * Name or #PCDATA. Leaf nodes that represent parsed character
     * data (#PCDATA) have qname.localpart of null.
     */
    public static final int CONTENTSPECNODE_LEAF = 0;

    /** Represents a zero or one occurence count, '?'. */
    public static final int CONTENTSPECNODE_ZERO_OR_ONE = 1;

    /** Represents a zero or more occurence count, '*'. */
    public static final int CONTENTSPECNODE_ZERO_OR_MORE = 2;
    
    /** Represents a one or more occurence count, '+'. */
    public static final int CONTENTSPECNODE_ONE_OR_MORE = 3;
    
    /** Represents choice, '|'. */
    public static final int CONTENTSPECNODE_CHOICE = 4;
    
    /** Represents sequence, ','. */
    public static final int CONTENTSPECNODE_SEQ = 5;

    /** 
     * Represents any namespace specified namespace. When the element
     * found in the document must belong to a specific namespace, 
     * <code>otherValue</code> will contain the name of the namespace.
     * If <code>otherValue</code> is <code>-1</code> then the element
     * can be from any namespace.
     * <p>
     * Lists of valid namespaces are created from choice content spec
     * nodes that have any content spec nodes as children.
     */
    public static final int CONTENTSPECNODE_ANY = 6;

    /** 
     * Represents any other namespace (XML Schema: ##other). 
     * <p>
     * When the content spec node type is set to CONTENTSPECNODE_ANY_OTHER, 
     * <code>value</code> will contain the namespace that <em>cannot</em>
     * occur.
     */
    public static final int CONTENTSPECNODE_ANY_OTHER = 7;

    /** Represents any namespace element (including "##local"). */
    public static final int CONTENTSPECNODE_ANY_NS = 8;

    /** Represents <ALL> */
    public static final int CONTENTSPECNODE_ALL = 9;

    /** prcessContent is 'lax' **/
    public static final int CONTENTSPECNODE_ANY_LAX = 22;

    public static final int CONTENTSPECNODE_ANY_OTHER_LAX = 23;

    public static final int CONTENTSPECNODE_ANY_NS_LAX = 24;

    /** processContent is 'skip' **/
    
    public static final int CONTENTSPECNODE_ANY_SKIP = 38;

    public static final int CONTENTSPECNODE_ANY_OTHER_SKIP = 39;

    public static final int CONTENTSPECNODE_ANY_NS_SKIP = 40;
    //
    // Data
    //

    /** 
     * The content spec node type. 
     *
     * @see CONTENTSPECNODE_LEAF
     * @see CONTENTSPECNODE_ZERO_OR_ONE
     * @see CONTENTSPECNODE_ZERO_OR_MORE
     * @see CONTENTSPECNODE_ONE_OR_MORE
     * @see CONTENTSPECNODE_CHOICE
     * @see CONTENTSPECNODE_SEQ
     * @see CONTENTSPECNODE_ALL
     */
    public int type;

    /**
     * The "left hand" value of the content spec node.
     * // leaf index, single child for unary ops, left child for binary ops.
     */
    /***
    public int value;

    /**
     * The "right hand" value of the content spec node.
     * // right child for binary ops
     */
    /***
    public int otherValue;
    /***/

    // child nodes

    /** Left child. */
    public int left;

    /** Right child. */
    public int right;

    // qname info

    /** QName. */
    public final QName qname = new QName();
        
    //
    // Constructors
    //

    /** Default constructor. */
    public XMLContentSpec() {
        clear();
    }

    /** Constructs a content spec with the specified values. */
    public XMLContentSpec(int type, int left, int right) {
        setValues(type, left, right);
    }

    /** Constructs a content spec with the specified value. */
    public XMLContentSpec(QName qname) {
        setValues(qname);
    }

    /** 
     * Constructs a content spec from the values in the specified content spec.
     */
    public XMLContentSpec(XMLContentSpec contentSpec) {
        setValues(contentSpec);
    }

    /**
     * Constructs a content spec from the values specified by the given
     * content spec provider and identifier.
     */
    public XMLContentSpec(XMLContentSpec.Provider provider,
                          int contentSpecIndex) {
        setValues(provider, contentSpecIndex);
    }

    //
    // Public methods
    //

    /** Clears the values. */
    public void clear() {
        type = -1;
        left = -1;
        right = -1;
        qname.clear();
    }

    /** Sets the values. */
    public void setValues(int type, int left, int right) {
        this.type = type;
        this.left = left;
        this.right = right;
        this.qname.clear();
    }
    
    /** Sets the values. */
    public void setValues(QName qname) {
        this.type = CONTENTSPECNODE_LEAF;
        this.left = -1;
        this.right = -1;
        this.qname.setValues(qname);
    }

    /** Sets the values of the specified content spec. */
    public void setValues(XMLContentSpec contentSpec) {
        type = contentSpec.type;
        left = contentSpec.left;
        right = contentSpec.right;
        qname.setValues(contentSpec.qname);
    }

    /**
     * Sets the values from the values specified by the given content spec
     * provider and identifier. If the specified content spec cannot be
     * provided, the values of this content spec are cleared.
     */
    public void setValues(XMLContentSpec.Provider provider,
                          int contentSpecIndex) {
        if (!provider.getContentSpec(contentSpecIndex, this)) {
            clear();
        }
    }

    //
    // Public static methods
    //

    /** 
     * Returns a string representation of the specified content spec 
     * identifier in the form of a DTD element content model.
     * <p>
     * <strong>Note:</strong> This method is not namespace aware.
     */
    public static String toString(XMLContentSpec.Provider provider, 
                                  int contentSpecIndex) {

        // lookup content spec node
        XMLContentSpec contentSpec = new XMLContentSpec();
       
        if (provider.getContentSpec(contentSpecIndex, contentSpec)) {

            // build string
            StringBuffer str = new StringBuffer();
            int    parentContentSpecType = contentSpec.type & 0x0f;
            int    nextContentSpec;
            switch (parentContentSpecType) {
                case XMLContentSpec.CONTENTSPECNODE_LEAF: {
                    str.append('(');
                    String uri = contentSpec.qname.uri;
                    String localpart = contentSpec.qname.localpart;
                    if (uri == null && localpart == null) {
                        str.append("#PCDATA");
                    }
                    else {
                        str.append(localpart);
                    }
                    str.append(')');
                    break;
                }
                case XMLContentSpec.CONTENTSPECNODE_ZERO_OR_ONE: {
                    provider.getContentSpec(contentSpec.left, contentSpec);
                    nextContentSpec = contentSpec.type;

                    if (nextContentSpec == XMLContentSpec.CONTENTSPECNODE_LEAF) {
                        str.append('(');
                        str.append(contentSpec.qname.localpart);
                        str.append(')');
                    } 
                    else if ( nextContentSpec == XMLContentSpec.CONTENTSPECNODE_ONE_OR_MORE  ||
                            nextContentSpec == XMLContentSpec.CONTENTSPECNODE_ZERO_OR_MORE  ||
                            nextContentSpec == XMLContentSpec.CONTENTSPECNODE_ZERO_OR_ONE ) {
                        str.append('(' );
                        appendContentSpec(provider, contentSpec, str, 
                                          true, parentContentSpecType );
                        str.append(')');

                    } 
                    else {
                        appendContentSpec(provider, contentSpec, str, 
                                          true, parentContentSpecType );
                    }
                    str.append('?');
                    break;
                }
                case XMLContentSpec.CONTENTSPECNODE_ZERO_OR_MORE: {
                    provider.getContentSpec(contentSpec.left, contentSpec);
                    nextContentSpec = contentSpec.type;

                    if ( nextContentSpec == XMLContentSpec.CONTENTSPECNODE_LEAF) {
                        str.append('(');
                        String uri = contentSpec.qname.uri;
                        String localpart = contentSpec.qname.localpart;
                        if (uri == null && localpart == null) {
                            str.append("#PCDATA");
                        }
                        /*** // REVISIT: Figure out what should be done. ***
                        else if (contentSpec.otherValue != -1) {
                            str.append("##any:uri="+contentSpec.otherValue);
                        }
                        else if (contentSpec.value == -1) {
                            str.append("##any");
                        }
                        else {
                             appendContentSpec(provider, contentSpec, str, 
                                               true, parentContentSpecType );
                        }
                        /***/
                        str.append(')');

                    } else if( nextContentSpec == XMLContentSpec.CONTENTSPECNODE_ONE_OR_MORE  ||
                        nextContentSpec == XMLContentSpec.CONTENTSPECNODE_ZERO_OR_MORE  ||
                        nextContentSpec == XMLContentSpec.CONTENTSPECNODE_ZERO_OR_ONE ) {
                        str.append('(' );
                        appendContentSpec(provider, contentSpec, str, 
                                          true, parentContentSpecType );
                        str.append(')');
                    } else {
                        appendContentSpec(provider, contentSpec, str, 
                                          true, parentContentSpecType );

                        //str.append(stringPool.toString(contentSpec.value));
                    }
                    str.append('*');
                    break;
                }
                case XMLContentSpec.CONTENTSPECNODE_ONE_OR_MORE: {
                    provider.getContentSpec(contentSpec.left, contentSpec);
                    nextContentSpec = contentSpec.type;

                    if ( nextContentSpec == XMLContentSpec.CONTENTSPECNODE_LEAF) {
                        str.append('(');
                        String uri = contentSpec.qname.uri;
                        String localpart = contentSpec.qname.localpart;
                        if (uri == null && localpart == null) {
                            str.append("#PCDATA");
                        }
                        /*** // REVISIT: Figure out what should be done. ***
                        else if (contentSpec.otherValue != -1) {
                            str.append("##any:uri="+contentSpec.otherValue);
                        }
                        else if (contentSpec.value == -1) {
                            str.append("##any");
                        }
                        else {
                            str.append(contentSpec.value);
                        }
                        /***/
                        str.append(')');
                    } else if( nextContentSpec == XMLContentSpec.CONTENTSPECNODE_ONE_OR_MORE  ||
                        nextContentSpec == XMLContentSpec.CONTENTSPECNODE_ZERO_OR_MORE  ||
                        nextContentSpec == XMLContentSpec.CONTENTSPECNODE_ZERO_OR_ONE ) {
                        str.append('(' );
                        appendContentSpec(provider, contentSpec, str, 
                                          true, parentContentSpecType );
                        str.append(')');
                    } else {
                        appendContentSpec(provider, contentSpec, str,
                                          true, parentContentSpecType);
                    }
                    str.append('+');
                    break;
                }
                case XMLContentSpec.CONTENTSPECNODE_ALL:
                case XMLContentSpec.CONTENTSPECNODE_CHOICE:
                case XMLContentSpec.CONTENTSPECNODE_SEQ: {
                    appendContentSpec(provider, 
                                      contentSpec, str, true,
                                      parentContentSpecType );
                    break;
                }
                case XMLContentSpec.CONTENTSPECNODE_ANY: {
                    str.append("##any");
                    break;
                }
                case XMLContentSpec.CONTENTSPECNODE_ANY_OTHER: {
                    str.append("##other:uri=");
                    str.append(contentSpec.qname.uri);
                    break;
                }
                case XMLContentSpec.CONTENTSPECNODE_ANY_NS: {
                    str.append("namespace:uri=");
                    str.append(contentSpec.qname.uri);
                    break;
                }
                default: {
                    str.append("???");
                }

            } // switch type

            // return string
            return str.toString();
        }

        // not found
        return null;

    } // toString(XMLContentSpec.Provider):String

    //
    // Object methods
    //

    /** Returns a hash code for this node. */
    public int hashCode() {
        return type << 16 | 
               left << 8 |
               right;
    }

    /** Returns true if the two objects are equal. */
    public boolean equals(Object object) {
        if (object != null && object instanceof XMLContentSpec) {
            XMLContentSpec contentSpec = (XMLContentSpec)object;
            return type == contentSpec.type &&
                   left == contentSpec.left &&
                   right == contentSpec.right && 
                   qname.uri == contentSpec.qname.uri &&
                   qname.localpart == contentSpec.qname.localpart;
        }
        return false;
    }

    //
    // Private static methods
    //

    /**
     * Appends more information to the current string buffer.
     * <p>
     * <strong>Note:</strong> This method does <em>not</em> preserve the
     * contents of the content spec node.
     */
    private static void appendContentSpec(XMLContentSpec.Provider provider,
                                          XMLContentSpec contentSpec, 
                                          StringBuffer str, 
                                          boolean parens,
                                          int     parentContentSpecType ) {

        int thisContentSpec = contentSpec.type & 0x0f;
        switch (thisContentSpec) {
            case XMLContentSpec.CONTENTSPECNODE_LEAF: {
                String uri = contentSpec.qname.uri;
                String localpart = contentSpec.qname.localpart;
                if (uri == null && localpart == null) {
                    str.append("#PCDATA");
                }
                /*** // REVISIT: Figure out what should be done. ***
                else if (contentSpec.value == -1 && contentSpec.otherValue != -1) {
                    str.append("##any:uri="+contentSpec.otherValue);
                }
                else if (contentSpec.value == -1) {
                    str.append("##any");
                }
                else {
                    str.append(contentSpec.value);
                }
                /***/
                break;
            }
            case XMLContentSpec.CONTENTSPECNODE_ZERO_OR_ONE: {
                if( parentContentSpecType == XMLContentSpec.CONTENTSPECNODE_ONE_OR_MORE  ||
                    parentContentSpecType == XMLContentSpec.CONTENTSPECNODE_ZERO_OR_MORE ||
                    parentContentSpecType == XMLContentSpec.CONTENTSPECNODE_ZERO_OR_ONE ) {
                    provider.getContentSpec(contentSpec.left, contentSpec);
                    str.append('(');
                    appendContentSpec(provider, contentSpec, str, true, thisContentSpec );
                    str.append(')');

                } 
                else {
                    provider.getContentSpec(contentSpec.left, contentSpec);
                    appendContentSpec(provider, contentSpec, str, true, thisContentSpec );
                }
                str.append('?');
                break;
            }
            case XMLContentSpec.CONTENTSPECNODE_ZERO_OR_MORE: {
                if( parentContentSpecType == XMLContentSpec.CONTENTSPECNODE_ONE_OR_MORE ||
                   parentContentSpecType == XMLContentSpec.CONTENTSPECNODE_ZERO_OR_MORE ||
                   parentContentSpecType == XMLContentSpec.CONTENTSPECNODE_ZERO_OR_ONE ) {
                   provider.getContentSpec(contentSpec.left, contentSpec);
                   str.append('(');
                   appendContentSpec(provider, contentSpec, str, true, thisContentSpec);
                   str.append(')' );
                } else{
                    provider.getContentSpec(contentSpec.left, contentSpec);
                    appendContentSpec(provider, contentSpec, str, true, thisContentSpec);
                }
                str.append('*');
                break;
            }
            case XMLContentSpec.CONTENTSPECNODE_ONE_OR_MORE: {
                if( parentContentSpecType == XMLContentSpec.CONTENTSPECNODE_ONE_OR_MORE   ||
                    parentContentSpecType == XMLContentSpec.CONTENTSPECNODE_ZERO_OR_MORE  ||
                    parentContentSpecType == XMLContentSpec.CONTENTSPECNODE_ZERO_OR_ONE ) {

                  str.append('(');
                  provider.getContentSpec(contentSpec.left, contentSpec);
                  appendContentSpec(provider, contentSpec, str, true, thisContentSpec);
                  str.append(')' );
                } else {
                    provider.getContentSpec(contentSpec.left, contentSpec);
                    appendContentSpec(provider, contentSpec, str, true, thisContentSpec);
                }
                str.append('+');
                break;
            }
            case XMLContentSpec.CONTENTSPECNODE_CHOICE:
            case XMLContentSpec.CONTENTSPECNODE_SEQ:
            case XMLContentSpec.CONTENTSPECNODE_ALL: {
                int type = contentSpec.type;
                if (parens) {
                    if (type == XMLContentSpec.CONTENTSPECNODE_ALL)
                        str.append("all(");
                    else
                        str.append('(');
                }
                int right = contentSpec.right;
                provider.getContentSpec(contentSpec.left, contentSpec);
                appendContentSpec(provider, contentSpec, str, contentSpec.type != type, thisContentSpec);
                if (right != -2) {
                if (type == XMLContentSpec.CONTENTSPECNODE_CHOICE) {
                    str.append('|');
                }
                else {
                    str.append(',');
                }
                /***
                // REVISIT: Do we need this? -Ac
                if (++index == CHUNK_SIZE) {
                    chunk++;
                    index = 0;
                }
                /***/
                provider.getContentSpec(right, contentSpec);
                appendContentSpec(provider, contentSpec, str, true, thisContentSpec);
                }
                if (parens) {
                    str.append(')');
                }
                break;
            }
            case XMLContentSpec.CONTENTSPECNODE_ANY: {
                str.append("##any");
                break;
            }
            case XMLContentSpec.CONTENTSPECNODE_ANY_OTHER: {
                str.append("##other:uri=");
                str.append(contentSpec.qname.uri);
                break;
            }
            case XMLContentSpec.CONTENTSPECNODE_ANY_NS: {
                str.append("namespace:uri=");
                str.append(contentSpec.qname.uri);
                break;
            }
            default: {
                str.append("???");
                break;
            }

        } // switch type

    } // appendContentSpec(XMLContentSpec.Provider,StringPool,XMLContentSpec,StringBuffer,boolean)

    //
    // Interfaces
    //

    /**
     * Provides a means for walking the structure built out of 
     * content spec "nodes". The user of this provider interface is
     * responsible for knowing what the content spec node values
     * "mean". If those values refer to content spec identifiers,
     * then the user can call back into the provider to get the
     * next content spec node in the structure.
     */
    public interface Provider {

        //
        // XMLContentSpec.Provider methods
        //

        /**
         * Fills in the provided content spec structure with content spec
         * information for a unique identifier.
         *
         * @param contentSpecIndex The content spec identifier. All content
         *                         spec "nodes" have a unique identifier.
         * @param contentSpec      The content spec struct to fill in with
         *                         the information.
         *
         * @return Returns true if the contentSpecIndex was found.
         */
        public boolean getContentSpec(int contentSpecIndex, XMLContentSpec contentSpec);

    } // interface Provider

} // class XMLContentSpec
