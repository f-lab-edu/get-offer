package com.get_offer.multipart

import com.get_offer.common.exception.UnsupportedFileExtensionException

class FileValidate {
    companion object {
        private val IMAGE_EXTENSIONS: List<String> = listOf("jpg", "png", "jpeg")

        fun checkImageFormat(fileName: String) {
            val extensionIndex = fileName.lastIndexOf('.')
            if (extensionIndex == -1) {
                throw UnsupportedFileExtensionException()
            }
            val extension = fileName.substring(extensionIndex + 1)
            require(IMAGE_EXTENSIONS.contains(extension)) {
                throw UnsupportedFileExtensionException()
            }
        }
    }
}