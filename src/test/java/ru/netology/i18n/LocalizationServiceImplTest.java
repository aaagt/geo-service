package ru.netology.i18n;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.netology.entity.Country;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LocalizationServiceImplTest {

    private static Stream<Arguments> provideLocationsForIps() {
        return Stream.of(
                Arguments.of(Country.RUSSIA, "Добро пожаловать"),
                Arguments.of(Country.USA, "Welcome")
        );
    }


    @ParameterizedTest
    @MethodSource("provideLocationsForIps")
    void locale(Country country, String expected) {
        final LocalizationServiceImpl sut = new LocalizationServiceImpl();

        final String message = sut.locale(country);

        assertEquals(expected, message);
    }
}