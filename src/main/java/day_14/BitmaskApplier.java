package day_14;

import java.util.Map;

@FunctionalInterface
public interface BitmaskApplier {

    void updateMemoryWithBitmask(Map<Long, Long> memory, WriteToMemoryInstruction instruction, String mask);
}
