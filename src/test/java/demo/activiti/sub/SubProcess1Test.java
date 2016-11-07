/**
 * 
 */
package demo.activiti.sub;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import demo.activiti.BaseTest;

/**
 * @author wangsenyuan
 *
 */
public class SubProcess1Test extends BaseTest {

  @Autowired
  private RuntimeService runtimeService;

  @Autowired
  private TaskService taskService;

  @Autowired
  private HistoryService historyService;
  
  @Test
  public void testSimpleSubProcess() {
    ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("callSubProcess1");

    Task tomTask = taskService.createTaskQuery().processInstanceId(processInstance.getId()).active().singleResult();
    
    Assert.assertNotNull(tomTask);
    
    taskService.complete(tomTask.getId());
    
    Task jimTask = taskService.createTaskQuery().processDefinitionKey("subProcess1").active().singleResult();
    
    Assert.assertNotNull(jimTask);
    Assert.assertEquals("jim", jimTask.getAssignee());
    taskService.complete(jimTask.getId());
    
    long count = historyService.createHistoricProcessInstanceQuery().finished().count();
    Assert.assertEquals(2, count);
  }
}
