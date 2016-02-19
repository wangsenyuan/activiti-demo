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

public class PingPongDemoTest extends BaseTest {
	@Autowired
	private RuntimeService runtimeService;

	@Autowired
	private TaskService taskService;

	@Test
	public void testIt() throws Exception {
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("PingPongDemoProcess");
		Task pingTask = taskService.createTaskQuery().taskAssignee("jim").singleResult();
		Assert.assertNotNull(pingTask);
		Map<String, Object> params = new HashMap<>();
		params.put("loop", true);
		taskService.complete(pingTask.getId(), params);

		Task pongTask = taskService.createTaskQuery().taskAssignee("bob").singleResult();
		Assert.assertNotNull(pongTask);

		taskService.complete(pongTask.getId(), params);

		pingTask = taskService.createTaskQuery().taskAssignee("jim").singleResult();
		Assert.assertNotNull(pingTask);
		taskService.complete(pingTask.getId(), params);

		pongTask = taskService.createTaskQuery().taskAssignee("bob").singleResult();
		Assert.assertNotNull(pongTask);

		taskService.complete(pongTask.getId(), params);

		params.put("loop", false);

		pingTask = taskService.createTaskQuery().taskAssignee("jim").singleResult();
		Assert.assertNotNull(pingTask);
		taskService.complete(pingTask.getId(), params);

		Task reportTask = taskService.createTaskQuery().taskAssignee("tom").active().singleResult();
		Assert.assertNotNull(reportTask);

		taskService.complete(reportTask.getId());
	}
}
