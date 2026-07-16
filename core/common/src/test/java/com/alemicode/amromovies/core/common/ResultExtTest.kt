package com.alemicode.amromovies.core.common

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isInstanceOf
import assertk.assertions.isTrue
import org.junit.jupiter.api.Test

private enum class TestError : Error { BOOM }

class ResultExtTest {

    @Test
    fun `map transforms success data`() {
        val result: Result<Int, TestError> = Result.Success(2)

        val mapped = result.map { it * 21 }

        assertThat(mapped).isInstanceOf<Result.Success<Int>>()
        assertThat((mapped as Result.Success).data).isEqualTo(42)
    }

    @Test
    fun `map passes error through unchanged`() {
        val result: Result<Int, TestError> = Result.Error(TestError.BOOM)

        val mapped = result.map { it * 21 }

        assertThat(mapped).isInstanceOf<Result.Error<TestError>>()
        assertThat((mapped as Result.Error).error).isEqualTo(TestError.BOOM)
    }

    @Test
    fun `onSuccess runs action only for success and returns original result`() {
        val result: Result<Int, TestError> = Result.Success(1)
        var actionRan = false

        val returned = result.onSuccess { actionRan = true }

        assertThat(actionRan).isTrue()
        assertThat(returned).isEqualTo(result)
    }

    @Test
    fun `onSuccess does not run action for error`() {
        val result: Result<Int, TestError> = Result.Error(TestError.BOOM)
        var actionRan = false

        result.onSuccess { actionRan = true }

        assertThat(actionRan).isFalse()
    }

    @Test
    fun `onFailure runs action only for error and returns original result`() {
        val result: Result<Int, TestError> = Result.Error(TestError.BOOM)
        var capturedError: TestError? = null

        val returned = result.onFailure { capturedError = it }

        assertThat(capturedError).isEqualTo(TestError.BOOM)
        assertThat(returned).isEqualTo(result)
    }

    @Test
    fun `onFailure does not run action for success`() {
        val result: Result<Int, TestError> = Result.Success(1)
        var actionRan = false

        result.onFailure { actionRan = true }

        assertThat(actionRan).isFalse()
    }

    @Test
    fun `asEmptyResult discards success data`() {
        val result: Result<Int, TestError> = Result.Success(99)

        val empty = result.asEmptyResult()

        assertThat(empty).isInstanceOf<Result.Success<Unit>>()
        assertThat((empty as Result.Success).data).isEqualTo(Unit)
    }
}
