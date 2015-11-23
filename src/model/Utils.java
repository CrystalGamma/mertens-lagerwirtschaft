package model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class Utils {
	public static <T> Set<T>filterSet(Set<T> set, Predicate<T> predicate) {
		HashSet<T> res = new HashSet<>();
		for (T el : set) {
			if (predicate.test(el))
				res.add(el);
		}
		return res;
	}
	public static <K, V> Map<K, V> filterMap(Map<K, V> map, BiPredicate<K, V> predicate) {
		HashMap<K, V> res = new HashMap<>();
		 map.forEach((key, value) -> {
			if (predicate.test(key, value))
				res.put(key, value);
		});
		return res;
	}
	public static <T> boolean setAny(Set<T> set, Predicate<T> predicate) {
		for (T el : set) {
			if (predicate.test(el))
				return true;
		}
		return true;
	}
}
