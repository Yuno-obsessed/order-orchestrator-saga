## General description of the task  

It is required to develop two microservices: "Order" and "Account".
"Account".   
The "Order" service has a single business scenario called "CreateOrder".  
When creating an order, the following condition must be met:
> Check whether the user creating the order has enough funds on his account in the service
> "Account" service to perform the operation.
> If there are enough funds, it is necessary to
> reduce their amount in the "Account" service, while creating an order in the "Order" service.
> 
Asynchronous creation of the order is allowed. After calling the "CreateOrder" method the "TraceID" will be returned, 
by which you can get information about the order using the "GetOrderByTrace" method.
However, the implementation of the latter method is optional.

#### Implementation requirement:
- The "Order" and "Account" services use different databases.
- Cases of data inconsistency in case one of the services fails and the order creation operation is interrupted.
  interruption of the order creation operation.
- The operation of order creation should be either completely executed or
  rejected, excluding the following scenarios:
  - The order has been created but the user's funds have not been debited.
  - The order was not created, but the user's funds were debited.
  - The order was created when the user did not have enough
  funds in the account.
- Each service must be packaged in a Dockerfile and linked to the others via dockercompose.
Code Requirement:
- The business scenario logic must be separated from the logic of interacting with the database
  and from the logic of query processing.

#### Results Delivery Requirement
- The code must be uploaded to a repository on GitHub or GitLab.
- The code must be in a single "mono-repository"

#### Project Startup Requirement:

- The project must be started with the "make up-services" command.After that
  all services and the necessary infrastructure must be up and running. The services
  must be available on "localhost" through the following ports:
  - The "Order" service is available on port 8000
  - The "Account" service is available on port 8001
  - The database for the "Order" service is available on port 5430
  - The database for the "Account" service is available on port 5431.
  - Any other required infrastructure can be located on
  any available ports