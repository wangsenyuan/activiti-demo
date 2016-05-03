/**
 * 
 */
package demo.activiti;

import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * @author wangsenyuan
 *
 */
public class MultipleInstancesFixNumberTest extends BaseTest {

  @Autowired
  private RuntimeService runtimeService;

  @Autowired
  private TaskService taskService;

  @Test
  public void testParallel() {
    Map<String, Object> variables = new HashMap<>();
    variables.put("loop", 3);
    variables.put("counter", 0);
    ProcessInstance processInstance =
        runtimeService.startProcessInstanceByKey("multiInstanceFixNumberProcess", variables);

    Object variable = runtimeService.getVariable(processInstance.getId(), "counter");
    // int counter = (int) variables.get("counter");
    Assert.assertEquals(3L, variable);

    Task userTask = taskService.createTaskQuery().processInstanceId(processInstance.getId()).active()
        .taskAssignee("jim").singleResult();
    taskService.complete(userTask.getId());
  }
}

