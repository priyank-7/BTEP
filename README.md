# D-Drive: Distributed Storage

## Overview

D-Drive is a distributed storage designed to provide scalable and efficient file storage across multiple nodes. It ensures high availability, fault tolerance, and seamless user experience for uploading, downloading, and managing files.

## Features

- **File Operations**: Supports file upload, download, and deletion.
- **Data Replication**: Ensures redundancy by replicating files across storage nodes.
- **Load Balancing**: Distributes user requests across storage nodes using a round-robin algorithm.
- **Authentication**: Provides token-based authentication for secure access.
- **Service Registry**: Tracks and monitors active storage nodes and load balancers.
- **Fault Tolerance**: Uses heartbeats to detect node failures and dynamically redistributes responsibilities.

## File Storage and Download Details

### File Upload

- **Storage Location**:
  - Files uploaded by users are stored in the home directory of the logged-in user of the host Operating system's **`/ddrive-storage`** directory.
  - Once uploaded, the file is automatically replicated to all other active storage nodes in the system to ensure redundancy and fault tolerance.

### File Download

- **Download Location on Client Device**:
  - Files downloaded by the client are saved in the **`Downloads`** folder of the client device.

## Architecture

The system comprises the following components:

1. **Client**: Issues commands like upload, download, and delete to interact with the system.
2. **Load Balancer**: Distributes client requests to storage nodes and validates authentication tokens.
3. **Storage Nodes**: Store files and handle replication, ensuring data availability.
4. **Service Registry**: Tracks live nodes, manages metadata, and facilitates communication between components.
5. **Database**: Stores metadata, user information, and access control details.

![Architecture Diagram](https://raw.githubusercontent.com/priyank-7/D-drive/main/Diagrams/System_Overview.png)

## File Storage Mechanism

Files are stored on all ther storage nodes and replicated across all storage nodes for redundancy. Metadata associated with files is managed by the database.

## Platforms and Technologies

### Software

- **Programming Language**: Java
- **Database**: Postgres
- **Tools**: Maven
- **Logging**: Log4j

## Installation

#### 1. Clone the repository:

```bash
git clone https://github.com/priyank-7/Distributed-Storage.git
cd Distributed-Storage
```

#### 2. Build and Run Modules

##### 1. Service Registry

```bash
cd service-registry
mvn clean install
java -jar <target/serviceRegistry.jar> <PORT>
```

Registory Will run on `<PORT>`

##### 2. Load Balancer

```bash
cd ../load-balancer
mvn clean install
java -jar <target/loadBalancer.jar> <PORT> <SERVICE_REGISTRY_IP> <SERVICE_REGISTRY_PORT>
```

Load Balancer Will run on `<PORT>`

##### 3. Storage Node

```bash
cd ../storage-node
mvn clean install
java -jar <target/server.jar> <PORT> <SERVICE_REGISTRY_IP> <SERVICE_REGISTRY_PORT>
```

Storage Node Will run on `<PORT>`, can spin up multiple Storage Nodes with different ports.

##### 4. Client

```bash
cd ../client
mvn clean install
java -jar <target/client-1.0-SNAPSHOT.jar> <LOADBALANCER_IP> <LOADBALANCER_PORT>
```

### Commands

- **`AUTH username:password`**: Login
- **`PUT File_Path`**: Upload a file to the storage system.
- **`GET File_Name`**: Retrieve a file from the system.
- **`Delete File_Name`**: Remove a file from the storage system.
- **`EXIT`**: Exit the client program

## Future Work

- WIP: Introduce dynamic load balancing
- WIP: Dockerizing all modules
- Implement end-to-end encryption for enhanced security.
- Add a user-friendly web-based UI.
