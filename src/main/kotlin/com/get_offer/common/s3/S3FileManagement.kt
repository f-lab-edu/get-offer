package com.get_offer.common.s3

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.ObjectMetadata
import com.get_offer.multipart.FileValidate
import java.util.*
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile

@Component
class S3FileManagement(
    @Value("\${aws.s3.bucket}")
    private val bucket: String,
    private val amazonS3: AmazonS3,
) {
    companion object {
        const val TYPE_IMAGE = "image"
    }

    fun uploadImages(multipartFiles: List<MultipartFile>): List<String> {
        return multipartFiles.map { uploadImage(it) }
    }

    fun uploadImage(multipartFile: MultipartFile): String {
        val originalFilename = multipartFile.originalFilename
            ?: throw IllegalStateException()
        FileValidate.checkImageFormat(originalFilename)
        val fileName = "${UUID.randomUUID()}-${originalFilename}"
        val objectMetadata = setFileDateOption(
            type = TYPE_IMAGE,
            file = getFileExtension(originalFilename),
            multipartFile = multipartFile
        )
        amazonS3.putObject(bucket, fileName, multipartFile.inputStream, objectMetadata)
        return fileName
    }

    fun getFile(fileName: String): String {
        return amazonS3.getUrl(bucket, fileName).toString()
    }

    fun delete(fileNames: List<String>) {
        fileNames.forEach { delete(it) }
    }

    fun delete(fileName: String) {
        amazonS3.deleteObject(bucket, fileName)
    }

    private fun getFileExtension(fileName: String): String {
        val extensionIndex = fileName.lastIndexOf('.')
        return fileName.substring(extensionIndex + 1)
    }

    private fun setFileDateOption(
        type: String,
        file: String,
        multipartFile: MultipartFile
    ): ObjectMetadata {
        val objectMetadata = ObjectMetadata()
        objectMetadata.contentType = "/${type}/${getFileExtension(file)}"
        objectMetadata.contentLength = multipartFile.inputStream.available().toLong()
        return objectMetadata
    }
}