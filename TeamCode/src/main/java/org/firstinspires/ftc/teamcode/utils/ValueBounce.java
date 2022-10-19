package org.firstinspires.ftc.teamcode.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ValueBounce {
    ArrayList<Integer> values = new ArrayList<>();
    int index = 0;
    int direction = 1;

    public ValueBounce(Integer... Vals) {
        values.addAll(Arrays.asList(Vals));
    }

    public Integer advance() {
        if(index + direction < 0 || index + direction > values.size()) {
            direction *= -1;
        }
        index += direction;
        return values.get(index);
    }

    public Integer getValue(int index) {
        return values.get(index);
    }
}
