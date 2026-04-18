package com.example.task_manager_backend;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class TaskManagerBackendApplicationTests {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Test
	void hibernateShouldCreateAllMainTables() {
		assertThat(tableExists("USERS")).isTrue();
		assertThat(tableExists("PROJECTS")).isTrue();
		assertThat(tableExists("PROJECT_MEMBERS")).isTrue();
		assertThat(tableExists("TASKS")).isTrue();
	}

	private boolean tableExists(String tableName) {
		Integer count = jdbcTemplate.queryForObject(
				"SELECT COUNT(*) FROM information_schema.tables WHERE upper(table_name) = ?",
				Integer.class,
				tableName
		);
		return count != null && count > 0;
	}

}
