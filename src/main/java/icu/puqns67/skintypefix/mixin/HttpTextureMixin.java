package icu.puqns67.skintypefix.mixin;

import com.mojang.blaze3d.platform.NativeImage;
import icu.puqns67.skintypefix.accessor.HttpTextureAccessor;
import net.minecraft.client.renderer.texture.HttpTexture;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.*;

import javax.annotation.Nullable;
import java.io.InputStream;
import java.util.concurrent.CompletableFuture;

import static icu.puqns67.skintypefix.util.Utils.checkSkinModelType;

@OnlyIn(Dist.CLIENT)
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
	@Unique
	@Nullable
	private PlayerSkin.Model skinTypeFix$type = null;

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
	private void setSkinTypeFix$closeImage() {
		if (this.skinTypeFix$image != null) {
			this.skinTypeFix$image.close();
			this.skinTypeFix$image = null;
		}
	}

	@Unique
	@Nullable
	public PlayerSkin.Model skinTypeFix$getType() {
		if (this.skinTypeFix$type == null) {
			if (this.skinTypeFix$image == null) {
				return null;
			}
			this.skinTypeFix$type = checkSkinModelType(this.skinTypeFix$image);
			this.setSkinTypeFix$closeImage();
		}
		return this.skinTypeFix$type;
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
				result = this.processLegacySkin(result);

				if (result != null) {
					this.setSkinTypeFix$closeImage();

					// If this.processLegacySkin is true, the image is the player's skin,
					// so a backup needs to be created for check.
					this.skinTypeFix$image = new NativeImage(64, 64, true);
					this.skinTypeFix$image.copyFrom(result);
				}
			}
			return result;
		} catch (Exception e) {
			LOGGER.warn("Error while loading the skin texture", e);
		}
		return null;
	}
}
