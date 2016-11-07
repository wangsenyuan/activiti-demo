/**
 * 
 */
package demo.activiti.listener;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;

/**
 * @author wangsenyuan
 *
 */
public class HandleErrorInfoListener implements ExecutionListener {

  /* (non-Javadoc)
   * @see org.activiti.engine.delegate.ExecutionListener#notify(org.activiti.engine.delegate.DelegateExecution)
   */
  @Override
  public void notify(DelegateExecution execution) throws Exception {
    execution.setVariable("message", "测试");
  }

}
