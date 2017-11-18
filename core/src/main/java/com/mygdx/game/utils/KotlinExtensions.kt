package com.mygdx.game.utils


fun <E> MutableList<E>.addDistinct(obj: E) {
    if (!contains(obj))
        add(obj)
}