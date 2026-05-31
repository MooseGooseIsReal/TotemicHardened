package pokefenn.totemic.entity.animal;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import pokefenn.totemic.lib.Resources;

public class EntityBuffalo extends EntityCow
{

    public EntityBuffalo(World world)
    {
        super(world);
        setSize(1.35F, 1.95F);
    }

    @Override
    protected void initEntityAI()
    {
        tasks.addTask(0, new EntityAISwimming(this));
        tasks.addTask(1, new EntityAIPanic(this, 2.0D));
        tasks.addTask(2, new EntityAIWander(this, 1.0D));
        tasks.addTask(3, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
        tasks.addTask(4, new EntityAILookIdle(this));
    }

    @Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(40);
        getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.15);
        getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(5.0);
    }

    @Override
    protected float getSoundVolume()
    {
        return 0.4F;
    }

    @Override
    protected float getSoundPitch()
    {
        return super.getSoundPitch() - 0.2F;
    }

    @Override
    protected ResourceLocation getLootTable()
    {
        return Resources.LOOT_BUFFALO;
    }

    @Override
    protected int getExperiencePoints(EntityPlayer player)
    {
        return 2 + world.rand.nextInt(4);
    }
}
