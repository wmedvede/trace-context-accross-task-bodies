package org.acme;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static io.restassured.RestAssured.given;

/**
 * Parameterized test that creates multiple workflow instances by POSTing
 * to the Quarkus Flow exec endpoint.
 *
 * Edit INSTANCE_COUNT and WORKFLOW_URL to target a different workflow or
 * change the number of instances created per run.
 *
 * The test runs against a live server (no @QuarkusTest bootstrap needed),
 * so start the application before executing this test:
 *   ./mvnw quarkus:dev
 */
class WorkflowInstanceCreationTest {

    /** Number of workflow instances to create. */
    private static final int INSTANCE_COUNT = 50;

    /** Base URL of the running Quarkus application. */
    private static final String BASE_URL = "http://localhost:8080";

    /**
     * Path + query for the target workflow.
     * Format: /q/flow/exec/{namespace}/{workflow-name}/{version}?wait=true
     */
    private static final String WORKFLOW_URL =
            "/q/flow/exec/otel/otel-do-with-do-task/1.0.0?wait=false";

    /** Bearer token configured in application.properties. */
    private static final String BEARER_TOKEN = "demo-secret-change-me";

    /**
     * Produces one argument per desired instance, carrying the target URL.
     * The stream size equals INSTANCE_COUNT, so the test body runs that many times.
     */
    static Stream<String> workflowUrls() {
        return Stream.generate(() -> WORKFLOW_URL).limit(INSTANCE_COUNT);
    }

    /**
     * For each element produced by {@link #workflowUrls()}, sends a POST
     * request to {@code workflowUrl} and asserts a 200 OK response.
     *
     * @param workflowUrl the endpoint path + query to POST to
     */
    @ParameterizedTest(name = "instance {index} -> {0}")
    @MethodSource("workflowUrls")
    void shouldCreateWorkflowInstance(String workflowUrl) {
        given()
                .baseUri(BASE_URL)
                .header("Authorization", "Bearer " + BEARER_TOKEN)
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body("{}")
            .when()
                .post(workflowUrl)
            .then()
                .statusCode(202);
    }
}
