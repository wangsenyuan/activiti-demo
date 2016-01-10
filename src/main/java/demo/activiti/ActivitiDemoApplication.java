package demo.activiti;

import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ActivitiDemoApplication {
	private static final Logger logger = LoggerFactory.getLogger(ActivitiDemoApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(ActivitiDemoApplication.class, args);
	}

	@Bean
	CommandLineRunner init(final RepositoryService repositoryService, final RuntimeService runtimeService,
			final TaskService taskService) {
		logger.info("init commandlinerunner.");
		
		return new CommandLineRunner() {

			public void run(String... strings) throws Exception {
				logger.info("commandlinerunner executed.");
				/*
				 * Map<String, Object> variables = new HashMap<String,
				 * Object>(); variables.put("applicantName", "John Doe");
				 * variables.put("email", "john.doe@activiti.com");
				 * variables.put("phoneNumber", "123456789");
				 * runtimeService.startProcessInstanceByKey("hireProcess",
				 * variables);
				 */
			}
		};
	}

	@Bean
	InitializingBean usersAndGroupsInitializer(final IdentityService identityService) {

		return new InitializingBean() {
			public void afterPropertiesSet() throws Exception {

				Group group = identityService.newGroup("user");
				group.setName("users");
				group.setType("security-role");
				identityService.saveGroup(group);

				User admin = identityService.newUser("admin");
				admin.setPassword("admin");
				identityService.saveUser(admin);
				
				logger.info("group & user added.");
			}
		};
	}
}
