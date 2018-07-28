package eventures.utils;

import org.modelmapper.ModelMapper;

public final class MappingUtil {

    public static Object map(Object source, Class<?> target) {
        ModelMapper modelMapper = new ModelMapper();

        return modelMapper.map(source, target);
    }
}
