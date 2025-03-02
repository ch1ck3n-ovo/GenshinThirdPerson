# ⛏️ Minecraft Fabric Mod
### [**> Join Discord <**](https://discord.gg/xHubQfKJPv)
A **Minecraft Fabric mod** that enhances third-person movement by introducing **Genshin Impact-style camera-based movement**. This mod allows for a smoother, more immersive third-person experience.

# 🎥 Demonstration
### Camera-Based Movement
![Camera-Based Movement](assets/CameraBasedMovement.gif?raw=true)

### Smooth Camera Clip
![Smooth Camera Clip](assets/SmoothCameraClip.gif?raw=true)

### Character Auto-Fade
![Character Auto-Fade](assets/AutoCharacterFade.gif?raw=true)

# 🌟 Features
- **Always Show Crosshair**: Keeps the crosshair visible even when in third-person view.
- **Camera-Based Movement**: Move based on the camera's direction, rather than the character's.
- **Disable Third-Person Front View**: Prevents switching to the front-view.
- **Smooth Camera Clip**: The camera smoothly zooms out when no longer blocked.
- **Auto Character Fade**: Character fades to avoid blocking the view when too close to the camera.

# 🔄 Transition
- **Common**: Linear, Ease, Ease-In, Ease-Out, Ease-In-Out
- **Genshin-Like**: Genshin Impact(1), Genshin Impact(2)

# ⚙️ Configuration
This mod provides various settings via **Mod Menu**.
<details>  
<summary>Click to expand.</summary>

```
{
	"alwaysShowCrosshair": {
		"status": true
	},
	"cameraBasedMovement": {
		"status": true,
		"alignRecoveryDelay": 20,
		"disableWhenElytra": true,
		"disableWhenRiding": true
	},
	"disableThirdPersonFrontView": {
		"status": true
	},
	"smoothCameraClip": {
		"status": true,
		"transitionMode": "LINEAR",
		"startDistance": 26,
		"transitionTime": 20,
		"rotationSpeed": 30,
		"autoCharacterFade": true,
		"applyToMobs": true
	}
}
```
</details>

# 📥 Installation
1. **Download & Install Fabric**: Ensure you have [Fabric Loader](https://fabricmc.net/use/) installed.
2. **Get the Latest Release**: Download the mod from [Modrinth](https://modrinth.com/mod/genshinthirdperson).
3. **Move the Mod to `mods` Folder**: Place the `.jar` file inside your `mods` folder.
4. **Launch the Game**: Start Minecraft with Fabric and enjoy!

# 🛠 Compatibility
- **Minecraft**: 1.21.4
- **Required**: [Fabric API](https://modrinth.com/mod/fabric-api), [Cloth Config API](https://modrinth.com/mod/cloth-config)
- **Suggests**: [Mod Menu](https://modrinth.com/mod/modmenu)

# 🌐 Language
- **Traditional Chinese**
- **English**

# 💖 Credits
Developed by **ch1ck3n-ovo**.

# 📜 License
This mod is licensed under [**GPL-3.0 License**](https://github.com/ch1ck3n-ovo/GenshinThirdPerson/blob/main/LICENSE).

---
*Enjoy a smoother and more immersive third-person experience in Minecraft!*