package icu.puqns67.skintypefix.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.authlib.SignatureState;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.minecraft.MinecraftProfileTextures;
import icu.puqns67.skintypefix.SkinTypeFix;
import icu.puqns67.skintypefix.accessor.PlayerSkinTextureAccessor;
import icu.puqns67.skintypefix.util.Utils;
import icu.puqns67.skintypefix.util.image.Places;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.PlayerSkinProvider;
import net.minecraft.client.util.SkinTextures;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;


@Environment(EnvType.CLIENT)
@Mixin(PlayerSkinProvider.class)
public class PlayerSkinProviderMixin {
	@Inject(method = "fetchSkinTextures(Ljava/util/UUID;Lcom/mojang/authlib/minecraft/MinecraftProfileTextures;)Ljava/util/concurrent/CompletableFuture;", at = @At("TAIL"), cancellable = true)
	private void onFetchSkinTexturesPrivate(
		UUID uuid,
		MinecraftProfileTextures textures,
		CallbackInfoReturnable<CompletableFuture<SkinTextures>> cir,
		@Local(ordinal = 0) MinecraftProfileTexture skinTextureOrigin,
		@Local(ordinal = 0) CompletableFuture<Identifier> skinFuture,
		@Local(ordinal = 1) CompletableFuture<Identifier> capeFuture,
		@Local(ordinal = 2) CompletableFuture<Identifier> elytraFuture,
		@Local SkinTextures.Model skinModelOrigin,
		@Local String skinUrl
	) {
		// Skip fix if using internal skin or uuid is invalid uuid
		if (skinTextureOrigin == null || Utils.isInvalidUUID(uuid)) {
			return;
		}

		// If model is defaults to SLIM, and config `skipFixForSlimPlayers` is set to true,
		// checks are skipped because some skins have bad pixels
		if (skinModelOrigin == SkinTextures.Model.SLIM && SkinTypeFix.CONFIG.skipFixForSlimPlayers) {
			return;
		}

		var textureManager = MinecraftClient.getInstance().getTextureManager();

		CompletableFuture<SkinTextures.Model> modelFuture = skinFuture.thenApply(v -> {
			// Get texture from TextureManager
			var skinTexture = (PlayerSkinTextureAccessor) textureManager.getTexture(skinFuture.join());

			// Wait skin loading if it needed fetch from web
			skinTexture.skinTypeFix$joinLoader();

			// Get image from PlayerSkinTexture
			var skinImage = skinTexture.skinTypeFix$getImage();
			if (skinImage == null) {
				SkinTypeFix.LOGGER.warn("[SkinTypeFix] [{}] {GET_IMAGE} An error occurred while getting image!", uuid);
				return skinModelOrigin;
			}

			// Check skin type
			var needFix = switch (skinModelOrigin) {
				case SLIM -> !Places.PLAYER.isAllBlack(skinImage);
				case WIDE -> Places.PLAYER.isAllBlack(skinImage);
			};

			if (needFix) {
				var skinModelFixed = Utils.reverseModelType(skinModelOrigin);
				SkinTypeFix.LOGGER.info("[SkinTypeFix] [{}] Fixed skin type: {} -> {}", uuid, skinModelOrigin, skinModelFixed);
				return skinModelFixed;
			}

			return skinModelOrigin;
		});

		// Return
		cir.setReturnValue(
			CompletableFuture
				.allOf(skinFuture, capeFuture, elytraFuture, modelFuture)
				.thenApply(v -> new SkinTextures(
					skinFuture.join(),
					skinUrl,
					capeFuture.join(),
					elytraFuture.join(),
					modelFuture.join(),
					textures.signatureState() == SignatureState.SIGNED
				))
		);
	}
}
