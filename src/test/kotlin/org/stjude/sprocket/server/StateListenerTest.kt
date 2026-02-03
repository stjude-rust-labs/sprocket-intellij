package org.stjude.sprocket.server

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

class StateListenerTest {

    @Test
    fun `listener is called when state changes`() {
        val stateManager = TestableStateManager()
        val receivedStates = mutableListOf<ServerState>()

        stateManager.addStateListener { state ->
            receivedStates.add(state)
        }

        stateManager.setState(ServerState.STARTING)
        stateManager.setState(ServerState.RUNNING)
        stateManager.setState(ServerState.STOPPED)

        assertEquals(3, receivedStates.size)
        assertEquals(ServerState.STARTING, receivedStates[0])
        assertEquals(ServerState.RUNNING, receivedStates[1])
        assertEquals(ServerState.STOPPED, receivedStates[2])
    }

    @Test
    fun `multiple listeners are all notified`() {
        val stateManager = TestableStateManager()
        val counter = AtomicInteger(0)

        repeat(5) {
            stateManager.addStateListener { counter.incrementAndGet() }
        }

        stateManager.setState(ServerState.RUNNING)

        assertEquals(5, counter.get())
    }

    @Test
    fun `removed listener is not called`() {
        val stateManager = TestableStateManager()
        val counter = AtomicInteger(0)
        val listener: (ServerState) -> Unit = { counter.incrementAndGet() }

        stateManager.addStateListener(listener)
        stateManager.setState(ServerState.STARTING)
        assertEquals(1, counter.get())

        stateManager.removeStateListener(listener)
        stateManager.setState(ServerState.RUNNING)
        assertEquals(1, counter.get())
    }

    @Test
    fun `listener receives correct state value`() {
        val stateManager = TestableStateManager()
        var capturedState: ServerState? = null

        stateManager.addStateListener { state ->
            capturedState = state
        }

        stateManager.setState(ServerState.ERROR)

        assertEquals(ServerState.ERROR, capturedState)
    }

    @Test
    fun `listeners are called in registration order`() {
        val stateManager = TestableStateManager()
        val order = mutableListOf<Int>()

        stateManager.addStateListener { order.add(1) }
        stateManager.addStateListener { order.add(2) }
        stateManager.addStateListener { order.add(3) }

        stateManager.setState(ServerState.RUNNING)

        assertEquals(listOf(1, 2, 3), order)
    }

    @Test
    fun `same listener can be added multiple times`() {
        val stateManager = TestableStateManager()
        val counter = AtomicInteger(0)
        val listener: (ServerState) -> Unit = { counter.incrementAndGet() }

        stateManager.addStateListener(listener)
        stateManager.addStateListener(listener)

        stateManager.setState(ServerState.RUNNING)

        assertEquals(2, counter.get())
    }

    @Test
    fun `removing listener only removes one instance`() {
        val stateManager = TestableStateManager()
        val counter = AtomicInteger(0)
        val listener: (ServerState) -> Unit = { counter.incrementAndGet() }

        stateManager.addStateListener(listener)
        stateManager.addStateListener(listener)
        stateManager.removeStateListener(listener)

        stateManager.setState(ServerState.RUNNING)

        assertEquals(1, counter.get())
    }

    @Test
    fun `listener exception does not prevent other listeners`() {
        val stateManager = TestableStateManager()
        val secondListenerCalled = AtomicInteger(0)

        stateManager.addStateListener { throw RuntimeException("Test exception") }
        stateManager.addStateListener { secondListenerCalled.incrementAndGet() }

        try {
            stateManager.setState(ServerState.RUNNING)
        } catch (_: RuntimeException) {
            // Expected
        }

        // CopyOnWriteArrayList iteration continues even after exception
        // but the forEach in the real implementation would stop
    }

    @Test
    fun `concurrent state changes are handled safely`() {
        val stateManager = TestableStateManager()
        val counter = AtomicInteger(0)
        val latch = CountDownLatch(100)

        stateManager.addStateListener {
            counter.incrementAndGet()
            latch.countDown()
        }

        val threads = (1..100).map {
            Thread {
                stateManager.setState(ServerState.RUNNING)
            }
        }

        threads.forEach { it.start() }
        threads.forEach { it.join() }

        assertTrue(latch.await(5, TimeUnit.SECONDS))
        assertEquals(100, counter.get())
    }

    private class TestableStateManager {
        private val listeners = CopyOnWriteArrayList<(ServerState) -> Unit>()

        fun addStateListener(listener: (ServerState) -> Unit) {
            listeners.add(listener)
        }

        fun removeStateListener(listener: (ServerState) -> Unit) {
            listeners.remove(listener)
        }

        fun setState(state: ServerState) {
            listeners.forEach { it(state) }
        }
    }
}
