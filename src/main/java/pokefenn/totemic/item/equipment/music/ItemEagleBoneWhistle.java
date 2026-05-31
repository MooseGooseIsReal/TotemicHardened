package pokefenn.totemic.item.equipment.music;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pokefenn.totemic.Totemic;
import pokefenn.totemic.api.TotemicAPI;
import pokefenn.totemic.api.music.ItemInstrument;
import pokefenn.totemic.api.music.MusicAPI;
import pokefenn.totemic.init.ModSounds;
import pokefenn.totemic.lib.Strings;

public class ItemEagleBoneWhistle extends ItemInstrument
{
    public ItemEagleBoneWhistle()
    {
        setSound(ModSounds.eagleBoneWhistle);
        setRegistryName(Strings.EAGLE_BONE_WHISTLE_NAME);
        setUnlocalizedName(Strings.RESOURCE_PREFIX + Strings.EAGLE_BONE_WHISTLE_NAME);
        setCreativeTab(Totemic.tabsTotem);
        setMaxDamage(70);
        setMaxStackSize(1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag)
    {
        tooltip.add(I18n.format(getUnlocalizedName() + ".tooltip"));
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
    {
        ItemStack stack = player.getHeldItem(hand);
        if(!world.isRemote)
            useInstrument(stack, player, 20);
        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }

    @Override
    protected void playMusic(ItemStack stack, Entity entity)
    {
        int bonusMusic = 0;
        if (entity.world.isThundering()) {
            bonusMusic = 1;
        } else if (entity.world.isRaining()) {
            bonusMusic = entity.world.rand.nextInt(2);
        }
        TotemicAPI.get().music().playMusic(entity.world, entity.posX, entity.posY, entity.posZ, entity, instrument, MusicAPI.DEFAULT_RANGE, instrument.getBaseOutput() + bonusMusic);
        spawnParticles((WorldServer) entity.world, entity.posX, entity.posY, entity.posZ, false);
        if (sound != null)
            entity.world.playSound(null, entity.posX, entity.posY, entity.posZ, sound, SoundCategory.PLAYERS, 1.0F, 1.0F);
    }

    @Override
    public EnumRarity getRarity(ItemStack stack)
    {
        return EnumRarity.UNCOMMON;
    }
}
