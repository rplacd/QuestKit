package net.minecraft.src;

/**
* Represents the current state of a quest FSM.
* 
* An immutable ADT that provides properties for the class' actual state (equality only looks that this) and a longer description of the current state.
* Two states should be used to mark a terminal - Success and Failure. GUI elements will mark these up accordingly, and parent QuestCollections will ignore quests that have "finished".
*/
public class QuestState implements INBTSerializable {
	private String state = "Invalid"; //two terminal names - 
	private String description = "A quest undefined/millions ask why waste?/the jvm cries";
	
	/**
	 * Get the state's name.
	 */
	public String getState() {
		return state;
	}	
	/**
	 * Get the state's description
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * Create a new state with a specified state name and longer description.
	 *
	 * @param newState The name to use.
	 * @param newDescription The description to use.
	 */
	public QuestState(String newState, String newDescription) {
		state = newState;
		description = newDescription;
	}
	
	/**
	 * Check whether the QuestState represents a terminal state - whether Success or Failure.
	 */
	public boolean isTerminal() {
		return this.state.equalsIgnoreCase("Success") || this.state.equalsIgnoreCase("Failure");
	}
	/**
	 * Check whether the QuestState represents a terminal state representing success.
	 */
	public boolean isTerminalSuccess() {
		return this.state.equalsIgnoreCase("Success");
	}
	/**
	 * Check whether the QuestState represents a terminal state representing failure.
	 */
	public boolean isTerminalFailure() {
		return this.state.equalsIgnoreCase("Failure");
	}
	
	/**
	 * Perform an equality test on two states - this checks the state's name only.
	 *
	 * @param otherState The other QuestState we're performing an equality test on.
	 * @return Whether the two states are equal.
	 */
	public boolean equals(QuestState otherState) {
		return this.state.equalsIgnoreCase(otherState.getState());
	}
	
	public void readFromNBT(NBTTagCompound source) {
		state = source.getString("State");
		description = source.getString("Description");
	}
	public void writeToNBT(NBTTagCompound dest) {
		dest.setString("State", state);
		dest.setString("Description", description);
	}
}
