package me.hippofatale.jackspdmmod.util;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class TickDelay {
    public Runnable fn;
    public int delay;

    public TickDelay(Runnable fn, int ticks) {
        this.fn = fn;
        this.delay = ticks;

        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onTIck(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            if (delay <= 0) {
                fn.run();
                MinecraftForge.EVENT_BUS.unregister(this);
            }
            delay--;
        }
    }

}
