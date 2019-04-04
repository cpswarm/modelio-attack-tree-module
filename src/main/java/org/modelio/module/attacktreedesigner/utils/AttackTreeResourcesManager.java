/**
 * Java Class : ResourcesManager.java
 *
 * Description :
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing,
 *    software distributed under the License is distributed on an
 *    "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *    KIND, either express or implied.  See the License for the
 *    specific language governing permissions and limitations
 *    under the License.
 *
 * @category   Util
 * @package    com.modeliosoft.modelio.sysml.utils
 * @author     Modelio
 * @license    http://www.apache.org/licenses/LICENSE-2.0
 * @version    2.0.08
 **/
package org.modelio.module.attacktreedesigner.utils;

import java.io.File;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.api.module.IModule;
import org.modelio.module.attacktreedesigner.i18n.Messages;

/**
 * This class handles the AttackTree resources i.e. images, styles, property names, etc.
 * @author ebrosse
 */
@objid ("9c0cb9cc-4d32-4f21-a8a7-e8740aad69e2")
public class AttackTreeResourcesManager {
    @objid ("fdaf4b57-792d-47d0-968c-598feaa51e80")
    private IModule _mdac;

    @objid ("54e375c2-4a00-493b-ba06-f9221d9fdd0d")
    private static AttackTreeResourcesManager instance = null;

    /**
     * Method ResourcesManager
     * @author ebrosse
     */
    @objid ("2b65c795-0eda-4a06-9906-a56f44265c33")
    private AttackTreeResourcesManager() {
    }

    /**
     * Method getInstance
     * @author ebrosse
     * @return the SysMLResourcesManager instance
     */
    @objid ("8eb9710b-f739-465a-b6bc-a123b3a44272")
    public static AttackTreeResourcesManager getInstance() {
        if(instance == null){
            instance =  new AttackTreeResourcesManager();
        }
        return instance;
    }

    /**
     * This method sets the current module
     * @param module : the current module
     */
    @objid ("a5a0364b-42c7-4318-b3fd-20b91b538d21")
    public void setJMDAC(IModule module) {
        this._mdac = module;
    }

    /**
     * Method getImage
     * @author ebrosse
     * @param imageName : the name of the image file
     * @return the complete path of the image file
     */
    @objid ("89d6c5b7-640d-44be-8f65-d39b13807995")
    public String getImage(String imageName) {
        return this._mdac.getModuleContext().getConfiguration().getModuleResourcesPath() + File.separator + "res" + File.separator + "icons" + File.separator + imageName;
    }

    /**
     * Method getStyle
     * @author ebrosse
     * @param styleName : the name of the style file
     * @return the absolute path of the style file
     */
    @objid ("7624d58f-93de-4c66-844f-9e12a31260f9")
    public String getStyle(String styleName) {
        return this._mdac.getModuleContext().getConfiguration().getModuleResourcesPath() + File.separator  + "res" + File.separator + "style" + File.separator + styleName;
    }

    /**
     * Method getPropertyName
     * @author ebrosse
     * @param propertyName : the name of the property
     * @return the internationalized name of the property
     */
    @objid ("3d7d33ea-cdee-48a1-b716-c8dfca129585")
    public String getPropertyName(String propertyName) {
        return Messages.getString("Ui.Property." + propertyName + ".Name" );
    }

}
