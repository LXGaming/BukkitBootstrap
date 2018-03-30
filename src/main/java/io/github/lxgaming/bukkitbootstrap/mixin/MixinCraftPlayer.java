/*
 * Copyright 2017 Alex Thomson
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.lxgaming.bukkitbootstrap.mixin;

import com.google.common.base.Preconditions;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.event.player.PlayerRegisterChannelEvent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Set;

@Mixin(value = CraftPlayer.class, remap = false)
public class MixinCraftPlayer extends CraftHumanEntity {
    
    @Shadow
    @Final
    private Set<String> channels;
    
    public MixinCraftPlayer(CraftServer server, EntityPlayer entity) {
        super(server, entity);
    }
    
    /**
     * @param channel
     * @author LX_Gaming
     * @reason Overwrite in order to change channel limit.
     */
    @Overwrite
    public void addChannel(String channel) {
        Preconditions.checkState(getChannels().size() < 1024, "Too many channels registered");
        if (getChannels().add(channel)) {
            Bukkit.getServer().getPluginManager().callEvent(new PlayerRegisterChannelEvent(Bukkit.getPlayer(getUniqueId()), channel));
        }
    }
    
    public Set<String> getChannels() {
        return channels;
    }
}