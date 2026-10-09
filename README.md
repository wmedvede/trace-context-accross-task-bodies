## To execute:

### Start jaeger

```
cd docker-compose
docker compose up
```

### Build and start the external-services app 

I'm using prod to make it more close to real envs.
```
java -jar target/quarkus-app/quarkus-run.jar
```

### Build and start the workflow services app

I'm using prod to make it more close to real envs.

```
java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005 -jar target/quarkus-app/quarkus-run.jar
```

### Create an instance of `otel-test-simple-call` workflow 

```
curl -X 'POST' \
'http://localhost:8080/q/flow/exec/otel/otel-test-simple-call/1.0.0?wait=true' \
-H 'Authorization: Bearer demo-secret-change-me' \
-H 'accept: application/json' \
-H 'Content-Type: application/json' \
-d '{}'
```

In Jaeger we can see that following trace was generated:

In this run, the spans produced by execution of the CallableTask "class io.serverlessworkflow.impl.executors.http.HttpExecutor"
are not child of corresponding currentSpans set by the OTelTaskSpanProxy.

![Execution 1](images/otel-test-simple-call-1.png)


### Create an instance of `otel-test-simple-call` workflow

```
curl -X 'POST' \
'http://localhost:8080/q/flow/exec/otel/otel-test-fork-with-call/1.0.0?wait=true' \
-H 'Authorization: Bearer demo-secret-change-me' \
-H 'accept: application/json' \
-H 'Content-Type: application/json' \
-d '{}'
```

#### First run
In a first run, the spans produced by execution of the CallableTask "class io.serverlessworkflow.impl.executors.http.HttpExecutor"
are child of currentSpans set by the OTelTaskSpanProxy. This is great, and what we would like to have.

![Execution 1](images/otel-test-fork-with-call-1.png)

#### Second run

In a second run, the spans produced by execution of the CallableTask "class io.serverlessworkflow.impl.executors.http.HttpExecutor"
are not child of currentSpans set by the OTelTaskSpanProxy.

![Execution 1](images/otel-test-fork-with-call-1.png)




