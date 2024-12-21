package com.get_offer.product.domain

import jakarta.persistence.LockModeType
import java.time.LocalDateTime
import java.util.*
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface ProductRepository : JpaRepository<Product, Long> {
    fun findAllByWriterIdOrderByEndDateDesc(writerId: Long): List<Product>

    fun findAllByStatusInOrderByEndDateDesc(statuses: List<ProductStatus>, pageRequest: PageRequest): Page<Product>

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from Product p where p.id = :id")
    fun findByIdWithLock(id: Long): Optional<Product>

    fun findByStatusAndEndDateLessThanEqual(status: ProductStatus, endDate: LocalDateTime): List<Product>

    fun findAllByTitleLike(title: String): List<Product>
}