/**
 * 
 */
package demo.activiti.service;

import java.util.Map;

import org.activiti.engine.impl.pvm.delegate.ActivityBehavior;
import org.activiti.engine.impl.pvm.delegate.ActivityExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author senyuanwang
 *
 */
public class DummyService implements ActivityBehavior {
	private static final Logger logger = LoggerFactory.getLogger(DummyService.class);
	/* (non-Javadoc)
	 * @see org.activiti.engine.impl.pvm.delegate.ActivityBehavior#execute(org.activiti.engine.impl.pvm.delegate.ActivityExecution)
	 */
	@Override
	public void execute(ActivityExecution execution) throws Exception {
//		logger.info("DummyService#execute called.");
		Map<String, Object> ctx = (Map<String, Object>) execution.getVariable("ctx");
		Object ping = ctx.get("ping");
		logger.info(ping.toString());
		ctx.put("pong", "hello from dummy");
	}

}
