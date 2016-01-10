/**
 * 
 */
package demo.activiti;

import java.util.List;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * @author senyuanwang
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = { ActivitiDemoApplication.class })
@WebAppConfiguration
@IntegrationTest
public class HelloActivitiTest {

	@Autowired
	private RuntimeService runtimeService;

	@Autowired
	private TaskService taskService;

	@Test
	public void testHelloActiviti() {
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("helloActivitiProcess");

		// First, the 'phone interview' should be active
		Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).taskAssignee("bob")
				.singleResult();
		Assert.assertEquals("User Task", task.getName());

		taskService.complete(task.getId());

		List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstance.getId()).orderByTaskName()
				.asc().list();

		Assert.assertEquals(0, tasks.size());
	}
}
