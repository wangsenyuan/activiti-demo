/**
 * 
 */
package demo.activiti;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import org.activiti.engine.HistoryService;
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
public class MultiInstanceUserTaskWithCompletionConditionTest extends BaseTest {

  @Autowired
  private RuntimeService runtimeService;

  @Autowired
  private TaskService taskService;

  @Autowired
  private HistoryService historyService;

  @Test
  public void testCompletionCondition() {
    Map<String, Object> variables = new HashMap<>();
    List<String> users = Arrays.asList("user1", "user2", "user3");

    variables.put("users", users);
    variables.put("rate", 0.6d);
    ProcessInstance pi =
        runtimeService.startProcessInstanceByKey("multiInstanceUserTaskWithCompletionCondition", variables);

    Task user1Task = taskService.createTaskQuery().taskAssignee("user1").singleResult();
    taskService.complete(user1Task.getId());
    Task user2Task = taskService.createTaskQuery().taskAssignee("user2").singleResult();
    taskService.complete(user2Task.getId());
    long count = historyService.createHistoricProcessInstanceQuery().finished().count();
    
    Assert.assertEquals(1, count);
  }
}
