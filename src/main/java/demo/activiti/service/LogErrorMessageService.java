package demo.activiti.service;

import org.activiti.engine.impl.pvm.delegate.ActivityBehavior;
import org.activiti.engine.impl.pvm.delegate.ActivityExecution;

public class LogErrorMessageService implements ActivityBehavior{

  @Override
  public void execute(ActivityExecution execution) throws Exception {
    String message = (String) execution.getVariable("message");
    System.out.println(message);
  }

}
