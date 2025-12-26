package com.example.habitchecklist.utils

import org.junit.Assert.*
import org.junit.Test
import java.time.Instant
import java.time.ZoneId

class DateUtilsTest {

    @Test
    fun `isSameDay returns true for same day`() {
        val timestamp1 = Instant.parse("2024-01-15T10:00:00Z")
            .atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        val timestamp2 = Instant.parse("2024-01-15T20:00:00Z")
            .atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        
        assertTrue(DateUtils.isSameDay(timestamp1, timestamp2))
    }

    @Test
    fun `isSameDay returns false for different days`() {
        val timestamp1 = Instant.parse("2024-01-15T23:00:00Z")
            .atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        val timestamp2 = Instant.parse("2024-01-16T01:00:00Z")
            .atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        
        assertFalse(DateUtils.isSameDay(timestamp1, timestamp2))
    }

    @Test
    fun `daysBetween calculates correctly`() {
        val start = Instant.parse("2024-01-15T10:00:00Z")
            .atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        val end = Instant.parse("2024-01-17T10:00:00Z")
            .atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        
        assertEquals(2, DateUtils.daysBetween(start, end))
    }

    @Test
    fun `parseTime parses valid time correctly`() {
        assertEquals(480, DateUtils.parseTime("08:00")) // 8:00 AM = 8*60 = 480 minutes
        assertEquals(1320, DateUtils.parseTime("22:00")) // 10:00 PM = 22*60 = 1320 minutes
        assertEquals(0, DateUtils.parseTime("00:00"))
    }

    @Test
    fun `parseTime returns null for invalid time`() {
        assertNull(DateUtils.parseTime("invalid"))
        assertNull(DateUtils.parseTime("25:00"))
        assertNull(DateUtils.parseTime("12:60"))
    }

    @Test
    fun `formatTime formats correctly`() {
        assertEquals("08:00", DateUtils.formatTime(480))
        assertEquals("22:00", DateUtils.formatTime(1320))
        assertEquals("00:00", DateUtils.formatTime(0))
        assertEquals("23:59", DateUtils.formatTime(23 * 60 + 59))
    }

    @Test
    fun `formatTime handles edge cases`() {
        assertEquals("00:00", DateUtils.formatTime(-10))
        assertEquals("23:59", DateUtils.formatTime(24 * 60))
    }
}

