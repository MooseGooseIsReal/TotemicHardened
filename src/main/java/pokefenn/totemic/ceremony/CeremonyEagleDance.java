package pokefenn.totemic.ceremony;

import net.minecraft.entity.passive.EntityChicken;
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
import pokefenn.totemic.util.EntityUtil;

import java.util.List;
import java.util.stream.Collectors;

public class CeremonyEagleDance extends Ceremony
{
    private static final DamageSource SPECIAL = new DamageSource("totemic:eagleRitualDmg").setDamageBypassesArmor().setDamageIsAbsolute();

    public CeremonyEagleDance(String name, int musicNeeded, int maxStartupTime, int backfireChance, MusicInstrument... instruments)
    {
        super(name, musicNeeded, maxStartupTime, backfireChance, instruments);
    }

    @Override
    public void effect(World world, BlockPos pos, CeremonyEffectContext context)
    {
        if(world.isRemote)
            return;

        List<EntityChicken> chickens = TotemicEntityUtil.getEntitiesInRange(EntityChicken.class, world, pos, 8, 8).limit(3).collect(Collectors.toList());
        if (!chickens.isEmpty()) {
            chickens.forEach(host -> {
                EntityBaldEagle eagle = new EntityBaldEagle(world);
                EntityUtil.spawnEntity(world, host.posX, host.posY, host.posZ, eagle);
                host.setDead();
                ((WorldServer) world).spawnParticle(EnumParticleTypes.SMOKE_LARGE, host.posX, host.posY + 1.0, host.posZ, 24, 0.6D, 0.5D, 0.6D, 1.0D);
            });
        } else {
            TotemicEntityUtil.getPlayersInRange(world, pos, 6, 6).forEach(host -> {
                for(int i = 0; i < 3; i++) {
                    EntityBaldEagle eagle = new EntityBaldEagle(world);
                    EntityUtil.spawnEntity(world, host.posX, host.posY, host.posZ, eagle);
                    ((WorldServer) world).spawnParticle(EnumParticleTypes.REDSTONE, host.posX, host.posY + 1.0, host.posZ, 24, 0.6D, 0.5D, 0.6D, 1.0D);
                }
                host.attackEntityFrom(SPECIAL, Float.MAX_VALUE);
            });
        }
    }
}
