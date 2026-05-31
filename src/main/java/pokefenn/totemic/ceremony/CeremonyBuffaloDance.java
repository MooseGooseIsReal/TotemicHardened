package pokefenn.totemic.ceremony;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import pokefenn.totemic.api.TotemicEntityUtil;
import pokefenn.totemic.api.ceremony.Ceremony;
import pokefenn.totemic.api.ceremony.CeremonyEffectContext;
import pokefenn.totemic.api.music.MusicInstrument;
import pokefenn.totemic.entity.animal.EntityBaldEagle;
import pokefenn.totemic.entity.animal.EntityBuffalo;
import pokefenn.totemic.util.EntityUtil;

public class CeremonyBuffaloDance extends Ceremony
{
    private static final DamageSource SPECIAL = new DamageSource("totemic:buffaloRitualDmg").setDamageBypassesArmor().setDamageIsAbsolute();

    public CeremonyBuffaloDance(String name, int musicNeeded, int maxStartupTime, int backfireChance, MusicInstrument... instruments)
    {
        super(name, musicNeeded, maxStartupTime, backfireChance, instruments);
    }

    @Override
    public void effect(World world, BlockPos pos, CeremonyEffectContext context) {
        if (world.isRemote)
            return;

        List<EntityCow> cows = TotemicEntityUtil.getEntitiesInRange(EntityCow.class, world, pos, 8, 8).limit(3).collect(Collectors.toList());
        if (!cows.isEmpty()) {
            cows.forEach(host -> {
                EntityBuffalo buffalo = new EntityBuffalo(world);
                EntityUtil.spawnEntity(world, host.posX, host.posY, host.posZ, buffalo);
                ((WorldServer) world).spawnParticle(EnumParticleTypes.SMOKE_LARGE, host.posX, host.posY + 1.0, host.posZ, 24, 0.6D, 0.5D, 0.6D, 1.0D);
                host.setDead();
            });
        } else {
            TotemicEntityUtil.getPlayersInRange(world, pos, 6, 6).forEach(host -> {
                for (int i = 0; i < 3; i++) {
                    EntityBuffalo buffalo = new EntityBuffalo(world);
                    EntityUtil.spawnEntity(world, host.posX, host.posY, host.posZ, buffalo);
                    ((WorldServer) world).spawnParticle(EnumParticleTypes.REDSTONE, host.posX, host.posY + 1.0, host.posZ, 24, 0.6D, 0.5D, 0.6D, 1.0D);
                }
                host.attackEntityFrom(SPECIAL, Float.MAX_VALUE);
            });
        }
    }
}
