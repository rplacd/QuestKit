package net.minecraft.src;
import java.lang.Exception;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

import net.minecraft.client.Minecraft;

/**
* The Java-native implementation of IQuest.
* 
* This class does some sugaring re: tick() and notify() despatch where we call a corresponding tick_STATENAME (et cetera) method to dispatch on state.
* An implementation of logging is also given.
*/
public abstract class JavaQuestBase implements IQuest {
	protected QuestState state;
	
	private LinkedList<QuestState> log = new LinkedList<QuestState>();
	public List<QuestState> getLog() {
		return (LinkedList<QuestState>)log;
	}
	
	public QuestState getState() {
		return state;
	}
	
	//if we can find a corresponding tick_name for the state, call it
	//or else call tick_default.
	public void tick(Minecraft minecraft) throws Exception {
		try {
			QuestState former = getState();
			Method tick_method = this.getClass().getDeclaredMethod("tick_".concat(state.getState()), new Class[]{ Minecraft.class });
			tick_method.invoke(this, minecraft);
			QuestState latter = getState();
			if(!former.equals(latter)) {
				log.addFirst(former);
			}
		} catch(NoSuchMethodException e) {
			tick_default(minecraft);
		} catch(Exception e) {
			throw e;
		}
	}
	
	/**
	* A default no-op implementation of tick_default.
	*/	
	public void tick_default(Minecraft minecraft) {	}
	
	public void notify(Minecraft minecraft, Object msg) throws Exception {
		try {
			QuestState former = getState();
			Method notify_method = this.getClass().getDeclaredMethod("notify_".concat(state.getState()), new Class[] { Minecraft.class, Object.class });
			notify_method.invoke(this, minecraft, msg);
			QuestState latter = getState();
			if(!former.equals(latter)) {
				log.addFirst(former);
			}
		} catch(NoSuchMethodException e) {
			notify_default(minecraft, msg);
		} catch(Exception e) {
			throw e;
		}		
	}
	/**
	* A default no-op implementation of notify_default.
	*/	
	public void notify_default(Minecraft minecraft, Object msg) {}
	
	
	public void readFromNBT(NBTTagCompound source) {
		state = new QuestState("", "");
		state.readFromNBT(source.getCompoundTag("State"));
		
		NBTTagCompound logCompound = source.getCompoundTag("Log");
		int logSize = source.getInteger("LogIndices");
		if(logSize > 0) {
			for(Integer i = 1; i <= logSize; ++i) {
				QuestState curr = new QuestState("", "");
				curr.readFromNBT(logCompound.getCompoundTag(i.toString()));
				log.add(curr);
			}
		}
	}
	public void writeToNBT(NBTTagCompound target) {
		NBTTagCompound stateCompound = new NBTTagCompound();
		state.writeToNBT(stateCompound);
		target.setCompoundTag("State", stateCompound);
		
		target.setInteger("LogIndices", log.size());
		NBTTagCompound logCompound = new NBTTagCompound();
		Integer index = 1;
		for(QuestState state : log) {
			NBTTagCompound curr = new NBTTagCompound();
			state.writeToNBT(curr);
			logCompound.setCompoundTag(index.toString(), curr);
			index++;
		}
		target.setCompoundTag("Log", logCompound);
		
	}	
}
