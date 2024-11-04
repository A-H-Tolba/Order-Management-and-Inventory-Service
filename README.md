Order Management and Inventory Service
==================

This is a microservices application for ordering inventory.

It consists of two microservices:
o Order Service: Manages orders by allowing users to place an order, and verifies item availability in inventory.
o Inventory Service: Manages inventory stock levels.

Distributed transactions between the two services is managed using the Saga choreography pattern, using Apache Kafka as a message broker.

Technologies
------------

- Spring Boot
- Apache Kafka
- Zookeeper
- JUnit & Mockito
- Docker Compose to link the containers.

How To Run
----------
First make sure that the properties are set correctly and as desired in you application.properties file.
Run ./mvnw clean package or mvnw.cmd clean package from each of the services directories -i.e. order-service, inventory-service-.

Once you create an order in the order application, after a while the
invoice and the shipment should be shown in the other applications.

To run the services and Kafka through Docker, install docker then run `docker-compose build`.