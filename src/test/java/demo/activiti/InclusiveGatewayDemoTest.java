/**
 * 
 */
package demo.activiti;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author senyuanwang
 *
 */
public class InclusiveGatewayDemoTest extends BaseTest {
	@Autowired
	private RuntimeService runtimeService;

	@Autowired
	private TaskService taskService;

	@Test
	public void testIt() {
		Map<String, Object> variables = new HashMap<>();
		variables.put("needToBuy", "book");

		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("inclusiveGatewayDemoProcess",
				variables);

		// variables = processInstance.getProcessVariables();

		Execution execution = runtimeService.createExecutionQuery().activityId(processInstance.getActivityId())
				.singleResult();

		Assert.assertEquals("bob", runtimeService.getVariable(execution.getId(), "whoDidIt"));
		Assert.assertNotNull(runtimeService.getVariable(execution.getId(), "bobTaskExecuted"));
		Assert.assertNull(runtimeService.getVariable(execution.getId(), "JimTaskExecuted"));

		List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstance.getId()).taskAssignee("tom")
				.list();

		Assert.assertEquals(1, tasks.size());

		taskService.complete(tasks.get(0).getId());

		processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstance.getId())
				.singleResult();

		Assert.assertNull(processInstance);
	}
}
