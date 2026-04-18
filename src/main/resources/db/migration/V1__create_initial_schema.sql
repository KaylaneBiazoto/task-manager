CREATE TABLE users (
                       id UUID PRIMARY KEY,
                       active BOOLEAN NOT NULL DEFAULT TRUE,
                       name VARCHAR(150) NOT NULL,
                       email VARCHAR(180) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       role VARCHAR(20) NOT NULL,
                       created_at TIMESTAMP NOT NULL DEFAULT NOW(),
                       updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE projects (
                          id UUID PRIMARY KEY,
                          active BOOLEAN NOT NULL DEFAULT TRUE,
                          name VARCHAR(150) NOT NULL,
                          description TEXT,
                          owner_id UUID NOT NULL,
                          created_at TIMESTAMP NOT NULL DEFAULT NOW(),
                          updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
                          CONSTRAINT fk_projects_owner FOREIGN KEY (owner_id) REFERENCES users (id)
);

CREATE TABLE project_members (
                                 id UUID PRIMARY KEY,
                                 active BOOLEAN NOT NULL DEFAULT TRUE,
                                 project_id UUID NOT NULL,
                                 user_id UUID NOT NULL,
                                 project_role VARCHAR(20) NOT NULL,
                                 created_at TIMESTAMP NOT NULL DEFAULT NOW(),
                                 updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
                                 CONSTRAINT fk_project_members_project FOREIGN KEY (project_id) REFERENCES projects (id),
                                 CONSTRAINT fk_project_members_user FOREIGN KEY (user_id) REFERENCES users (id),
                                 CONSTRAINT uk_project_user UNIQUE (project_id, user_id)
);

CREATE TABLE tasks (
                       id UUID PRIMARY KEY,
                       active BOOLEAN NOT NULL DEFAULT TRUE,
                       project_id UUID NOT NULL,
                       assignee_id UUID,
                       title VARCHAR(200) NOT NULL,
                       description TEXT,
                       status VARCHAR(20) NOT NULL,
                       priority VARCHAR(20) NOT NULL,
                       deadline DATE,
                       created_at TIMESTAMP NOT NULL DEFAULT NOW(),
                       updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
                       CONSTRAINT fk_tasks_project FOREIGN KEY (project_id) REFERENCES projects (id),
                       CONSTRAINT fk_tasks_assignee FOREIGN KEY (assignee_id) REFERENCES users (id)
);

-- Filter tasks by status, priority, assignee and dates

CREATE INDEX idx_tasks_project_id ON tasks(project_id);
CREATE INDEX idx_tasks_assignee_id ON tasks(assignee_id);
CREATE INDEX idx_tasks_status ON tasks(status);
CREATE INDEX idx_tasks_priority ON tasks(priority);
CREATE INDEX idx_tasks_deadline ON tasks(deadline);
CREATE INDEX idx_tasks_created_at ON tasks(created_at);
