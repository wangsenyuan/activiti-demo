package demo.activiti.sub;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.Deployment;
import org.activiti.spring.impl.test.SpringActivitiTestCase;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration("classpath:activiti.cfg.xml")
public class SubProcess2Test extends SpringActivitiTestCase {

  @Autowired
  private RuntimeService runtimeService;
  

  @Autowired
  private TaskService taskService;

  @Autowired
  private HistoryService historyService;
  
  @Test
  @Deployment(resources = {"processes/sub/SubProcess2.bpmn20.xml"})
  public void testSubProcess2() {
      ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("subProcess2");
      
      Task user1Task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).active().singleResult();
      
      Assert.assertNotNull(user1Task);
      
      Assert.assertEquals("user1", user1Task.getAssignee());
      
      taskService.complete(user1Task.getId());
      
      Task user2Task = taskService.createTaskQuery().taskAssignee("user2").active().singleResult();
      Assert.assertNotNull(user2Task);
      
      taskService.complete(user2Task.getId());
      
      long count = historyService.createHistoricProcessInstanceQuery().finished().count();
      Assert.assertEquals(0, count);
  }
   
}
