package com.openchat.secureim.dom.smil;

import com.openchat.secureim.util.SmilUtil;
import org.w3c.dom.NodeList;
import org.w3c.dom.smil.SMILLayoutElement;
import org.w3c.dom.smil.SMILRootLayoutElement;

public class SmilLayoutElementImpl extends SmilElementImpl implements
        SMILLayoutElement {
    SmilLayoutElementImpl(SmilDocumentImpl owner, String tagName) {
        super(owner, tagName);
    }

    public boolean getResolved() {
        return false;
    }

    public String getType() {
        return this.getAttribute("type");
    }

    public NodeList getRegions() {
        return this.getElementsByTagName("region");
    }

    public SMILRootLayoutElement getRootLayout() {
        NodeList childNodes = this.getChildNodes();
        SMILRootLayoutElement rootLayoutNode = null;
        int childrenCount = childNodes.getLength();
        for (int i = 0; i < childrenCount; i++) {
            if (childNodes.item(i).getNodeName().equals("root-layout")) {
                rootLayoutNode = (SMILRootLayoutElement)childNodes.item(i);
            }
        }
        if (null == rootLayoutNode) {
            rootLayoutNode = (SMILRootLayoutElement) getOwnerDocument().createElement("root-layout");
            rootLayoutNode.setWidth(SmilUtil.ROOT_WIDTH);
            rootLayoutNode.setHeight(SmilUtil.ROOT_HEIGHT);
            appendChild(rootLayoutNode);
        }
        return rootLayoutNode;
    }

}
