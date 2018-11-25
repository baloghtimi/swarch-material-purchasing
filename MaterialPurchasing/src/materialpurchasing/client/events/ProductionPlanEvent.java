package materialpurchasing.client.events;

public interface ProductionPlanEvent {
	void productionPlanAddedEvent(Boolean result);

	void productionPlanModifiedEvent(Boolean result);

	void productionPlanRemovedEvent(Boolean result);
}
