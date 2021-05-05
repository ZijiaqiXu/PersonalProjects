# Distributed Hash Table
This is an implementation of Distributed Hash Table.

The system contains two layers:
1. Business layer: performs business logic of DHT and is included under /activity.
2. Resource layer: a REST API for service request under /resource.

The system also implements remote control functionality for nodes to control each other in a classical client-server patter:
1. Client-side under /main.
2. Server-side under /remote.

The /recording contains a .mp4 video demostrating the remote control functions of the application.