package com.location.algorithm.design.iterator

interface Aggregate<out T> {
  fun iterator(): Iterator<T>
}