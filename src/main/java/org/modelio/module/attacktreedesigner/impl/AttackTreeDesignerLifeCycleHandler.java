package org.modelio.module.attacktreedesigner.impl;

import java.util.Map;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.api.module.lifecycle.DefaultModuleLifeCycleHandler;
import org.modelio.api.module.lifecycle.ModuleException;
import org.modelio.vbasic.version.Version;

@objid ("ed835c6b-d88a-46ed-8356-c0842a7baaa9")
public class AttackTreeDesignerLifeCycleHandler extends DefaultModuleLifeCycleHandler {
    @objid ("af1eeeb9-239f-4a66-8712-ebb8024f3689")
    public AttackTreeDesignerLifeCycleHandler(final AttackTreeDesignerModule module) {
        super(module);
    }

    @objid ("73618154-80a6-45ea-ba00-b620c7e597fc")
    @Override
    public boolean start() throws ModuleException {
        return super.start();
    }

    @objid ("f7f65469-862f-4d13-a62a-82eba0cc7d4c")
    @Override
    public void stop() throws ModuleException {
        super.stop();
    }

    /**
     * @param mdaPath @return
     */
    @objid ("8c0c97e9-c8c1-4a34-bb7e-d5fd2fd5623a")
    public static boolean install(final String modelioPath, final String mdaPath) throws ModuleException {
        return DefaultModuleLifeCycleHandler.install(modelioPath, mdaPath);
    }

    @objid ("3a81b1de-cf15-4ef3-9e29-17a4d19bc783")
    @Override
    public boolean select() throws ModuleException {
        return super.select();
    }

    @objid ("0d92792b-2a58-46dd-8ac5-d359aafee490")
    @Override
    public void upgrade(final Version oldVersion, final Map<String, String> oldParameters) throws ModuleException {
        super.upgrade(oldVersion, oldParameters);
    }

    @objid ("f1b811fa-ad7c-4bd1-878c-dc62168a5a5e")
    @Override
    public void unselect() throws ModuleException {
        super.unselect();
    }

}
