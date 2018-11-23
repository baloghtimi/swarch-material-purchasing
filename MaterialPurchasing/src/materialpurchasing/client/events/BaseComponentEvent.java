package materialpurchasing.client.events;

public abstract interface BaseComponentEvent {
	public abstract void baseComponentAdded(Boolean result);

	public abstract void baseComponentModified(Boolean result);
	
	public abstract void baseComponentRemoved(Boolean result);
}
