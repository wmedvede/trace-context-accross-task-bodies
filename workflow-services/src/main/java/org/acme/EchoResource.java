package org.acme;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Scope;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.Operation;

import java.util.Map;


@Path("/external-services")
public class EchoResource {

    @Inject
    Tracer tracer;

    @Path("echo-one")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(operationId = "echoOne")
    public String echoOne(Map<String, Object> input) {
        String message = (String) input.get("message");
        Span span = tracer.spanBuilder("CreateEchoOne message")
                .setAttribute("external.service.name", "EchoOneResource")
                .startSpan();
        try (Scope scope = span.makeCurrent()) {
            return doWork("echo-one", message);
        } finally {
            span.end();
        }
    }

    @Path("echo-two")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(operationId = "echoTwo")
    public String echoTwo(Map<String, Object> input) {
        String message = (String) input.get("message");
        Span span = tracer.spanBuilder("CreateEchoTwo message")
                .setAttribute("external.service.name", "EchoTwoResource")
                .startSpan();
        try (Scope scope = span.makeCurrent()) {
            return doWork("echo-two", message);
        } finally {
            span.end();
        }
    }

    @Path("echo-three")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(operationId = "echoThree")
    public String echoThree(Map<String, Object> input) {
        String message = (String) input.get("message");
        Span span = tracer.spanBuilder("CreateEchoThree message")
                .setAttribute("external.service.name", "EchoThreeResource")
                .startSpan();
        try (Scope scope = span.makeCurrent()) {
            return doWork("echo-three", message);
        } finally {
            span.end();
        }
    }

    private String doWork(String service, String message) {
        return "{\"" + service + "\": \"" + message + " - " + java.time.ZonedDateTime.now() + "\"}";
    }
}
