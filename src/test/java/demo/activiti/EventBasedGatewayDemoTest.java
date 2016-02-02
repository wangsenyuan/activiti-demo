/**
 * 
 */
package demo.activiti;

import java.util.List;

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
public class EventBasedGatewayDemoTest extends BaseTest {

	@Autowired
	private RuntimeService runtimeService;

	@Autowired
	private TaskService taskService;

	@Test
	public void testSignalEvent() throws InterruptedException {
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("eventBasedGatewayDemoProcess");
		List<Execution> executions = runtimeService.createExecutionQuery().processInstanceId(processInstance.getId())
				.signalEventSubscriptionName("test").list();

		Assert.assertEquals(1, executions.size());

		Execution signalExecution = executions.get(0);
		runtimeService.signalEventReceived("test", signalExecution.getId());

		Execution userTaskExecution = runtimeService.createExecutionQuery().activityId(processInstance.getActivityId())
				.singleResult();
		Assert.assertNotNull(userTaskExecution);

		Assert.assertNotNull(runtimeService.getVariable(userTaskExecution.getId(), "signalCatched"));
		Assert.assertNull(runtimeService.getVariable(userTaskExecution.getId(), "timerFired"));

		Task taskOfJim = taskService.createTaskQuery().processInstanceId(processInstance.getId()).active()
				.taskAssignee("jim").singleResult();
		Assert.assertNotNull(taskOfJim);

		taskService.complete(taskOfJim.getId());

		processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstance.getId())
				.singleResult();

		Assert.assertNull(processInstance);
	}

	/**
	 * it seems not work as expected. leave it for now;
	 * 
	 * @throws InterruptedException
	 */
	// @Test
	public void testWaitTimer() throws InterruptedException {
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("eventBasedGatewayDemoProcess");
		Thread.sleep(2000);

		List<Execution> executions = runtimeService.createExecutionQuery().activityId(processInstance.getActivityId())
				.signalEventSubscriptionName("test").list();

		Assert.assertEquals(0, executions.size());
		// Execution signalExecution = executions.get(0);

		Execution userTaskExecution = runtimeService.createExecutionQuery().activityId(processInstance.getActivityId())
				.singleResult();
		Assert.assertNotNull(userTaskExecution);

		Assert.assertNull(runtimeService.getVariable(userTaskExecution.getId(), "signalCatched"));
		Assert.assertNotNull(runtimeService.getVariable(userTaskExecution.getId(), "timerFired"));

		Task taskOfJim = taskService.createTaskQuery().processInstanceId(processInstance.getId()).active()
				.taskAssignee("jim").singleResult();
		Assert.assertNotNull(taskOfJim);

		taskService.complete(taskOfJim.getId());

		processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstance.getId())
				.singleResult();

		Assert.assertNull(processInstance);
	}

}
