package org.modelio.module.attacktreedesigner.i18n;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import com.modeliosoft.modelio.javadesigner.annotations.objid;

@objid ("71c3a654-4b1d-4f92-8bff-6a0c03212a69")
public class Messages {
    @objid ("8f26c40b-1fb6-4cf0-ac09-5517a0fe7603")
    private static ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle ("org.modelio.module.attacktreedesigner.i18n.messages");

    @objid ("c33dac11-e3f8-404a-94df-462fdb2fd240")
    private Messages() {
    }

    @objid ("b5704300-ae15-49fc-9b1f-5cbdc763d7c2")
    public static String getString(final String key) {
        try {
            return RESOURCE_BUNDLE.getString (key);
        } catch (@SuppressWarnings ("unused") MissingResourceException e) {
            return '!' + key + '!';
        }
    }

    @objid ("ef1d4402-5606-40b1-bcfb-f710e41dfb5e")
    public static String getString(final String key, final String... params) {
        try {
            return MessageFormat.format (RESOURCE_BUNDLE.getString (key),(Object[]) params);
        } catch (@SuppressWarnings ("unused") MissingResourceException e) {
            return '!' + key + '!';
        }
    }

}
