//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.09.22 at 06:41:18 PM EDT 
//


package com.xstreamcompare.hashtable.generated.response_24;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="UserAction" type="{}CTAActionType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "userAction"
})
@XmlRootElement(name = "DefaultUserAction")
public class DefaultUserAction {

    @XmlElement(name = "UserAction", required = true)
    protected CTAActionType userAction;

    /**
     * Gets the value of the userAction property.
     * 
     * @return
     *     possible object is
     *     {@link CTAActionType }
     *     
     */
    public CTAActionType getUserAction() {
        return userAction;
    }

    /**
     * Sets the value of the userAction property.
     * 
     * @param value
     *     allowed object is
     *     {@link CTAActionType }
     *     
     */
    public void setUserAction(CTAActionType value) {
        this.userAction = value;
    }

}