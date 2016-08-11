package org.flycraft.dropchanger;

import java.io.File;

import net.minecraftforge.common.MinecraftForge;

import org.flycraft.dropchanger.content.Checker;
import org.flycraft.dropchanger.content.DropChangerGuiHandler;
import org.flycraft.dropchanger.managers.DataManager;
import org.flycraft.dropchanger.managers.LocalManager;
import org.flycraft.dropchanger.managers.PacketManager;
import org.flycraft.dropchanger.managers.SqlManager;
import org.flycraft.dropchanger.thingsdoers.DropCatcher;
import org.flycraft.dropchanger.thingsdoers.Sender;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;

@Mod(modid=DCInfo.MOD_ID, name=DCInfo.MOD_NAME, version=DCInfo.MOD_VERSION)
@NetworkMod(clientSideRequired=false, serverSideRequired=false, channels={DCInfo.MOD_CHANNEL_TOMOD, DCInfo.MOD_CHANNEL_TOPLAYER, DCInfo.MOD_CHANNEL_SIMPLECOMMANDS}, packetHandler = PacketManager.class)
public class DropChanger {
	
	public static DataManager dataMng;
	public static LocalManager localMng;
	public static SqlManager sqlManager;
	public static DropChangerGuiHandler guiHandler;
	public static DropCatcher eventCatcher;
	
	public static File configDir;
	
    @Instance(value=DCInfo.MOD_ID)
    public static DropChanger instance;
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
    	Sender.send("Starting witch language manager...");
    	configDir = event.getSuggestedConfigurationFile();
    	dataMng = new DataManager(configDir);
    	dataMng.init();
    	localMng = new LocalManager();
    	Checker checker = new Checker();
    	NetworkRegistry.instance().registerGuiHandler(this, guiHandler = new DropChangerGuiHandler());
    	MinecraftForge.EVENT_BUS.register(eventCatcher = new DropCatcher());
    	
    	Sender.sendWML("launch.end");
    	
    }
    
    @EventHandler
    public void sStarting(FMLServerStartingEvent event) {
    	if(dataMng.isSqlCustomQuery()) {
    		sqlManager = new SqlManager(dataMng.getMySqlInfo());
    		sqlManager.start();
    	}
    }
    
    @EventHandler
    public void sStopping(FMLServerStoppingEvent event) {
    	if(sqlManager != null) {
    		sqlManager.kill();
    	}
    }
	
}
