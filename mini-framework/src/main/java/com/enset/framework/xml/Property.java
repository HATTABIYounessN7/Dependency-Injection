package com.enset.framework.xml;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;

@XmlAccessorType(XmlAccessType.FIELD)
public class Property {

    @XmlAttribute
    private String name;

    @XmlAttribute
    private String ref;

    public String getName() {
        return name;
    }

    public String getRef() {
        return ref;
    }
}
