package tw.ch1ck3n;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ModInitializer;

import tw.ch1ck3n.config.GTPConfig;
import tw.ch1ck3n.camera.GenshinCamera;

public class GenshinThirdPerson implements ModInitializer {

	public static GenshinThirdPerson instance;
	public static final String MOD_ID = "genshinthirdperson";
	private GenshinCamera camera;
	private GTPConfig config;

	@Override
	public void onInitialize() {
		instance = this;
		this.camera = new GenshinCamera(instance);
		AutoConfig.register(GTPConfig.class, JanksonConfigSerializer::new);
		this.config = AutoConfig.getConfigHolder(GTPConfig.class).getConfig();
	}

	public static GenshinThirdPerson getInstance() {
		return instance;
	}
	public GenshinCamera getCamera() {
		return camera;
	}
	public GTPConfig getConfig() {
		return config;
	}
}