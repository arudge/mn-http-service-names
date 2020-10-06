package mn.http.service.name

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import io.micronaut.http.client.exceptions.ReadTimeoutException
import io.micronaut.test.annotation.MicronautTest
import io.micronaut.test.support.TestPropertyProvider
import mn.http.service.name.client.PropertyAwareClient
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification
import javax.inject.Inject

@MicronautTest
class PropertyAwareClientSpec extends Specification implements TestPropertyProvider {

    @Inject
    PropertyAwareClient kebabClient

    @Shared @AutoCleanup("stop") WireMockServer wireMockServer

    def cleanup() {
        wireMockServer.resetAll()
    }

    void 'with a kebab this will use the property timeout and puke'() {
        given:
        def hello = "Hello"
        wireMockServer.stubFor(WireMock.get(WireMock.urlPathMatching("/"))
                .willReturn(WireMock.aResponse().withBody(hello).withFixedDelay(1000)))

        when:
        def response = kebabClient.getData()

        then:
        wireMockServer.verify(1, WireMock.getRequestedFor(WireMock.urlPathMatching("/")))
        thrown(ReadTimeoutException)
    }

    @Override
    Map<String, String> getProperties() {
        wireMockServer = new WireMockServer(new WireMockConfiguration().options().dynamicPort())
        wireMockServer.start()

        return [
                'micronaut.http.services.kebab-V1.url': "http://localhost:${wireMockServer.port()}/",
                'micronaut.http.services.kebab-V1.read-timeout': "500ms"
        ]
    }
}
