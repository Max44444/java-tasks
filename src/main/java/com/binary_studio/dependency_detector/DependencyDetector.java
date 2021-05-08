package com.binary_studio.dependency_detector;

import java.util.*;
import java.util.stream.Collectors;

public final class DependencyDetector {

	private DependencyDetector() {
	}

	public static boolean canBuild(DependencyList libraries) {
		var librariesCheckStatus = libraries.libraries.stream().collect(Collectors.toMap(a -> a, a -> 0));

		for (var lib : libraries.libraries) {
			if (isCyclic(lib, librariesCheckStatus, libraries.dependencies)) {
				return false;
			}
		}

		return true;
	}

	static boolean isCyclic(String library, Map<String, Integer> librariesCheckStatus, List<String[]> dependencies) {

		if (librariesCheckStatus.get(library) != 0) {
			return librariesCheckStatus.get(library) == 2;
		}

		librariesCheckStatus.put(library, 2);

		for (String[] el : dependencies) {
			if (el[0].equals(library)) {
				if (isCyclic(el[1], librariesCheckStatus, dependencies)) {
					return true;
				}
			}
		}

		librariesCheckStatus.put(library, 1);

		return false;

	}

}
