package com.tt.driver.data.models

abstract class RemoteResult<out T>
class Success<T>(val data: T) : RemoteResult<T>()
class Failure<T>(val error: String) : RemoteResult<T>()
class Loading<T> : RemoteResult<T>()