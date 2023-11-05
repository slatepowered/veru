package test.slatepowered.veru.collection;

import com.slatepowered.veru.collection.ArrayUtil;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class CollectionUtilTest {

    @Test
    void test_FindRepeatingSection() {
        Integer[] arrayA = new Integer[] { 5, 4, 6, 7, 6, 7, 6, 7 };
        Integer[] arrayB = new Integer[] { 1, 2, 6, 7, 8, 9, 6, 7, 8, 9, 2, 3, 4, 5 };
        Integer[] arrayC = ArrayUtil.repeat(new Integer[] { 6, 7, 8, 9 }, 10);

        System.out.println(ArrayUtil.findRepeatingSection(ArrayUtil.wrap(arrayA)).toStringWithElements());
        System.out.println(ArrayUtil.findRepeatingSection(ArrayUtil.wrap(arrayB)).toStringWithElements());
        System.out.println(ArrayUtil.findRepeatingSection(ArrayUtil.wrap(arrayC)).toStringWithElements());

        System.out.println(Arrays.toString(
                ArrayUtil.arrayWithout(ArrayUtil.findRange(
                        ArrayUtil.findContinuousRepeatingSectionsForward(
                                ArrayUtil.findRepeatingSection(ArrayUtil.wrap(arrayB))
                        )
                ))
        ));

        System.out.println(Arrays.toString(
                ArrayUtil.wrap(arrayB)
                .findRepeatingSection()
                .findContinuousRepeatingRange()
                .arrayWithout()
        ));
    }

}
