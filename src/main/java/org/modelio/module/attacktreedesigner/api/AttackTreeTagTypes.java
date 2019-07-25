package org.modelio.module.attacktreedesigner.api;

import com.modeliosoft.modelio.javadesigner.annotations.objid;

@objid ("af31a1b2-cb3c-4759-8344-72297d509be4")
public interface AttackTreeTagTypes {
    @objid ("10ad6f6e-8c6b-45ef-9b13-dbf6d2d6ed5e")
    public static final String SEVERITY = "Severity";

    @objid ("ee10cda6-1aab-4cf2-9969-5147294bcdb2")
    public static final String PROBABILITY = "Probability";

    @objid ("b8e82735-64ce-40be-9243-3fae2d12fbf9")
    public static final String RISK_LEVEL = "Risk level";

    @objid ("c946fca0-fe9b-4fd5-8b85-27ca7db97752")
    public static final String SECURITY_RELATED = "Security related";

    @objid ("a3147554-875b-4e63-9418-8d50bc7e1d2a")
    public static final String SAFETY_RELATED = "Safety related";

    @objid ("e35270e4-86ba-48d1-a637-6ceed3e0d30b")
    public static final String OUT_OF_SCOPE = "Out of scope";

    @objid ("4280546a-6b03-40cb-924f-68c035a1cbae")
    public static final String CUSTOM_FIELD = "Custom field";

}
