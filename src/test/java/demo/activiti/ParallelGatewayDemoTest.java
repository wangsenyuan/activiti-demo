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
public class ParallelGatewayDemoTest extends BaseTest {
	@Autowired
	private RuntimeService runtimeService;

	@Autowired
	private TaskService taskService;

	@Test
	public void testIt() throws InterruptedException {
		Map<String, Object> variables = new HashMap<>();
		variables.put("needToBuy", "book");

		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("parallelGatewayDemoProcess",
				variables);

		List<Task> tasksForBob = taskService.createTaskQuery().processInstanceId(processInstance.getId())
				.taskAssignee("bob").list();
		Assert.assertEquals(1, tasksForBob.size());
		List<Task> tasksForJim = taskService.createTaskQuery().processInstanceId(processInstance.getId())
				.taskAssignee("jim").list();
		Assert.assertEquals(1, tasksForJim.size());

		for (Task task : tasksForJim) {
			taskService.complete(task.getId());
		}
		tasksForBob = taskService.createTaskQuery().processInstanceId(processInstance.getId()).taskAssignee("bob")
				.list();
		Assert.assertEquals(1, tasksForBob.size());
		for (Task task : tasksForBob) {
			taskService.complete(task.getId());
		}

		// Thread.sleep(1000);

		processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstance.getId())
				.singleResult();
		Assert.assertNull(processInstance);
	}
}
