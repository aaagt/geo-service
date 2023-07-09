package ru.netology.geo;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.netology.entity.Country;
import ru.netology.entity.Location;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GeoServiceImplTest {

    private static Stream<Arguments> provideLocationsForIps() {
        return Stream.of(
                Arguments.of("172.1.2.3", new Location("Moscow", Country.RUSSIA, null, 0)),
                Arguments.of(GeoServiceImpl.MOSCOW_IP, new Location("Moscow", Country.RUSSIA, null, 0)),
                Arguments.of("96.1.2.3", new Location("New York", Country.USA, null, 0)),
                Arguments.of(GeoServiceImpl.NEW_YORK_IP, new Location("New York", Country.USA, null, 0))
        );
    }


    @ParameterizedTest
    @MethodSource("provideLocationsForIps")
    void byIp(String ip, Location expected) {
        final GeoServiceImpl sut = new GeoServiceImpl();

        final Location location = sut.byIp(ip);

        assertEquals(expected.getCountry(), location.getCountry());
        assertEquals(expected.getCity(), location.getCity());
    }
}