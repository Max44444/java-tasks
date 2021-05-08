package com.binary_studio.academy_coin;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public final class AcademyCoin {

	private AcademyCoin() {
	}

	public static int maxProfit(Stream<Integer> prices) {
		AtomicInteger purchasePrice = new AtomicInteger(Integer.MAX_VALUE);
		AtomicInteger sellingPrice = new AtomicInteger(-1);

		return Stream.concat(prices, Stream.of(-1)).reduce(0, (sum, price) -> {
			if (sellingPrice.get() == -1 && purchasePrice.get() > price) {
				purchasePrice.set(price);
			}
			else if (sellingPrice.get() < price) {
				sellingPrice.set(price);
			}
			else {
				sum += sellingPrice.getAndSet(-1) - purchasePrice.getAndSet(price);
			}
			return sum;
		});

	}

}
