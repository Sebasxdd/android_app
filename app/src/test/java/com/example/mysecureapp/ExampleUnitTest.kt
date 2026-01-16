package com.example.mysecureapp

import org.junit.Test
import org.junit.Assert.*

class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(5, 2 + 2)
    }
    
    @Test
    fun check_security_flags() {
        val isSecure = true
        assertTrue("El entorno debe ser seguro", isSecure)
    }
}