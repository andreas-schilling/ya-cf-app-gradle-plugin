package io.pivotal.services.plugin.tasks;

import io.pivotal.services.plugin.CfProperties;
import org.cloudfoundry.operations.CloudFoundryOperations;
import org.cloudfoundry.operations.applications.RestageApplicationRequest;
import org.gradle.api.tasks.TaskAction;
import reactor.core.publisher.Mono;

import java.time.Duration;

/**
 * Responsible for cf restage.
 *
 * @author Biju Kunjummen
 */
public class CfRestageTask extends AbstractCfTask {

    @TaskAction
    public void restage() {
        CfProperties cfProperties = getCfProperties();
        LOGGER.info("About to call Restage task : {} ", cfProperties.toString());

        CloudFoundryOperations cfOperations = getCfOperations();

        Mono<Void> resp = cfOperations.applications().restage(RestageApplicationRequest.builder()
            .name(cfProperties.name())
            .stagingTimeout(Duration.ofMinutes(cfProperties.stagingTimeout()))
            .startupTimeout(Duration.ofMinutes(cfProperties.startupTimeout())).build()
        );

        resp.block(Duration.ofMillis(defaultWaitTimeout));
    }

    @Override
    public String getDescription() {
        return "Restage an Application";
    }
}
