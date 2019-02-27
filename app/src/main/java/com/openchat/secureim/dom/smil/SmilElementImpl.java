package com.openchat.secureim.dom.smil;

import org.w3c.dom.DOMException;
import org.w3c.dom.smil.SMILElement;

import com.openchat.secureim.dom.ElementImpl;

public class SmilElementImpl extends ElementImpl implements SMILElement {
    
    SmilElementImpl(SmilDocumentImpl owner, String tagName)
    {
        super(owner, tagName.toLowerCase());
    }

    public String getId() {
        return null;
    }

    public void setId(String id) throws DOMException {

    }

}
