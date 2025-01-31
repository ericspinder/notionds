package com.notion.dataSource;

import com.notionds.dataSource.Options;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class OptionsTest {


    @Test
    public void testOption () {
        Map<String,Object> properties = new HashMap<>();
        properties.put("com.notionds.connection.Max_Queue_Size",1);
        properties.put(Options.Durations.ConnectionMaxLifetime.getKey(), Duration.ofHours(1));
        Options options = new Options(properties);
        Assertions.assertEquals(1, options.get(Options.Integers.Connection_Max_Queue_Size.getKey()));
        Assertions.assertEquals(Duration.ofHours(1),options.get(Options.Durations.ConnectionMaxLifetime.getKey()));
        options.setValue(Options.Integers.Connection_Max_Queue_Size.getKey(), 100);
        options.setValue(Options.Durations.ConnectionMaxLifetime.getKey(), Duration.ofDays(1));
        Assertions.assertEquals(100, options.get(Options.Integers.Connection_Max_Queue_Size.getKey()));
        Assertions.assertEquals(Duration.ofDays(1), options.get(Options.Durations.ConnectionMaxLifetime.getKey()));
    }
}
