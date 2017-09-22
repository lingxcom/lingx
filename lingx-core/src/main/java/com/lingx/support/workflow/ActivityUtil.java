package com.lingx.support.workflow;

import java.util.ArrayList;
import java.util.List;
/*
import org.activiti.engine.RuntimeService;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
*/
public class ActivityUtil {
	/*public static List<String> getActiveActivityIds(RuntimeService runtimeService, String processInstanceId) {  
        // 会通过id -> parent_id -> parent_id -> ... 循环找出所有的执行中的子流程  
        return runtimeService.getActiveActivityIds(processInstanceId);  
    }  
   
    public static List<ActivityImpl> getFlatAllActivities(ProcessDefinitionEntity processDefinition) {  
        List<ActivityImpl> result = new ArrayList<ActivityImpl>();  
        flattenActivities(result, processDefinition.getActivities());  
        return result;  
    }  
   
    private static void flattenActivities(List<ActivityImpl> container, List<ActivityImpl> ancestors) {  
        if (ancestors.size() > 0) {  
            for (ActivityImpl activity : ancestors) {  
                flattenActivities(container, activity.getActivities());  
            }  
            container.addAll(ancestors);  
        }  
    }  */
   
}
