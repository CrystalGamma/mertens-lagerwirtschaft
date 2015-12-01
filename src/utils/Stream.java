package utils;

import java.util.Observable;

public class Stream extends Observable{
	public void push(Object o) {
		setChanged();
		notifyObservers(o);
	}
}
