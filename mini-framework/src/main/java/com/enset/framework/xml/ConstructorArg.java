package com.enset.framework.xml;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;

@XmlAccessorType(XmlAccessType.FIELD)
public class ConstructorArg {

    @XmlAttribute
    private String ref;

    public String getRef() {
        return ref;
    }

}
