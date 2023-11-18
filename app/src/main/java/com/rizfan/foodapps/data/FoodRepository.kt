package com.rizfan.foodapps.data

import com.rizfan.foodapps.data.model.MakananData
import com.rizfan.foodapps.data.model.OrderMakanan
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class FoodRepository {

    private val orderMakanan = mutableListOf<OrderMakanan>()

    init {
        if (orderMakanan.isEmpty()) {
            MakananData.makanan.forEach {
                orderMakanan.add(OrderMakanan(it, 0))
            }
        }
    }

    fun getsMakanan(): Flow<List<OrderMakanan>> {
        return flowOf(orderMakanan)
    }

    fun getOrderMakananById(makananId: Int): OrderMakanan {
        return orderMakanan.first {
            it.makanan.id == makananId
        }
    }

    fun searchMakanan(query:String): Flow<List<OrderMakanan>> {
        return flowOf(orderMakanan.filter {
            it.makanan.nama.contains(query, ignoreCase = true)
        })
    }

    fun updateOrderMakanan(makananId: Int, newCountValue: Int): Flow<Boolean> {
        val index = orderMakanan.indexOfFirst { it.makanan.id == makananId }
        val result = if (index >= 0) {
            val orderReward = orderMakanan[index]
            orderMakanan[index] =
                orderReward.copy(makanan = orderReward.makanan, count = newCountValue)
            true
        } else {
            false
        }
        return flowOf(result)
    }

    fun getAddedOrderMakanan(): Flow<List<OrderMakanan>> {
        return getsMakanan()
            .map { orderMakanans ->
                orderMakanans.filter { orderMakanan ->
                    orderMakanan.count != 0
                }
            }
    }

}