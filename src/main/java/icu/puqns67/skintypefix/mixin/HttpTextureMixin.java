package icu.puqns67.skintypefix.mixin;

import com.mojang.blaze3d.platform.NativeImage;
import icu.puqns67.skintypefix.accessor.HttpTextureAccessor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.texture.HttpTexture;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.*;

import java.io.InputStream;
import java.util.concurrent.CompletableFuture;

@Environment(EnvType.CLIENT)
@Mixin(HttpTexture.class)
public abstract class HttpTextureMixin extends SimpleTexture implements HttpTextureAccessor {
	@Shadow
	@Final
	private static Logger LOGGER;
	@Unique
	@Nullable
	protected NativeImage skinTypeFix$image = null;
	@Shadow
	@Nullable
	private CompletableFuture<?> future;
	@Shadow
	@Final
	private boolean processLegacySkin;

	public HttpTextureMixin(ResourceLocation location) {
		super(location);
	}

	@Shadow
	@Nullable
	protected abstract NativeImage processLegacySkin(NativeImage image);

	@Unique
	public void skinTypeFix$joinFuture() {
		if (this.future != null) {
			this.future.join();
		}
	}

	@Unique
	@Nullable
	public NativeImage skinTypeFix$getImage() {
		return this.skinTypeFix$image;
	}

	/**
	 * @author Puqns67
	 * @reason Overwrite the load() function to create another NativeImage at loading, using for check skin.
	 */
	@Overwrite
	@Nullable
	private NativeImage load(InputStream stream) {
		try {
			var result = NativeImage.read(stream);
			if (this.processLegacySkin) {
				// If this.processLegacySkin is true, the image is the player's skin, so a backup needs to be created for check
				this.skinTypeFix$image = new NativeImage(64, 64, true);
				this.skinTypeFix$image.copyFrom(result);

				result = this.processLegacySkin(result);
			}
			return result;
		} catch (Exception e) {
			LOGGER.warn("Error while loading the skin texture", e);
		}
		return null;
	}
}
