package eu.okatrych.domain.util

interface IMapper<in T, out R> {
    fun map(value: T): R
}