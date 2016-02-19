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

public class LoopExampleTest extends BaseTest {
	@Autowired
	private RuntimeService runtimeService;

	@Autowired
	private TaskService taskService;

	@Test
	public void testLoop() {
		Map<String, Object> params = new HashMap<>();
		params.put("operator", "bob");
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("loopExampleDemoProcess", params);
		List<Task> tasksForBob = taskService.createTaskQuery().processInstanceId(processInstance.getId())
				.taskAssignee("bob").list();

		Assert.assertEquals(1, tasksForBob.size());

		Task firstTask = tasksForBob.get(0);

		params = new HashMap<>();
		params.put("breakLoop", false);
		params.put("operator", "jim");

		taskService.complete(firstTask.getId(), params);

		List<Task> tasksForJim = taskService.createTaskQuery().processInstanceId(processInstance.getId())
				.taskAssignee("jim").list();
		Assert.assertEquals(1, tasksForJim.size());

		Task secondTask = tasksForJim.get(0);
		taskService.complete(secondTask.getId());
	}
}
