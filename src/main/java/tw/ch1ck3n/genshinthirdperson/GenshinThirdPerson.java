package tw.ch1ck3n.genshinthirdperson;

import lombok.Getter;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ModInitializer;

import tw.ch1ck3n.genshinthirdperson.config.GTPConfig;
import tw.ch1ck3n.genshinthirdperson.camera.GenshinCamera;
import tw.ch1ck3n.genshinthirdperson.util.DisableMode;

@Getter
public class GenshinThirdPerson implements ModInitializer {

	@Getter
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

	public boolean isEnabled() {
		return (config.cameraBasedMovement.status && camera.isThirdPerson()) &&
				!(config.thirdPersonFrontView.status &&	camera.isThirdPersonFrontView() &&
				config.thirdPersonFrontView.disableMode == DisableMode.DISABLE);
	}
}