# Clinic System
This is a project called Clinic System, in the structure of Apache Maven.

For application:

The system consists of 3 layers:

1. Domain layer: 3 domains (billing, domain, and research) which is included in the module ClinicDomian. This layer interacts directly with persistent architecture (here we use PostgreSql database) using JPA and performs the business logic (patient operations, provider operations, and treatment operations).

2. Service layer: for each domain, there is a service module (ClinicBillingService, ClinicService, and ClinicResearchService respectively) acting as an intermediate for receiving clien-side service request and send it to the domain layer to perform actual business logic.

3. Resource layer: a REST API for web service request access.


The abstraction between these layers are:

1. DAO (Data Access Object) implementation for service to access domain objects.
2. MOM (Message-Oriented-Middleware) using JMS for client-service, and service-service message transfer.


For Kubernetes deployment:
1. Apply .yaml file in /dashboard to run the administration console for the cluster and access it through web browser.
2. Build docker image for postgresql database using the Dockerfile and apply the .yaml files to run the appplication pod and service.
3. Build docker image for ActiveMQ message broker using the Dockerfile and apply the .yaml files to run the broker.
4. Build docker image using the Dockerfile and apply the .yaml files to run the three web applications respectively.
5. The specific command using kubectl is included at /Kubernetes.



