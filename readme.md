# Part 1 - My Immutable Queue solution in Java # 
The source code in this project contains the my ImmutableQueue class, my Queue interface as specified for me and a 
test class for the ImmutableQueue.
 
# Part 2 - High level Google Analytics style backend system # 

## <u> System components diagram </u> ##
![Analytics backend High-level-design](./images/high-level-analytics-service-design-doc.png?raw=true)

I have chosen to use only Open Source tools that are completely cloud agnostic.
AWS and other cloud platforms are truly excellent; however, I would be very mindful 
not to tie my system to a specific cloud vendor. This stack can run entirely 
containerised in a docker swarm on kubernetes. This can be on premises or in the cloud.
This is also more easily testable than for example an AWS-bound system. I believe that 
true scalability means cloud agnosticism.

##<u>Components' roles and purpose </u> ##

##### Client's browser / Web app
Each page or Activity (Android screen) will be registered with the backend service. The page/activity will 
load an asychrounouly running script which will assign a tracking cookie to each system user (if it has not 
been previously set already) and send analytics about each and every user action to the backend system via 
a fire-and-forget XHR POST request.

##### Kong Api gateway #####
The purpose of the API gateway component of a distributed system like this is is to act as a reverse HTTP proxy which routes traffic transparently to endpoints in a micro-service of some kind based on URI paths.
Prom the perspective of an app client developer this pattern can be very preferable as it essentially obfuscates away the complexity of orchestrating calls to potentially many micro-services. It's useful if the client developer doesnt even need to know there are many services involved in the wider system.

Kong is a very popular and robust open source API gateway product. This seems to be a very appropriate choice for an API gateway outside of a cloud deployed system.</p>

##### Consul service discovery and load balancing #####
Consul is a great open source service-discovery and load-balancing product. I have used this several times. In a micro-services architecture we want scalability, availability and fault tolerance.
When we use a service-discoverer and load balancer like Consul we have the ability to register several instances of a service with consul. Using its gossip-based protocol, consul can route the request to the most  available instance of that service and will balance the load of many requests accross all relevant instances as evenly as possible. The caller of the service doesnt even need to know there is more than 1 service instance. 
If a service goes down, the hope is that the request can be routed to an available active instance.

An aside: there are some very compelling arguments against using a service such that Netflix has created and made available in the Java Spring Boot Cloud framework/project.
firstly, this ties us into using a particular language and sticking to it. It also means the app itself 'knows about' the load balancer and service discoverer.
Using consul, we spin up our services inside docker containers and just add a .confd file inside the linux OS running in the container which registers itself with the Consul service discoverer. This is a great decoupling (we should always strive for this).

##### Micro-services #####
I feel that the following 2 suggested services are really the bare minimum split between domain actions 
that I would recommend. Essentially one service handles writes while the other handles reads. These can 
be separately scaled as I predict that write volume is far higher than the read volume.

<ul>Log analytics service</ul>
This service listens to requests from the clients app and upon request is in charge of processing and finally persisting analalytical data as and when it is streamed into the service.

<ul>Query analytics service</ul>
This service will will aggregate and present processed analytical data to the client (dashboard app) upon request. There could also be implemented push functionality to push out changes to the client periodically or after x amount of data becomes available.
This functionality, however was not in the specification and I did not wish to over engineer my solution.

If we deploy our micro-services in docker containers then we can just register each container with consul and we dont 
have any coupling between the services. Also we are free free to write the individual micro-services in the language 
most appropriate for each each use-case.

Java (in particularly with the huge Spring/Spring Boot web framework) and the JVM are only a good fir in micro-services 
architecture in a service that is long-running and can be run inside a container ideally.
The function-as-a-service model that we often use in micro-services and/or cloud based computing is not a very good fit 
for Java Spring based  apps due to their slow cold-start time and slow development cycle.
I am much more inclined to use a very fast compiling (Go) or interpreted language (JavaScript) in many cases. Of course 
this depends a great deal on the specific use-case.

##### Kafka event stream #####
Kafka is a well known, distributed event log. Sometimes we may refer to a product such as Kafka as an 'event bus' or 'event stream'.
The power of Kafka can be leveraged in order to build realtime data streaming platforms.
A service can publish an event or 'topic' on the event stream. Other services can be 'listening' to topics of this can and will be very quickly notified of the change and can respond accordingly.
This method essentially moves the coupling of services from each other and instead moves this dependency into one place. the event stream.
For the most part this is much better because services need not know about each other and can be developed and deployed completely independently of each other.
It is important to chose a service that is proven to be robust and battle tested (and not to make the solution myself - others have already been through the pain).
Kafka is a great choice, although something supporting AMQP protocol such as RabbitMQ would be great too.

##### Apache spark #####
<ul>Parallel stream processing</ul>
Can process streams of data in real time, in parallel.

<ul>Ignite - in memory DB</ul>
In memory DB (similar to Redis). Easily compatible with Apache Spark tools.

<ul>Process time-series data</ul>
Spark is ideal for processing live time-series data streams.

I have to be honest.....I was aware that Apache Spark can be used to process time-series data streams, however until my 
pre-investigation for this assignment I was not actually aware of Apache Ignite 
in-memory database. If I did not find this I probably would have chosen Redis for 
the same needs. I prefer to use several interacting components provided by the same 
vendor as they are designed to work well together. Apache is also a very trusted 
software provider within the communities I am familiar with. 

##### InfluxDB #####
InfluxDb is a popular open source time-series column based database. There are many data persistence options available; and even more so if I consider cloud vendor specific persistence stores such as 
AWS's DynamoDB.
InfluxDB is especially optimised for long-term persistence of time-series data-sets such as those we are 
gathering from our metrics service.

##### Analytics dashboard client app #####
This app would benefit from the same kinds of availability, fault tolerance and scalability as the other 
components of this system by leveraging the same service discovery and load balancing and service component 
transparency we get from the API gateway and consul load-balancer and service discoverer.


This service should be very robust and available with the benefit of being a cloud agnostic stack. Using docker swarm or kubernetes will allow this stack to be deployed anywhere where docker can run.