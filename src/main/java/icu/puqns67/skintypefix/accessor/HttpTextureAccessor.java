package icu.puqns67.skintypefix.accessor;

import net.minecraft.client.resources.PlayerSkin;
import org.jetbrains.annotations.Nullable;

public interface HttpTextureAccessor {
	void skinTypeFix$joinFuture();

	@Nullable
	PlayerSkin.Model skinTypeFix$getType();
}
