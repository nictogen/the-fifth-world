package com.nic.tfw.items;

import com.nic.tfw.TheFifthWorld;
import com.nic.tfw.potion.PotionExplode;
import com.nic.tfw.superpower.Gene;
import lucraft.mods.lucraftcore.karma.KarmaHandler;
import lucraft.mods.lucraftcore.karma.KarmaStat;
import lucraft.mods.lucraftcore.superpowers.SuperpowerHandler;
import lucraft.mods.lucraftcore.superpowers.SuperpowerPlayerHandler;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.lang.reflect.Field;
import java.util.Random;
import java.util.UUID;

/**
 * Created by Nictogen on 1/14/19.
 */
@Mod.EventBusSubscriber
public class ItemInjectionGun extends Item
{
	public ItemInjectionGun()
	{
		setRegistryName(TheFifthWorld.MODID, "injection_gun");
		setTranslationKey("injection_gun");
		addPropertyOverride(new ResourceLocation("full"),
				(stack, worldIn, entityIn) -> stack.hasTagCompound() && stack.getTagCompound().hasKey("vial_tag") ?
						stack.getTagCompound().getCompoundTag("vial_tag").getInteger("full") :
						-1);
		setMaxStackSize(1);
	}

	@Override public boolean onEntitySwing(EntityLivingBase entityLiving, ItemStack stack)
	{
		if (entityLiving.isSneaking() && entityLiving instanceof EntityPlayerMP)
		{
			if(!stack.hasTagCompound() || !stack.getTagCompound().hasKey("vial_tag")) return super.onEntitySwing(entityLiving, stack);
			int full = stack.getTagCompound().getCompoundTag("vial_tag").getInteger("full");
			if (full == 0)
			{
				try
				{
					Gene.getRandomGenes(entityLiving, stack);
				}
				catch (IllegalAccessException e)
				{
					e.printStackTrace();
				}
				return true;
			}
			else if(full == 3 && stack.getTagCompound().getCompoundTag("vial_tag").getUniqueId("donor_uuid").toString().equals(entityLiving.getUniqueID().toString())){
				giveSuperpower(stack, (EntityPlayer) entityLiving);
			}
			return true;
		}
		return super.onEntitySwing(entityLiving, stack);
	}

