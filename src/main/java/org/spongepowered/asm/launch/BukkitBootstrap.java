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

package org.spongepowered.asm.launch;

import java.io.File;
import java.util.List;

import net.minecraft.launchwrapper.ITweaker;
import net.minecraft.launchwrapper.LaunchClassLoader;

/**
 * Modified version of {@link org.spongepowered.asm.launch.MixinTweaker MixinTweaker} in order to launch {@link org.bukkit.craftbukkit.Main Main}.
 */
public class BukkitBootstrap implements ITweaker {
	
	private String[] launchArguments;
	
	public BukkitBootstrap() {
		setLaunchArguments(new String[]{});
		MixinBootstrap.start();
	}
	
	@Override
	public void acceptOptions(List<String> args, File gameDir, File assetsDir, String profile) {
		if (args != null && !args.isEmpty()) {
			setLaunchArguments(args.toArray(new String[args.size()]));
		}
		
		System.setProperty("java.net.preferIPv4Stack", "true");
		System.setProperty("IReallyKnowWhatIAmDoingISwear", "true"); // Don't check if build is outdated.
		MixinBootstrap.doInit(args);
	}
	
	@Override
	public void injectIntoClassLoader(LaunchClassLoader classLoader) {
		classLoader.addClassLoaderExclusion("com.mojang.util.QueueLogAppender");
		classLoader.addClassLoaderExclusion("jline.");
		classLoader.addClassLoaderExclusion("org.fusesource.");
		MixinBootstrap.inject();
	}
	
	@Override
	public String getLaunchTarget() {
		return "org.bukkit.craftbukkit.Main";
	}
	
	@Override
	public String[] getLaunchArguments() {
		return launchArguments;
	}
	
	private void setLaunchArguments(String[] launchArguments) {
		this.launchArguments = launchArguments;
	}
}