package com.nic.tfw.superpower;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import lucraft.mods.lucraftcore.superpowers.abilities.data.AbilityData;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.JsonUtils;

import java.util.Objects;

/**
 * Created by Nictogen on 2019-02-15.
 */
public class AbilityDataBlock extends AbilityData<Block>
{
	public AbilityDataBlock(String key)
	{
		super(key);
	}

	@Override public Block parseValue(JsonObject jsonObject, Block defaultValue)
	{
		if (!JsonUtils.hasField(jsonObject, this.jsonKey))
			return defaultValue;
		Block block = Block.getBlockFromName(JsonUtils.getString(jsonObject, this.jsonKey));
		if(block == null)
			throw new JsonSyntaxException("Block " + JsonUtils.getString(jsonObject, this.jsonKey) + " does not exist!");
		return block;
	}

	@Override public void writeToNBT(NBTTagCompound nbt, Block value)
	{
		nbt.setString(this.jsonKey, Objects.requireNonNull(value.getRegistryName()).toString());
	}

	@Override public Block readFromNBT(NBTTagCompound nbt, Block defaultValue)
	{
		if (!nbt.hasKey(this.jsonKey))
			return defaultValue;
		return Block.getBlockFromName(nbt.getString(this.jsonKey));
	}
}
