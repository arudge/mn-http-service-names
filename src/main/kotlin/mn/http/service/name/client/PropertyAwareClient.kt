package mn.http.service.name.client

import io.micronaut.http.annotation.Get
import io.micronaut.http.client.annotation.Client

@Client(id = "kebab-V1")
interface PropertyAwareClient {
    @Get
    fun getData(): String
}
