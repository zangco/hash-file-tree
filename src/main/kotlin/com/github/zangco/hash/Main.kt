package com.github.zangco.hash

import java.io.File
import java.io.FileInputStream
import java.security.DigestInputStream
import java.security.MessageDigest

val algorithm = "SHA-1"

private fun hashString(bytes: ByteArray): String{
    return bytes.joinToString(transform = {String.format("%02x", it)}, separator = "-")
}

fun hashString(path: String) : String {
    var file = File(path)
    val hash = FileInputStream(file).use { stream ->
        hash(stream)
    }
    val result = hashString(hash)
    return result
}

fun hash(path: String) : ByteArray {
    var file = File(path)
    val hash = FileInputStream(file).use { stream ->
        hash(stream)
    }
    return hash
}

private fun hash(stream: FileInputStream): ByteArray {
    val md = MessageDigest.getInstance(algorithm)
    DigestInputStream(stream, md).use { input ->
        while (input.read() != -1) {
        }
    }

    val hash = md.digest()
    return hash
}

fun pathHash(path: String): ByteArray {
    val md = MessageDigest.getInstance(algorithm)
    val bytes = path.toByteArray()
    return md.digest(bytes)
}

// ToDo better data structure or override equals/ hashcode
private data class FileHash(val path: ByteArray, val content: ByteArray);

fun main(args: Array<String>) {
    println("Running")
    val md = MessageDigest.getInstance(algorithm)
    val treeWalk = File("/tmp").walk()
    treeWalk.filter{it.isFile}
            .sortedBy { it.absolutePath }
            .map{FileHash(pathHash(it.absolutePath), hash(it.absolutePath))}
            .forEach { md.update(it.path); md.update(it.content) }
    val digest = md.digest()
    println(hashString(digest))
}
