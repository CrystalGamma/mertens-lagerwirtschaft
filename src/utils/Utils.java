package utils;

import model.Model;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;

public class Utils {
	/** erzeugt ein eine Map, die nur die Elemente enthält, die das Prädikat (Parameter: Schlüssel, Wert) auswählt */
	public static <K, V> Map<K, V> filterMap(Map<K, V> map, BiPredicate<K, V> predicate) {
		HashMap<K, V> res = new HashMap<>();
		 map.forEach((key, value) -> {
			if (predicate.test(key, value))
				res.put(key, value);
		});
		return res;
	}
	/** erzeugt ein Array, das durch die Abbildung der Elemente des übergebenen Arrays gefüllt wird */
	public static <T, S> T[] arrayMap(Class<T> klass, S[] arr, Function<S, T> f) {
		T[] res = (T[])Array.newInstance(klass, arr.length);
		for (int i = 0; i < arr.length; i++) {
			res[i] = f.apply(arr[i]);
		}
		return res;
	}
	/** Stellt (rekursiv) alle Unterlager eines Oberlagers zusammen */
	public static Set<Model.LagerHalle> getLagerHallen(Model.OberLager oberLager) {
		Set<Model.LagerHalle> lagerHallen = new HashSet<>();

		for(Model.Lager lager : oberLager.getUnterLager()) {
			if(lager instanceof Model.LagerHalle)
				lagerHallen.add((Model.LagerHalle) lager);
			else if(lager instanceof Model.OberLager)
				lagerHallen.addAll(getLagerHallen((Model.OberLager) lager));
		}

		return lagerHallen;
	}
}
