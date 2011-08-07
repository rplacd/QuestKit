package net.minecraft.src;

/**
 * An interface that codifies Mojang's informal de/serialization protocol to and from compound NBT tags.
 */
public interface INBTSerializable {
	/**
	 * Reset the object's state from an NBT compound.
	 * 
	 * @param source Source compound to restore from.
	 */
	public void readFromNBT(NBTTagCompound source);
	/**
	 * Write the object's state to an NBT compound.
	 * 
	 * @param target Target compound to write to.
	 */
	public void writeToNBT(NBTTagCompound target);
}
