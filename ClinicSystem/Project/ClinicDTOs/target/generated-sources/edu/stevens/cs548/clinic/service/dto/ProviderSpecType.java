//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2021.04.11 at 11:31:40 AM EDT 
//


package edu.stevens.cs548.clinic.service.dto;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for provider-spec-type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="provider-spec-type"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="surgery"/&gt;
 *     &lt;enumeration value="radiology"/&gt;
 *     &lt;enumeration value="internal"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "provider-spec-type")
@XmlEnum
public enum ProviderSpecType {

    @XmlEnumValue("surgery")
    SURGERY("surgery"),
    @XmlEnumValue("radiology")
    RADIOLOGY("radiology"),
    @XmlEnumValue("internal")
    INTERNAL("internal");
    private final String value;

    ProviderSpecType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ProviderSpecType fromValue(String v) {
        for (ProviderSpecType c: ProviderSpecType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
