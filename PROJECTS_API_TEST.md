# PROJECTS CRUD API - Test Guide

## Base URL
```
http://localhost:8080/api/projects
```

## Endpoints

### 1. CREATE PROJECT
**Method:** POST  
**Endpoint:** `/api/projects`  
**Body:**
```json
{
  "name": "Project Alpha",
  "description": "A test project for managing tasks"
}
```
**Response:** 201 Created
```json
{
  "success": true,
  "message": "Project created successfully",
  "data": {
    "id": 1,
    "name": "Project Alpha",
    "description": "A test project for managing tasks",
    "ownerId": 1,
    "ownerName": "admin",
    "active": true,
    "createdAt": "2026-04-18T17:15:00",
    "updatedAt": "2026-04-18T17:15:00",
    "members": [
      {
        "id": 1,
        "userId": 1,
        "userName": "admin",
        "projectRole": "ADMIN",
        "active": true
      }
    ]
  }
}
```

### 2. GET PROJECT BY ID
**Method:** GET  
**Endpoint:** `/api/projects/{projectId}`  
**Example:** `/api/projects/1`  
**Response:** 200 OK
```json
{
  "success": true,
  "message": "Project retrieved successfully",
  "data": {
    "id": 1,
    "name": "Project Alpha",
    "description": "A test project for managing tasks",
    "ownerId": 1,
    "ownerName": "admin",
    "active": true,
    "createdAt": "2026-04-18T17:15:00",
    "updatedAt": "2026-04-18T17:15:00",
    "members": [
      {
        "id": 1,
        "userId": 1,
        "userName": "admin",
        "projectRole": "ADMIN",
        "active": true
      }
    ]
  }
}
```

### 3. LIST ALL PROJECTS
**Method:** GET  
**Endpoint:** `/api/projects`  
**Query Parameters:** 
- `page` (default: 0)
- `size` (default: 20)
- `sort` (default: id,desc)

**Example:** `/api/projects?page=0&size=10`  
**Response:** 200 OK
```json
{
  "success": true,
  "message": "Projects retrieved successfully",
  "data": {
    "content": [
      {
        "id": 1,
        "name": "Project Alpha",
        "description": "A test project for managing tasks",
        "ownerId": 1,
        "ownerName": "admin",
        "active": true,
        "createdAt": "2026-04-18T17:15:00",
        "updatedAt": "2026-04-18T17:15:00",
        "members": [...]
      }
    ],
    "pageable": {
      "size": 10,
      "number": 0,
      "sort": {...}
    },
    "totalElements": 1,
    "totalPages": 1
  }
}
```

### 4. UPDATE PROJECT
**Method:** PUT  
**Endpoint:** `/api/projects/{projectId}`  
**Example:** `/api/projects/1`  
**Body:**
```json
{
  "name": "Project Alpha Updated",
  "description": "Updated description"
}
```
**Response:** 200 OK

### 5. DELETE PROJECT
**Method:** DELETE  
**Endpoint:** `/api/projects/{projectId}`  
**Example:** `/api/projects/1`  
**Response:** 200 OK
```json
{
  "success": true,
  "message": "Project deleted successfully",
  "data": null
}
```

### 6. ADD PROJECT MEMBER
**Method:** POST  
**Endpoint:** `/api/projects/{projectId}/members`  
**Example:** `/api/projects/1/members`  
**Body:**
```json
{
  "userId": 2,
  "projectRole": "MEMBER"
}
```
**Response:** 201 Created
```json
{
  "success": true,
  "message": "Member added successfully",
  "data": {
    "id": 2,
    "userId": 2,
    "userName": "user1",
    "projectRole": "MEMBER",
    "active": true
  }
}
```

### 7. LIST PROJECT MEMBERS
**Method:** GET  
**Endpoint:** `/api/projects/{projectId}/members`  
**Example:** `/api/projects/1/members`  
**Response:** 200 OK
```json
{
  "success": true,
  "message": "Members retrieved successfully",
  "data": [
    {
      "id": 1,
      "userId": 1,
      "userName": "admin",
      "projectRole": "ADMIN",
      "active": true
    },
    {
      "id": 2,
      "userId": 2,
      "userName": "user1",
      "projectRole": "MEMBER",
      "active": true
    }
  ]
}
```

## Curl Examples

### Create Project
```bash
curl -X POST http://localhost:8080/api/projects \
  -H "Content-Type: application/json" \
  -d '{
    "name": "My Project",
    "description": "Project description"
  }'
```

### Get Project
```bash
curl -X GET http://localhost:8080/api/projects/1
```

### List Projects
```bash
curl -X GET "http://localhost:8080/api/projects?page=0&size=10"
```

### Update Project
```bash
curl -X PUT http://localhost:8080/api/projects/1 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Updated Project",
    "description": "Updated description"
  }'
```

### Delete Project
```bash
curl -X DELETE http://localhost:8080/api/projects/1
```

### Add Member
```bash
curl -X POST http://localhost:8080/api/projects/1/members \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 2,
    "projectRole": "MEMBER"
  }'
```

### Get Members
```bash
curl -X GET http://localhost:8080/api/projects/1/members
```

## Available Users (from test data)
- ID: 1, Username: admin, Email: admin@example.com
- ID: 2, Username: user1, Email: user1@example.com
- ID: 3, Username: user2, Email: user2@example.com
- ID: 4, Username: user3, Email: user3@example.com

## Error Handling

### 404 Not Found
```json
{
  "success": false,
  "message": "Project not found",
  "data": null
}
```

### 409 Conflict (Business Rule Violation)
```json
{
  "success": false,
  "message": "User is already a member of this project",
  "data": null
}
```

## Running the Application

```bash
java -jar target/task-manager-backend-0.0.1-SNAPSHOT.jar
```

The application will start on `http://localhost:8080`

