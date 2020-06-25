package eu.okatrych.domain.interactor

import kotlinx.coroutines.*

/**
 * Is used to declare that there is no parameters for UseCase
 */
typealias None = Unit

/**
 * Abstract class for a Use Case (Interactor in terms of Clean Architecture).
 * This abstraction represents an execution unit for different use cases (this means than any use
 * case in the application should implement this contract).
 *
 * By convention each [UseCase] implementation will execute its job in a background thread
 * ([executionDispatcher] in this case).
 */
abstract class UseCase<in Params, out Type>(
    private val executionDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    protected abstract suspend fun run(params: Params): Type

    suspend operator fun invoke(params: Params, job: Job): Type {
        return withContext(job + executionDispatcher) {
            run(params)
        }
    }
}