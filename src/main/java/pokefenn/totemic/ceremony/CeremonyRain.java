package pokefenn.totemic.ceremony;

import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import pokefenn.totemic.api.TotemicEntityUtil;
import pokefenn.totemic.api.ceremony.Ceremony;
import pokefenn.totemic.api.ceremony.CeremonyEffectContext;
import pokefenn.totemic.api.music.MusicInstrument;

public class CeremonyRain extends Ceremony
{
    public final boolean doThunder;

    public CeremonyRain(boolean thunder, String name, int musicNeeded, int maxStartupTime, int backfireChance, MusicInstrument... instruments)
    {
        super(name, musicNeeded, maxStartupTime, backfireChance, instruments);
        this.doThunder = thunder;
    }

    @Override
    public void effect(World world, BlockPos pos, CeremonyEffectContext context)
    {
        if(!world.isRemote)
        {
            world.getWorldInfo().setRaining(true);
            world.getWorldInfo().setRainTime(0);

            if (doThunder)
            {
                world.getWorldInfo().setThundering(true);
                world.getWorldInfo().setThunderTime(0);

                TotemicEntityUtil.getPlayersInRange(world, pos, 20, 20).forEach(player ->
                {
                    world.addWeatherEffect(new EntityLightningBolt(world, player.posX, player.posY, player.posZ, true));
                });
            }
        }
    }
}
