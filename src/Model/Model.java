package Model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;

public abstract class Model implements Cloneable, Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6582224451818652593L;
	protected PropertyChangeSupport observer = new PropertyChangeSupport(this);
	
	public abstract void initialize();
	
	public abstract void addPropertyChangeListener(PropertyChangeListener listener);
	public abstract void removePropertyChangeListener(PropertyChangeListener listener);
	
	@Override
	public abstract Model clone();
}