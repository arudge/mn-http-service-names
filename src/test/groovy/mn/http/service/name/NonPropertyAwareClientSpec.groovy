package mn.http.service.name

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import io.micronaut.http.client.exceptions.ReadTimeoutException
import io.micronaut.test.annotation.MicronautTest
import io.micronaut.test.support.TestPropertyProvider
import mn.http.service.name.client.NonPropertyAwareClient
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification

import javax.inject.Inject

@MicronautTest
class NonPropertyAwareClientSpec extends Specification implements TestPropertyProvider {

    @Inject
    NonPropertyAwareClient kebabClient

    @Shared @AutoCleanup("stop") WireMockServer wireMockServer

    def cleanup() {
        wireMockServer.resetAll()
    }

    void 'without a kebab this will use the default timeout of 10s and no read timeout exception'() {
        given:
        def hello = "Hello"
        wireMockServer.stubFor(WireMock.get(WireMock.urlPathMatching("/"))
        .willReturn(WireMock.aResponse().withBody(hello).withFixedDelay(1000)))

        when:
        def response = kebabClient.getData()

        then:
        wireMockServer.verify(1, WireMock.getRequestedFor(WireMock.urlPathMatching("/")))
        response == hello

        and: "this won't happen"
        thrown(ReadTimeoutException)
    }

    @Override
    Map<String, String> getProperties() {
        wireMockServer = new WireMockServer(new WireMockConfiguration().options().dynamicPort())
        wireMockServer.start()

        return [
                'micronaut.http.services.nonKebabV1.url': "http://localhost:${wireMockServer.port()}/",
                'micronaut.http.services.nonKebabV1.read-timeout': "500ms",
        ]
    }
}
