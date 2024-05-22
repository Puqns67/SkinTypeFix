package icu.puqns67.skintypefix.mixin;

import com.mojang.blaze3d.platform.TextureUtil;
import icu.puqns67.skintypefix.SkinTypeFix;
import icu.puqns67.skintypefix.accessor.PlayerSkinTextureAccessor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.PlayerSkinTexture;
import net.minecraft.client.texture.ResourceTexture;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;

import java.io.InputStream;
import java.util.concurrent.CompletableFuture;


@Environment(EnvType.CLIENT)
@Mixin(PlayerSkinTexture.class)
public abstract class PlayerSkinTextureMixin extends ResourceTexture implements PlayerSkinTextureAccessor {
	@Unique
	@Nullable
	protected NativeImage image = null;
	@Shadow
	@Nullable
	private CompletableFuture<?> loader;
	@Shadow
	@Final
	private boolean convertLegacy;

	public PlayerSkinTextureMixin(Identifier location) {
		super(location);
	}

	@Shadow
	@Nullable
	protected abstract NativeImage remapTexture(NativeImage image);

	@Unique
	public void skinTypeFix$joinLoader() {
		if (this.loader != null) {
			this.loader.join();
		}
	}

	@Unique
	public NativeImage skinTypeFix$getImage() {
		return this.image;
	}

	/**
	 * @author Puqns67
	 * @reason test
	 */
	@Nullable
	@Overwrite
	private NativeImage loadTexture(InputStream stream) {
		try {
			var buffer = TextureUtil.readResource(stream);
			buffer.rewind();
			this.image = NativeImage.read(buffer);
			buffer.rewind();
			var result = NativeImage.read(buffer);
			if (this.convertLegacy) {
				this.image = this.remapTexture(this.image);
				result = this.remapTexture(result);
			}
			return result;
		} catch (Exception exception) {
			SkinTypeFix.LOGGER.warn("Error while loading the skin texture", exception);
		}
		return null;
	}
}
