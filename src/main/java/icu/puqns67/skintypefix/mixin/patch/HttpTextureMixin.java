package icu.puqns67.skintypefix.mixin.patch;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.platform.TextureUtil;
import icu.puqns67.skintypefix.SkinTypeFix;
import icu.puqns67.skintypefix.mixin.accessor.HttpTextureAccessor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.texture.HttpTexture;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;

import java.io.InputStream;
import java.util.concurrent.CompletableFuture;

@Environment(EnvType.CLIENT)
@Mixin(HttpTexture.class)
public abstract class HttpTextureMixin extends SimpleTexture implements HttpTextureAccessor {
	@Unique
	@Nullable
	protected NativeImage image = null;
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
	public void skinTypeFix$joinLoader() {
		if (this.future != null) {
			this.future.join();
		}
	}

	@Unique
	public NativeImage skinTypeFix$getImage() {
		return this.image;
	}

	/**
	 * @author Puqns67
	 * @reason Overwrite the load() function to create another NativeImage at loading, using for check skin.
	 */
	@Nullable
	@Overwrite
	private NativeImage load(InputStream stream) {
		try {
			var buffer = TextureUtil.readResource(stream);
			buffer.rewind();
			this.image = NativeImage.read(buffer);
			buffer.rewind();
			var result = NativeImage.read(buffer);
			if (this.processLegacySkin) {
				this.image = this.processLegacySkin(this.image);
				result = this.processLegacySkin(result);
			}
			return result;
		} catch (Exception exception) {
			SkinTypeFix.LOGGER.warn("Error while loading the skin texture", exception);
		}
		return null;
	}
}
