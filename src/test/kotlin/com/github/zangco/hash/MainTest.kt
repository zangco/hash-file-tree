package com.github.zangco.hash

import org.hamcrest.core.IsEqual
import org.hamcrest.core.IsNot
import org.junit.*
import java.io.File

class MainTest {
    val outFilename = "build/out.txt"
    val referenceFileName = "build/reference.txt"

    @Before
    fun setUp() {
        cleanUpFile(referenceFileName)
        cleanUpFile(outFilename)
    }

    @Test
    fun filesWithSameContentShouldHaveSameHash() {
        File(referenceFileName).printWriter().use {
            it.println("new line")
        }
        File(outFilename).printWriter().use {
            it.println("new line")
        }

        Assert.assertArrayEquals(hash(referenceFileName), hash(outFilename))
    }

    @Test
    fun filesWithDifferentContentShouldHaveDifferentHash() {
        File(referenceFileName).printWriter().use {
            it.println("new line")
        }
        File(outFilename).printWriter().use {
            it.println("new line 2")
        }

        Assert.assertThat(hash(referenceFileName), IsNot.not(IsEqual.equalTo(hash(outFilename))))
    }

    // ToDo / \ handling?
    @Test
    fun hashingSamePathNameShouldReturnSameHash(){
        Assert.assertArrayEquals(pathHash("abc/xyz.txt"), pathHash("abc/xyz.txt"))
    }

    @Test
    fun hashingDifferentPathNameShouldReturnDifferentHash(){
        Assert.assertThat(pathHash("abc/xxx.txt"), IsNot.not(IsEqual.equalTo(pathHash("abc/xyz.txt"))))
    }

    private fun cleanUpFile(name: String) {
        val outFile = File(name)
        if (outFile.exists()) {
            outFile.delete()
        }
    }
}
