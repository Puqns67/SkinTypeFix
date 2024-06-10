package icu.puqns67.skintypefix.mixin.patch;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.authlib.SignatureState;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.minecraft.MinecraftProfileTextures;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import icu.puqns67.skintypefix.Config;
import icu.puqns67.skintypefix.SkinTypeFix;
import icu.puqns67.skintypefix.mixin.accessor.HttpTextureAccessor;
import icu.puqns67.skintypefix.util.Utils;
import icu.puqns67.skintypefix.util.image.Places;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.client.resources.SkinManager;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.nio.file.Path;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@OnlyIn(Dist.CLIENT)
@Mixin(SkinManager.class)
public class SkinManagerMixin {
	@Mutable
	@Final
	@Unique
	private TextureManager skinTypeFix$textureManager;

	@Inject(method = "<init>", at = @At("TAIL"))
	public void onInit(TextureManager textureManager, Path path, MinecraftSessionService minecraftSessionService, Executor executor, CallbackInfo ci) {
		this.skinTypeFix$textureManager = textureManager;
	}

	@Inject(method = "registerTextures", at = @At("TAIL"), cancellable = true)
	private void onRegisterTextures(
		UUID uuid,
		MinecraftProfileTextures textures,
		CallbackInfoReturnable<CompletableFuture<PlayerSkin>> cir,
		@Local(ordinal = 0) MinecraftProfileTexture skinTextureOrigin,
		@Local(ordinal = 0) CompletableFuture<ResourceLocation> skinFuture,
		@Local(ordinal = 1) CompletableFuture<ResourceLocation> capeFuture,
		@Local(ordinal = 2) CompletableFuture<ResourceLocation> elytraFuture,
		@Local PlayerSkin.Model skinModelOrigin,
		@Local String skinUrl
	) {
		// Skip fix if using internal skin or uuid is invalid uuid
		if (skinTextureOrigin == null || Utils.isInvalidUUID(uuid)) {
			return;
		}

		// If model is defaults to SLIM, and config `skipFixForSlimPlayers` is set to true,
		// checks are skipped because some skins have bad pixels
		if (Config.skipFixForSlimPlayers && skinModelOrigin == PlayerSkin.Model.SLIM) {
			return;
		}

		CompletableFuture<PlayerSkin.Model> modelFuture = skinFuture.thenApply(v -> {
			// Get texture from TextureManager
			var skinTexture = (HttpTextureAccessor) skinTypeFix$textureManager.getTexture(skinFuture.join());

			// Wait skin loading if it needed fetch from web
			skinTexture.skinTypeFix$joinFuture();

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
				.thenApply(v -> new PlayerSkin(
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
