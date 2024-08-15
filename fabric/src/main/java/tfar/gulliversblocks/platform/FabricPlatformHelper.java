package tfar.gulliversblocks.platform;

import net.minecraft.core.Registry;
import tfar.gulliversblocks.GulliversBlocks;
import tfar.gulliversblocks.platform.services.IPlatformHelper;
import net.fabricmc.loader.api.FabricLoader;

import java.util.Map;

public class FabricPlatformHelper implements IPlatformHelper {

    @Override
    public String getPlatformName() {
        return "Fabric";
    }

    @Override
    public boolean isModLoaded(String modId) {

        return FabricLoader.getInstance().isModLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {

        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    @Override
    public <F> void registerAll(Map<String,? extends F> map, Registry<F> registry, Class<? extends F> filter) {
        for (Map.Entry<String,? extends F> entry : map.entrySet()) {
            Registry.register(registry, GulliversBlocks.id(entry.getKey()),entry.getValue());
        }
    }

}
