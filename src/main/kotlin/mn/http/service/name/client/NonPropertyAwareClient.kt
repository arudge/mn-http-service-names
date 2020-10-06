package mn.http.service.name.client

import io.micronaut.http.annotation.Get
import io.micronaut.http.client.annotation.Client

@Client(id = "nonKebabV1")
interface NonPropertyAwareClient {
    @Get
    fun getData(): String
}
