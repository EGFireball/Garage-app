package com.idimi.garage.util

fun List<String>.concat() = this.joinToString("") { it }.takeWhile { it.isDigit() }