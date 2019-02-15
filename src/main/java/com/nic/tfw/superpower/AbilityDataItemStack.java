package com.nic.tfw.superpower;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import lucraft.mods.lucraftcore.superpowers.abilities.data.AbilityData;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.JsonUtils;

import java.util.Objects;

/**
 * Created by Nictogen on 2019-02-15.
 */
public class AbilityDataItemStack extends AbilityData<ItemStack>
{
	public AbilityDataItemStack(String key)
	{
		super(key);
	}

	@Override public ItemStack parseValue(JsonObject jsonObject, ItemStack defaultValue)
	{
		if (!JsonUtils.hasField(jsonObject, this.jsonKey))
			return defaultValue;
		JsonArray array = JsonUtils.getJsonArray(jsonObject, this.jsonKey);
		Item item = Item.getByNameOrId(array.get(0).getAsString());
		if(item == null)
			throw new JsonSyntaxException("Item " + array.get(0).getAsString() + " does not exist!");
		int amount = array.get(1).getAsInt();
		return new ItemStack(item, amount);
	}

	@Override public void writeToNBT(NBTTagCompound nbt, ItemStack value)
	{
		NBTTagCompound tag = new NBTTagCompound();
		tag.setString("item", value.getItem().getRegistryName().toString());
		tag.setInteger("amount", value.getCount());
		nbt.setTag(this.jsonKey, tag);
	}

	@Override public ItemStack readFromNBT(NBTTagCompound nbt, ItemStack defaultValue)
	{
		if (!nbt.hasKey(this.jsonKey))
			return defaultValue;
		return new ItemStack(Objects.requireNonNull(Item.getByNameOrId(nbt.getCompoundTag(this.jsonKey).getString("item"))), nbt.getCompoundTag(this.jsonKey).getInteger("amount"));
	}
}
