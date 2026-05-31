package pokefenn.totemic.entity.animal;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityFlying;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateFlying;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import pokefenn.totemic.init.ModSounds;
import pokefenn.totemic.lib.Resources;

public class EntityBaldEagle extends EntityAnimal implements EntityFlying
{
    public float flap;
    public float flapSpeed;
    public float oFlapSpeed;
    public float oFlap;
    public float flapping = 1.0F;

    public EntityBaldEagle(World world)
    {
        super(world);
        setSize(0.6F, 1.0F);
        moveHelper = new EntityFlyHelper(this);
    }

    @Override
    protected void initEntityAI()
    {
        tasks.addTask(0, new EntityAIPanic(this, 1.25D));
        tasks.addTask(0, new EntityAISwimming(this));
        tasks.addTask(2, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        tasks.addTask(3, new EntityAIWanderAvoidWaterFlying(this, 1.0D));
    }

    @Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        getAttributeMap().registerAttribute(SharedMonsterAttributes.FLYING_SPEED);
        getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20.0);
        getEntityAttribute(SharedMonsterAttributes.FLYING_SPEED).setBaseValue(0.4);
        getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.2);
        getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4.0);
    }

    @Override
    protected PathNavigate createNavigator(World worldIn)
    {
        PathNavigateFlying navigator = new PathNavigateFlying(this, worldIn);
        navigator.setCanOpenDoors(false);
        navigator.setCanFloat(true);
        navigator.setCanEnterDoors(true);
        return navigator;
    }

    @Override
    public float getEyeHeight()
    {
        return height * 0.6F;
    }

    @Override
    public void onLivingUpdate()
    {
        super.onLivingUpdate();
        calculateFlapping();
    }

    private void calculateFlapping()
    {
        this.oFlap = this.flap;
        this.oFlapSpeed = this.flapSpeed;
        this.flapSpeed = (float)(this.flapSpeed + (this.onGround ? -1 : 4) * 0.3D);
        this.flapSpeed = MathHelper.clamp(this.flapSpeed, 0.0F, 1.0F);

        if (!this.onGround && this.flapping < 1.0F)
        {
            this.flapping = 1.0F;
        }

        this.flapping = (float)(this.flapping * 0.9D);

        if (!this.onGround && this.motionY < 0.0D)
        {
            this.motionY *= 0.6D;
        }

        this.flap += this.flapping * 2.0F;
    }

    @Override
    public boolean isBreedingItem(ItemStack stack)
    {
        return false;
    }

    @Override
    public void fall(float distance, float damageMultiplier)
    { }

    @Override
    protected void updateFallState(double y, boolean onGroundIn, IBlockState state, BlockPos pos)
    { }

    @Override
    public boolean canMateWith(EntityAnimal otherAnimal)
    {
        return false;
    }

    @Override
    @Nullable
    public EntityAgeable createChild(EntityAgeable ageable)
    {
        return null;
    }

    @Override
    @Nullable
    protected SoundEvent getAmbientSound()
    {
        return ModSounds.baldEagleAmbient;
    }

    @Override
    @Nullable
    protected SoundEvent getHurtSound(DamageSource damageSource)
    {
        return ModSounds.baldEagleHurt;
    }

    @Override
    @Nullable
    protected SoundEvent getDeathSound()
    {
        return ModSounds.baldEagleDeath;
    }

    @Override
    protected void playStepSound(BlockPos pos, Block block)
    {
        playSound(SoundEvents.ENTITY_PARROT_STEP, 0.15F, 1.0F);
    }

    @Override
    protected float playFlySound(float distance)
    {
        playSound(SoundEvents.ENTITY_PARROT_FLY, 0.15F, 1.0F);
        return distance + this.flapSpeed / 2.0F;
    }

    @Override
    protected boolean makeFlySound()
    {
        return true;
    }

    @Override
    public SoundCategory getSoundCategory()
    {
        return SoundCategory.NEUTRAL;
    }


    @Override
    @Nullable
    protected ResourceLocation getLootTable()
    {
        return Resources.LOOT_BALD_EAGLE;
    }

    public boolean isFlying()
    {
        return !this.onGround;
    }
}
