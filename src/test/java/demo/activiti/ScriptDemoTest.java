/**
 * 
 */
package demo.activiti;

import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
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
public class ScriptDemoTest {

	@Autowired
	private RuntimeService runtimeService;

	@Autowired
	private TaskService taskService;

	@Test
	public void testHelloActiviti() throws InterruptedException {
		Map<String, Object> variable = new HashMap<>();
		Map<String, Object> ctx = new HashMap<>();
		variable.put("ctx", ctx);
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("scriptDemoProcess", variable);

		Thread.sleep(1000);
		Object sum = ctx.get("sum");
		Assert.assertNotNull(sum);
		Assert.assertEquals(3, sum);
	}
}
