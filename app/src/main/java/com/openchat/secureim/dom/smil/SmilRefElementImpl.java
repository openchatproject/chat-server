package com.openchat.secureim.dom.smil;

import org.w3c.dom.smil.SMILRefElement;

public class SmilRefElementImpl extends SmilRegionMediaElementImpl implements
        SMILRefElement {

    SmilRefElementImpl(SmilDocumentImpl owner, String tagName) {
        super(owner, tagName);
    }

}
