package com.aidanvii.toolbox.adapterviews.recyclerview

import com.aidanvii.toolbox.DisposableItem
import com.aidanvii.toolbox.adapterviews.databinding.BindableAdapterItem
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.never
import com.nhaarman.mockito_kotlin.verify
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.util.concurrent.atomic.AtomicBoolean

internal class AdapterNotifierItemBoundObserverTest {

    val tested = AdapterNotifierItemBoundObserver<TestItem>()

    @Nested
    inner class `when lazyBindableItem is not initialised` {

        val testItem = TestItem()

        @Nested
        inner class `when onItemBound is called` {

            init {
                tested.onItemBound(testItem, mock())
            }

            // TODO only bind if lazy is initialised and is an AdapterNotifier
            @Test
            fun `adapter is bound and lazyBindableItem is initialised`() {
                verify(testItem.adapterNotifierItem).bindAdapter(any())
            }
        }

        @Nested
        inner class `when onItemUnBound is called` {

            init {
                tested.onItemUnBound(testItem, mock())
            }

            @Test
            fun `adapter is unbound and lazyBindableItem is initialised`() {
                verify(testItem.adapterNotifierItem, never()).unbindAdapter(any())
            }
        }
    }

    @Nested
    inner class `when lazyBindableItem is initialised` {

        val testItem = TestItem().apply {
            lazyBindableItem.value
        }

        @Nested
        inner class `when onItemBound is called` {

            init {
                tested.onItemBound(testItem, mock())
            }

            @Test
            fun `adapter is bound and lazyBindableItem is not initialised`() {
                verify(testItem.adapterNotifierItem).bindAdapter(any())
            }
        }

        @Nested
        inner class `when onItemUnBound is called` {

            init {
                tested.onItemUnBound(testItem, mock())
            }

            @Test
            fun `adapter is unbound and lazyBindableItem is not initialised`() {
                verify(testItem.adapterNotifierItem).unbindAdapter(any())
            }
        }
    }

    class TestItem : BindableAdapterItem, DisposableItem {

        override val _disposed = AtomicBoolean(false)

        override val bindingId: Int get() = 1
        override val layoutId: Int get() = 1

        override val lazyBindableItem = lazy(LazyThreadSafetyMode.NONE) { adapterNotifierItem }

        val adapterNotifierItem = mock<AdapterNotifier>()
    }
}