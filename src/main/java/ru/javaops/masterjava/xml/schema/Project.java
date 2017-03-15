
package ru.javaops.masterjava.xml.schema;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
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
 *         &lt;element name="Name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Description" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ProjectGroups">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence maxOccurs="unbounded" minOccurs="0">
 *                   &lt;element name="GroupID" type="{http://www.w3.org/2001/XMLSchema}IDREF"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
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
    "name",
    "description",
    "projectGroups"
})
@XmlRootElement(name = "Project", namespace = "http://javaops.ru")
public class Project {

    @XmlElement(name = "Name", namespace = "http://javaops.ru", required = true)
    protected String name;
    @XmlElement(name = "Description", namespace = "http://javaops.ru", required = true)
    protected String description;
    @XmlElement(name = "ProjectGroups", namespace = "http://javaops.ru", required = true)
    protected Project.ProjectGroups projectGroups;

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription(String value) {
        this.description = value;
    }

    /**
     * Gets the value of the projectGroups property.
     * 
     * @return
     *     possible object is
     *     {@link Project.ProjectGroups }
     *     
     */
    public Project.ProjectGroups getProjectGroups() {
        return projectGroups;
    }

    /**
     * Sets the value of the projectGroups property.
     * 
     * @param value
     *     allowed object is
     *     {@link Project.ProjectGroups }
     *     
     */
    public void setProjectGroups(Project.ProjectGroups value) {
        this.projectGroups = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence maxOccurs="unbounded" minOccurs="0">
     *         &lt;element name="GroupID" type="{http://www.w3.org/2001/XMLSchema}IDREF"/>
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
        "groupID"
    })
    public static class ProjectGroups {

        @XmlElementRef(name = "GroupID", namespace = "http://javaops.ru", type = JAXBElement.class, required = false)
        protected List<JAXBElement<Object>> groupID;

        /**
         * Gets the value of the groupID property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the groupID property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getGroupID().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link JAXBElement }{@code <}{@link Object }{@code >}
         * 
         * 
         */
        public List<JAXBElement<Object>> getGroupID() {
            if (groupID == null) {
                groupID = new ArrayList<JAXBElement<Object>>();
            }
            return this.groupID;
        }

    }

}
