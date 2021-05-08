package com.binary_studio.fleet_commander.core.common;

public final class PositiveInteger implements Comparable<PositiveInteger> {

	private final Integer underlyingVal;

	public PositiveInteger(Integer val) {
		if (val < 0) {
			throw new IllegalArgumentException(String.format("Got negative value %d, expected positive integer", val));
		}
		this.underlyingVal = val;
	}

	public static PositiveInteger of(Integer val) {
		return new PositiveInteger(val);
	}

	public Integer value() {
		return this.underlyingVal;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || obj.getClass() != PositiveInteger.class) {
			return false;
		}
		return ((PositiveInteger) obj).underlyingVal == this.underlyingVal;
	}

	@Override
	public int hashCode() {
		return this.underlyingVal.hashCode();
	}

	@Override
	public int compareTo(PositiveInteger o) {
		return Integer.compare(this.underlyingVal, o.underlyingVal);
	}

	public static PositiveInteger sum(PositiveInteger a, PositiveInteger b) {
		return PositiveInteger.of(a.value() + b.value());
	}

	public static PositiveInteger sub(PositiveInteger a, PositiveInteger b) {
		if (a.compareTo(b) < 0) {
			throw new IllegalArgumentException();
		}

		return PositiveInteger.of(a.value() - b.value());
	}

	public static PositiveInteger max(PositiveInteger a, PositiveInteger b) {
		return a.underlyingVal >= b.underlyingVal ? a : b;
	}

	public static PositiveInteger min(PositiveInteger a, PositiveInteger b) {
		return a.underlyingVal <= b.underlyingVal ? a : b;
	}

}