	@Override public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker)
	{
		if(attacker.world.isRemote || !stack.hasTagCompound() || !stack.getTagCompound().hasKey("vial_tag")) return super.hitEntity(stack, target, attacker);
		int full = stack.getTagCompound().getCompoundTag("vial_tag").getInteger("full");
		if (full == 0)
		{
			try
			{
				Gene.getRandomGenes(target, stack);
			}
			catch (IllegalAccessException e)
			{
				e.printStackTrace();
			}
		}
		else if(full == 3){
			Boolean b = stack.getTagCompound().getCompoundTag("vial_tag").getUniqueId("donor_uuid").toString().equals(target.getUniqueID().toString());
			if(!b) return super.hitEntity(stack, target, attacker);
			if(target instanceof EntityPlayer)
			{
				giveSuperpower(stack, (EntityPlayer) target);
			} else if(target instanceof EntityVillager){
				ItemStack s = createGeneDataBook(stack, attacker);
				MerchantRecipeList recipes = new MerchantRecipeList();
				recipes.add(new MerchantRecipe(new ItemStack(Items.PAPER), s));
				Field f = EntityVillager.class.getDeclaredFields()[7];
				f.setAccessible(true);
				try
				{
					f.set(target, recipes);
				}
				catch (IllegalAccessException e)
				{
					e.printStackTrace();
				}
				target.addPotionEffect(new PotionEffect(PotionExplode.POTION_EXPLODE, 5*20));
				if(attacker instanceof EntityPlayer){
					KarmaHandler.increaseKarmaStat((EntityPlayer) attacker, TEST_SUBJECTS_EXPLODED);
				}
				removeContentsOfVial(stack);
			}
		}
		return super.hitEntity(stack, target, attacker);
	}

	private int addLine(StringBuilder stringBuilder, NBTTagList pages, String content, int index){
		//TODO handle adding more than 20 characters
		if(index == 14){
			pages.appendTag(new NBTTagString(stringBuilder.toString()));
			stringBuilder.delete(0, stringBuilder.length());
			stringBuilder.append(content);
			stringBuilder.append("\n");
			return 1;
		} else
		stringBuilder.append(content);
		stringBuilder.append("\n");
		return index + 1;
	}

	private void giveSuperpower(ItemStack stack, EntityPlayer player){
		if(SuperpowerHandler.getSuperpower(player) == null)
		{
			NBTTagCompound compound = new NBTTagCompound();
			compound.setTag("gene_data", stack.getTagCompound().getTag("vial_tag"));
			SuperpowerHandler.setSuperpower(player, TheFifthWorld.Superpowers.genetically_modified);
			SuperpowerPlayerHandler handler = SuperpowerHandler.getSuperpowerPlayerHandler(player);
			handler.setStyleNBTTag(compound);
			removeContentsOfVial(stack);
		}
	}

	private void removeContentsOfVial(ItemStack stack){
		NBTTagCompound compound = new NBTTagCompound();
		compound.setInteger("full", 0);
		stack.getTagCompound().setTag("vial_tag", compound);
	}

	private ItemStack createGeneDataBook(ItemStack stack, EntityLivingBase attacker){
		ItemStack s = new ItemStack(Items.WRITTEN_BOOK);
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setString("title", "Gene Data");
		nbt.setString("author", attacker.getDisplayName().getFormattedText());
		NBTTagList pages = new NBTTagList();

		int index = 0;

		StringBuilder s2 = new StringBuilder("");

		index = addLine(s2, pages, "Genetic Abilities:", index);
		for (int l = 1; stack.getTagCompound().getCompoundTag("vial_tag").hasKey("gene_" + l); l++)
		{
			NBTTagCompound gene = stack.getTagCompound().getCompoundTag("vial_tag").getCompoundTag("gene_" + l);
			Gene g = Gene.GENE_REGISTRY.getValue(new ResourceLocation(gene.getString("registry_name")));
			if(g != null)
			{

				index = addLine(s2, pages, "", index);
				index = addLine(s2, pages, g.displayName, index);
				index = addLine(s2, pages, Math.round(gene.getFloat("quality")*10000f) / 100.0 + "% Quality", index);
				index = addLine(s2, pages, gene.getInteger("stacks") + " Iterations", index);
			}
		}
		index = addLine(s2, pages, "", index);
		index = addLine(s2, pages, "Genetic Defects", index);
		index = addLine(s2, pages, "", index);
		for (int l = 1; stack.getTagCompound().getCompoundTag("vial_tag").hasKey("defect_" + l); l++)
		{
			NBTTagCompound defect = stack.getTagCompound().getCompoundTag("vial_tag").getCompoundTag("defect_" + l);
			Gene g = Gene.GENE_REGISTRY.getValue(new ResourceLocation(defect.getString("registry_name")));
			if(g != null)
			{

				index = addLine(s2, pages, g.displayName, index);
			}
		}

		pages.appendTag(new NBTTagString(s2.toString()));

		for (int i = 0; i < pages.tagCount(); ++i)
		{
			String s1 = pages.getStringTagAt(i);
			ITextComponent itextcomponent = new TextComponentString(s1);
			s1 = ITextComponent.Serializer.componentToJson(itextcomponent);
			pages.set(i, new NBTTagString(s1));
		}
		nbt.setTag("pages", pages);
		s.setTagCompound(nbt);
		return s;
	}

	@Override public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
	{
		if(playerIn.world.isRemote) return super.onItemRightClick(worldIn, playerIn, handIn);
		EnumHand off = handIn == EnumHand.MAIN_HAND ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND;
		ItemStack stack = playerIn.getHeldItem(handIn);
		ItemStack stackOff = playerIn.getHeldItem(off);
		if (stackOff.getItem() instanceof ItemVial && (!stack.hasTagCompound() || !stack.getTagCompound().hasKey("vial_tag")))
		{
			if (!stack.hasTagCompound())
				stack.setTagCompound(new NBTTagCompound());
			if (!stackOff.hasTagCompound() || stackOff.getTagCompound().getInteger("full") == 0)
			{
				NBTTagCompound nbt = new NBTTagCompound();
				nbt.setInteger("full", 0);
				stackOff.setTagCompound(nbt);
			}
			else if (stackOff.hasTagCompound() && stackOff.getTagCompound().getInteger("full") == 2)
				return super.onItemRightClick(worldIn, playerIn, handIn);
			stack.getTagCompound().setTag("vial_tag", stackOff.getTagCompound());
			playerIn.setHeldItem(off, ItemStack.EMPTY);
			return new ActionResult<>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
		}
		else if (stackOff.isEmpty() && stack.hasTagCompound() && stack.getTagCompound().hasKey("vial_tag"))
		{
			ItemStack vial = new ItemStack(TheFifthWorld.Items.vial);
			vial.setTagCompound(stack.getTagCompound().getCompoundTag("vial_tag"));
			playerIn.setHeldItem(off, vial);
			stack.getTagCompound().removeTag("vial_tag");
			return new ActionResult<>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
		}
		return super.onItemRightClick(worldIn, playerIn, handIn);
	}

	public static class InjectionGeneColor implements IItemColor
	{
		@Override public int colorMultiplier(ItemStack stack, int tintIndex)
		{
			Color c = new Color(1.0f, 1.0f, 1.0f, 1.0f);

			if (tintIndex == 1 && stack.hasTagCompound())
			{
				UUID donorUUID = stack.getTagCompound().getCompoundTag("vial_tag").getUniqueId("donor_uuid");
				int mod = stack.getTagCompound().getCompoundTag("vial_tag").getCompoundTag("gene_1").getString("registry_name").length();
				Random r = new Random(donorUUID.getMostSignificantBits() + donorUUID.getLeastSignificantBits() + mod);
				c = new Color(r.nextFloat(), r.nextFloat(), r.nextFloat());
			}
			return c.getRGB();
		}
	}

	public static KarmaStat TEST_SUBJECTS_EXPLODED;

	@SubscribeEvent
	public static void onRegisterKarmaStats(RegistryEvent.Register<KarmaStat> e) {
		e.getRegistry().register(TEST_SUBJECTS_EXPLODED = new KarmaStat("test_subjects_exploded", -25).setRegistryName(TheFifthWorld.MODID, "test_subjects_exploded"));
	}
}
