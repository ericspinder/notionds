package com.notionds.dataSource.management;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.management.*;

public class NotionDsMBean extends NotificationBroadcasterSupport implements DynamicMBean {

    private static final Logger log = LogManager.getLogger();


    List<String> allowedClassNamesForInvoke = new ArrayList<>();
    private Management management;

    public NotionDsMBean() {
        this.management = new Management.Default_JMX();
    }
    public NotionDsMBean(Management management) {
        this.management = management;
    }

    public Object getAttribute(String attribute_name) throws AttributeNotFoundException, MBeanException, ReflectionException {
        if (attribute_name == null) {
            throw new RuntimeOperationsException(new IllegalArgumentException("Attribute name cannot be null"), "Cannot invoke a getter of " + dClassName + " with null attribute name");
        }
        if (management.getterSupplierList.containsKey(attribute_name)) {
            return management.getterSupplierList.get(attribute_name);
        }
        else {
            throw new AttributeNotFoundException("Cannot find " + attribute_name + " attribute in " + dClassName);
        }
    }

    public void setAttribute(Attribute attribute) throws AttributeNotFoundException, InvalidAttributeValueException, MBeanException, ReflectionException {
        if (attribute == null || attribute.getName() == null || attribute.getName().isBlank() ) {
            throw new RuntimeOperationsException(new IllegalArgumentException("Attribute cannot be null nor its name"),"Cannot invoke a setter of " + dClassName + " with null attribute");
        }
        if (management.setterConsumerList.containsKey(attribute.getName())) {
            management.setterConsumerList.get(attribute.getName());
        }
        else {
            throw new AttributeNotFoundException("Attribute " + attribute.getName() + " not found in " + this.getClass().getName());
        }
    }

    public AttributeList getAttributes(String[] attributeNames) {
        if (attributeNames == null || attributeNames.length == 0) throw new RuntimeOperationsException( new IllegalArgumentException("attributeNames[] cannot be null or empty"), "Cannot invoke a getter of " + dClassName);
        AttributeList resultList = new AttributeList();
        for (int i = 0; i < attributeNames.length; i++) {
            try {
                resultList.add(new Attribute(attributeNames[i], getAttribute(attributeNames[i])));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return resultList;
    }

    public AttributeList setAttributes(AttributeList attributes) {
        if (attributes == null || attributes.isEmpty()) throw new RuntimeOperationsException( new IllegalArgumentException("AttributeList attributes cannot be null or empty"),"Cannot invoke a setter of " + dClassName);
        AttributeList resultList = new AttributeList();
        for (Iterator i = attributes.iterator(); i.hasNext(); ) {
            Attribute attr = (Attribute) i.next();
            try {
                setAttribute(attr);
                String name = attr.getName();
                resultList.add(new Attribute(name, getAttribute(name)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return resultList;
    }

    public Object invoke(String operationName, Object params[], String signatures[])  throws MBeanException, ReflectionException {
        if (operationName == null || operationName.isBlank()) throw new RuntimeOperationsException( new IllegalArgumentException("Operation name cannot be null or blank"), "Cannot invoke a null operation in " + dClassName);
        for (String signature: signatures) {
            if (signature == null || signature.isBlank()) {
                throw new RuntimeOperationsException( new IllegalArgumentException("Signature cannot be null or empty"),"Cannot invoke a setter of " + dClassName);
            }
            if (!allowedClassNamesForInvoke.contains(signature)) {
                log.error("Attempted to hydrate a class not explicitly allowed, when not part of the development process, this indicates a 'hack'.");
                throw new RuntimeOperationsException( new IllegalArgumentException("Signature cannot be null or empty"),"Attempted to hydrate a class not explicitly allowed, this indicates a 'hack' on " + dClassName );
            }
            Object o = null;
            return null;
        }
        return null;
    }

    public MBeanInfo getMBeanInfo() {
       return management.getMBeanInfo();
    }


    public void reset() {
        AttributeChangeNotification acn = new AttributeChangeNotification(this,0, 0,"NbChanges reset","NbChanges","Integer", nbChanges, 0);
        state = "initial state";
        nbChanges = 0;
        nbResets++;
        sendNotification(acn);
    }

    /**
     * Build the private dMBeanInfo field,
     * which represents the management interface exposed by the MBean,
     * that is, the set of attributes, constructors, operations and
     * notifications which are available for management.
     * <p>
     * A reference to the dMBeanInfo object is returned by the getMBeanInfo()
     * method of the DynamicMBean interface. Note that, once constructed, an
     * MBeanInfo object is immutable.
     */
    private void buildDynamicMBeanInfo() {

        dAttributes[0] =
                new MBeanAttributeInfo("State",
                        "java.lang.String",
                        "State string.",
                        true,
                        true,
                        false);
        dAttributes[1] =
                new MBeanAttributeInfo("NbChanges",
                        "java.lang.Integer",
                        "Number of times the State string has been changed.",
                        true,
                        false,
                        false);

        Constructor[] constructors = this.getClass().getConstructors();
        dConstructors[0] =
                new MBeanConstructorInfo("Constructs a SimpleDynamic object", constructors[0]);

        MBeanParameterInfo[] params = null;
        dOperations[0] =
                new MBeanOperationInfo("reset",
                        "reset State and NbChanges " +
                                "attributes to their initial values",
                        params,
                        "void",
                        MBeanOperationInfo.ACTION);

        dNotifications[0] =
                new MBeanNotificationInfo(
                        new String[]{AttributeChangeNotification.ATTRIBUTE_CHANGE},
                        AttributeChangeNotification.class.getName(),
                        "This notification is emitted when the reset() method is called.");

        dMBeanInfo = new MBeanInfo(dClassName,
                dDescription,
                dAttributes,
                dConstructors,
                dOperations,
                dNotifications);
    }

    /*
     * -----------------------------------------------------
     * PRIVATE VARIABLES
     * -----------------------------------------------------
     */

    private String state = "initial state";
    private int nbChanges = 0;
    private int nbResets = 0;

    private String dClassName = this.getClass().getName();
    private String dDescription = "Simple implementation of a dynamic MBean.";

    private MBeanAttributeInfo[] dAttributes =
            new MBeanAttributeInfo[2];
    private MBeanConstructorInfo[] dConstructors =
            new MBeanConstructorInfo[1];
    private MBeanNotificationInfo[] dNotifications =
            new MBeanNotificationInfo[1];
    private MBeanOperationInfo[] dOperations =
            new MBeanOperationInfo[1];
    private MBeanInfo dMBeanInfo = null;

}
