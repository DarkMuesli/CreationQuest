package de.thkoeln.creationquest.utils

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class KotlinExtensionsKtTest {

    data class TestObject(var name: String, var value : Int)

    @Test
    fun testAddOneDistinct() {
        val list = mutableListOf<TestObject>()
        list.addDistinct(TestObject("Test1", 1))

        assertEquals(1, list.size)
        assertEquals(TestObject("Test1", 1), list[0])
    }

    @Test
    fun testAddTwoDifferentDistinct() {
        val list = mutableListOf<TestObject>()
        list.addDistinct(TestObject("Test1", 1))
        list.addDistinct(TestObject("Test2", 2))

        assertEquals(2, list.size)
    }

    @Test
    fun testAddTwoIdenticalDistinct() {
        val list = mutableListOf<TestObject>()
        val testObj = TestObject("Test1", 1)
        list.addDistinct(testObj)
        testObj.value = 5
        list.addDistinct(testObj)

        assertEquals(1, list.size)
        assertEquals(testObj, list[0])
    }
}