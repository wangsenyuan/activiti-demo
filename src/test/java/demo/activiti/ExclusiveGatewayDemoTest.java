/**
 * 
 */
package demo.activiti;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author senyuanwang
 *
 */
public class ExclusiveGatewayDemoTest extends BaseTest {

	@Autowired
	private RuntimeService runtimeService;

	@Autowired
	private TaskService taskService;

	@Test
	public void testBuyBook() {
		Map<String, Object> variables = new HashMap<>();
		variables.put("needToBuy", "book");

		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("exclusiveGatewayDemoProcess",
				variables);

		List<Task> tasksForBob = taskService.createTaskQuery().processInstanceId(processInstance.getId())
				.taskAssignee("bob").list();
		Assert.assertEquals(1, tasksForBob.size());
		List<Task> tasksForJim = taskService.createTaskQuery().processInstanceId(processInstance.getId())
				.taskAssignee("jim").list();
		Assert.assertEquals(0, tasksForJim.size());
		for (Task task : tasksForBob) {
			taskService.complete(task.getId());
		}
		tasksForBob = taskService.createTaskQuery().processInstanceId(processInstance.getId()).taskAssignee("bob")
				.list();
		Assert.assertEquals(0, tasksForBob.size());
	}
}
