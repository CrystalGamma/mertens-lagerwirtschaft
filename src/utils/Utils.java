package utils;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;

public class Utils {
	static DateFormat idf = new SimpleDateFormat("yyyy-MM-dd");
	static DateFormat odf = new SimpleDateFormat("dd.MM.yyyy");

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
	public static <T, S> T[] arrayMap(Class<T> klass, S[] arr, Function<S, T> f) {
		T[] res = (T[])Array.newInstance(klass, arr.length);
		for (int i = 0; i < arr.length; i++) {
			res[i] = f.apply(arr[i]);
		}
		return res;
	}
	public static <K, S, T> Map<K, T> mapMap(Map<K, S> map, Function<S, T> f) {
		HashMap<K, T> res = new HashMap<>();
		map.forEach((key, value) -> {
			res.put(key, f.apply(value));
		});
		return Collections.unmodifiableMap(res);
	}
	public static String parseDate(String datum) {
		try {
			return odf.format(idf.parse(datum));
		} catch (ParseException e) {
			e.printStackTrace();
			return datum;
		}
	}
}
