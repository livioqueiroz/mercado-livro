package com.mercadolivro.service

import com.mercadolivro.events.PurchaseEvent
import com.mercadolivro.helper.buildPurchase
import com.mercadolivro.repository.PurchaseRepository
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.just
import io.mockk.runs
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.context.ApplicationEventPublisher

@ExtendWith(MockKExtension::class)
class PurchaseServiceTest {

    @MockK
    private lateinit var purchaseRepository: PurchaseRepository

    @MockK
    private lateinit var applicationEventPublisher: ApplicationEventPublisher

    @InjectMockKs
    private lateinit var purchaseService: PurchaseService

    val publishEventSlot = slot<PurchaseEvent>()


    @Test
    fun ` should create purchase and publish event `() {

        val purchase = buildPurchase()

        every { purchaseRepository.save(purchase) } returns purchase

        every { applicationEventPublisher.publishEvent(capture(publishEventSlot)) } just runs

        purchaseService.create(purchase)

        verify(exactly = 1) { purchaseRepository.save(purchase) }

        assertEquals(purchase, publishEventSlot.captured.purchaseModel)
    }

    @Test
    fun `should update Purchase`() {
        val purchase = buildPurchase()

        every { purchaseRepository.save(purchase) } returns purchase

        purchaseService.update(purchase)
        verify(exactly = 1) { purchaseRepository.save(purchase) }
    }

}