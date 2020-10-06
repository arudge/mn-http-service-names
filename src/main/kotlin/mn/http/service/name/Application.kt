package mn.http.service.name

import io.micronaut.runtime.Micronaut.*

fun main(args: Array<String>) {
	build()
	    .args(*args)
		.packages("mn.http.service.name.*")
		.start()
}

