package io.screret.github.juicesandsodas.properties.block;


import io.screret.github.juicesandsodas.properties.block.blender.BlenderRodOrientation;
import net.minecraft.state.EnumProperty;

public class BlockProperties {
    public static final EnumProperty<BlenderRodOrientation> ROD_ORIENTATION = EnumProperty.create("rod_orientation", BlenderRodOrientation.class);
    /*public static final EnumProperty<AxelOrientation> AXEL_ORIENTATION = EnumProperty.<AxelOrientation>create("axel_orientation", AxelOrientation.class);
    public static final IntegerProperty SALT_LEVEL = IntegerProperty.create("salt_level",0,6);
    public static final EnumProperty<LeatherStatus> LEFT_LEATHER_STATE = EnumProperty.<LeatherStatus>create("leather_left", LeatherStatus.class);
    public static final EnumProperty<LeatherStatus> RIGHT_LEATHER_STATE = EnumProperty.<LeatherStatus>create("leather_right", LeatherStatus.class);
    /* Only used by Horsetail
    public static final BooleanProperty BLOOM_PHASE = BooleanProperty.create("bloom");*/
}