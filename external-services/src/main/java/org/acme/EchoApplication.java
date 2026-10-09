package org.acme;

import jakarta.ws.rs.core.Application;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.servers.Server;


@OpenAPIDefinition(
        info = @Info(title = "EchoService", description = "Echo Service Endpoint", version = "1.2.3-SNAPSHOT"),
        servers = {@Server(url = "http://localhost:8585")})
public class EchoApplication extends Application {
}
