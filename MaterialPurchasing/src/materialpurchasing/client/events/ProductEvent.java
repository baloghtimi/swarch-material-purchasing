package materialpurchasing.client.events;

public interface ProductEvent {
	void productAddedEvent(Boolean result);

	void productModifiedEvent(Boolean result);

	void productRemovedEvent(Boolean result);
}
