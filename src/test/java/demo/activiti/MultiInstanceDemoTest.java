/**
 * 
 */
package demo.activiti;

import java.io.Serializable;
import java.util.HashMap;
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
 * @author wangsenyuan
 *
 */
public class MultiInstanceDemoTest extends BaseTest {

	@Autowired
	private RuntimeService runtimeService;

	@Autowired
	private TaskService taskService;

	@Test
	public void testIt() {
		ProcessHelper processHelper = new ProcessHelper(10);

		Map<String, Object> variables = new HashMap<>();
		variables.put("processHelper", processHelper);
		variables.put("currentIndex", 0);
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("multiInstanceDemoProcess",
				variables);

		Execution execution = runtimeService.createExecutionQuery().processInstanceId(processInstance.getId())
				.activityId(processInstance.getActivityId()).singleResult();
		Assert.assertNotNull(execution);

		for (int i = 0; i < 10; i++) {
			Assert.assertEquals(i * i, runtimeService.getVariable(execution.getId(), "" + i));
		}
		Task userTask = taskService.createTaskQuery().processInstanceId(processInstance.getId()).active()
				.singleResult();
		Assert.assertNotNull(userTask);

		taskService.complete(userTask.getId());
	}
}

class ProcessHelper implements Serializable {
	public final int stopAt;

	public ProcessHelper(int stopAt) {
		this.stopAt = stopAt;
	}

	public boolean shouldStop(int index) {
		System.out.println("shouldStop called with [" + index + "]");
		return index >= stopAt;
	}
}
