package org.modelio.module.attacktreedesigner.utils;

import java.util.List;
import org.modelio.api.modelio.model.IModelingSession;
import org.modelio.metamodel.uml.infrastructure.TagParameter;
import org.modelio.metamodel.uml.infrastructure.TaggedValue;
import org.modelio.metamodel.uml.statik.Class;
import org.modelio.module.attacktreedesigner.api.IAttackTreeDesignerPeerModule;

public class TagsManager {


    public static void addParameter(IModelingSession session, Class attackElement, TaggedValue severityTaggedValue, String value) {
        TagParameter tagParameter= session.getModel().createTagParameter(value, severityTaggedValue);
        severityTaggedValue.getActual().add(tagParameter);
    }

    public static TaggedValue createTag(IModelingSession session, String tagName, Class attackElement) {
        TaggedValue severityTaggedValue = session.getModel().createTaggedValue(IAttackTreeDesignerPeerModule.MODULE_NAME, tagName, attackElement);
        return severityTaggedValue;
    }



    public static String getParameter(TaggedValue tag, String tagType) {

        String tagName = tag.getDefinition().getName();
        if (tagName.equals(tagType)) {

            List<TagParameter> actuals = tag.getActual();
            if ((actuals != null) && (actuals.size() > 0)) {
                return actuals.get(0).getValue();
            } else
                return "";

        }
        return "";
    }


}
