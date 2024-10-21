package com.get_offer.common.exception

/**
 * custom exception 처리를 위해 남겨 놓았습니다.
 */
class NotFoundException(override val message: String) : RuntimeException(message)
class UnAuthorizationException : RuntimeException()

class UnsupportedFileExtensionException : RuntimeException()