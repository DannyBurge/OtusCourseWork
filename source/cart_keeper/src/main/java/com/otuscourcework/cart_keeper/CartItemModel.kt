package com.otuscourcework.cart_keeper

data class CartItemModel(
    val groupId: Int,
    val id: Int,
    val name: String,
    val subName: String,
    val price: Int,
    var amount: Int
)
