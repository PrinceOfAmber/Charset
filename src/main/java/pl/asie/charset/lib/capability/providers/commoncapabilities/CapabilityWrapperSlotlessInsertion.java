package pl.asie.charset.lib.capability.providers.commoncapabilities;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.cyclops.commoncapabilities.api.capability.itemhandler.ISlotlessItemHandler;
import pl.asie.charset.api.lib.IItemInsertionHandler;
import pl.asie.charset.lib.capability.CapabilityHelper;

public class CapabilityWrapperSlotlessInsertion implements CapabilityHelper.Wrapper<IItemInsertionHandler> {
	@CapabilityInject(ISlotlessItemHandler.class)
	public static Capability<ISlotlessItemHandler> CAP;

	@Override
	public IItemInsertionHandler get(ICapabilityProvider provider, EnumFacing side) {
		ISlotlessItemHandler handler = CapabilityHelper.get(CAP, provider, side);
		if (handler != null) {
			return new IItemInsertionHandler() {
				@Override
				public ItemStack insertItem(ItemStack stack, boolean simulate) {
					return handler.insertItem(stack, simulate);
				}
			};
		} else {
			return null;
		}
	}
}
