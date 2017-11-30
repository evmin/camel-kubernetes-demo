# Introduction

This post originated from a number of discussions I have had in the last few months. So I decided to summarise the thinking in a single blog post, so I can reference everyone back to these bullet points.

## Why Camel on Google Kubernetes Engine?

In short - a powerful combination of the best of breed technologies - Camel and Kubernetes - aimed at solving a wide range of integration tasks in an efficient and scalable manner. And on the cheap. The last point is equally important, and that's where Google pricing has an advantage.

Let me give a short overview of the technologies to set the frame of reference.

## Why Camel?

[Apache Camel](http://camel.apache.org/) is a routing and mediation engine that can be considered a reference implementation for the majority of the [Enterprise Integration Patterns](http://www.enterpriseintegrationpatterns.com/) defined by Gregor Hohpe and Bobby Woolf.

Being a top level Apache Project, it is well supported and comes with a generous [Apache2.0](http://www.apache.org/licenses/LICENSE-2.0.html) license, which is enterprise friendly.

It is easily extensible, provides a configuration [DSL](https://en.wikipedia.org/wiki/Domain-specific_language) that conforms to [the fluent interface principles](https://martinfowler.com/bliki/FluentInterface.html) as set by Martin Fowler and includes a gazillion of components and adapters.

For a more detailed review please check a very nice summary from Jonathan Anstey [Open Source Integration with Apache Camel](https://dzone.com/articles/open-source-integration-apache).

## Why Kubernetes Engine?

Why doing Containers and Kubernetes in the world where serverless computing is picking up steam? 

As much as I like serverless architecture, the container based deployment is still perceived to be a bit more flexible. And this flexibility is quite important for the integration tasks, where requirements can be somewhat peculiar in terms of dependencies, latencies and execution times. Serverless is not particularly friendly for the long running processes for example. 

Docker containers, on the other hand, strike the balance between being stateless, being easily customisable and handling the unusual tasks rather well. General adoption of Kubernetes as the emerging de-facto standard helps as well. 

Shadowing a typical DevOps team for a day usually provides enough of insight as to why the combination is so awesome. Or just Google it if more information is required.

## Why Google?

The easiest way to externalise the liability of running the container infrastructure at a very attractive price point. 

But [Google Cloud Platform](https://cloud.google.com/) is not just about the Kubernetes. It is the whole plethora of the additional capabilities - Logging, Monitoring, Persistence, Pub/Sub, Analytics, etc.

Google offering set allows designs where the architect can just pick the xPAAS capabilities needed and compose the business solution.  And again - the infrastructure is operated by someone else and it is one of the cheaper options for the comparable performance, features and quality of support.

## Why Camel at Google?

But is there a place for Apache Camel in the already rich Google Cloud Platform ecosystem? Is there a real need for its capabilities?  Why not, for example, use Spark or Apache Beam? Both available as managed infrastructure - Dataproc and Dataflow respectively. Why not the latter one?

Well, I do think that Dataflow is a great product. It is irreplaceable for the operations at scale where dynamic session calculations are a must. 

But let's consider simpler scenarios, where the focus is more on the message processing and orchestration, rather than the data handling, situations where there is only a few million exchanges a day to process, solutions where there is a number of the external systems need to be integrated. Dataflow would be coming as a tad too heavy and expensive. 

The lowest deployment entity with Dataflow is a VM and there are at least three required per a job deployment. Half a dozen of Dataflow solutions would rake up a considerable cost overhead. 

Plus there are not that many thrid party adaptors available for Dataflow as yet, so integrating would require effort and some custom, low level code.

That's where Camel on Kubernetes comes in shining. The lowest deployment component is a container - multiple deployments would fit into a single VM, even with a handful of adapters and components configured. 

The number of the Apache Camel components avalable out of the box make integration and orchestration tasks simple, and the Google ecosystem is enabled too - there are prepackaged adapters for Google PubSub and Bigquery.

So, from this perspective Apache Camel complements the GCP stack.  

With Kubernetes being the de-facto standard for the containerised deployments, such setup can be replicated with Azure, AWS, IBM, RedHat, etc. 

Upcoming Kubernetes Cluster Federation will also unlock the notion of a true Hybrid Integration Platform - the Apache Camel/Docker/Kubernetes package can be managed through a single control pane - regardless of how many clusters are operated or where they are located. 

But enough of the *"Why?"*, let's get on with the *"How?"*.

