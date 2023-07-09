package ru.netology.sender;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import ru.netology.entity.Country;
import ru.netology.entity.Location;
import ru.netology.geo.GeoService;
import ru.netology.geo.GeoServiceImpl;
import ru.netology.i18n.LocalizationService;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Тесты для проверки языка отправляемого сообщения")
class MessageSenderImplTest {

    GeoService geoService;

    LocalizationService localizationService;

    @Nested
    @DisplayName("если ip относится к российскому сегменту адресов")
    class WhenIPInRussianSegment {

        static final Country COUNTRY = Country.RUSSIA;
        static final Location LOCATION = new Location("Moscow", COUNTRY, null, 0);

        @BeforeEach
        public void setupGeoService() {
            geoService = Mockito.mock(GeoService.class);
            Mockito.when(geoService.byIp(Mockito.argThat(arg -> arg.startsWith("172.") || arg.equals(GeoServiceImpl.MOSCOW_IP))))
                    .thenReturn(LOCATION);
        }

        @BeforeEach
        public void setupLocalizationService() {
            localizationService = Mockito.mock(LocalizationService.class);
            Mockito.when(localizationService.locale(COUNTRY))
                    .thenReturn("Mock:Добро пожаловать");
        }

        @ParameterizedTest
        @ValueSource(strings = {"172.1.2.3", GeoServiceImpl.MOSCOW_IP})
        @DisplayName("Должны быть сообщения на русском")
        void shouldBeRussianMessage(String ip) {

            // Тестируемая система отправки собщений
            final MessageSenderImpl sut = new MessageSenderImpl(geoService, localizationService);

            // Хэдеры сообщения
            final Map<String, String> headers = new HashMap<String, String>();
            headers.put(MessageSenderImpl.IP_ADDRESS_HEADER, ip);

            // ожидаемое сообщение
            final String expected = "Mock:Добро пожаловать";

            final String message = sut.send(headers);

            assertEquals(expected, message);
            Mockito.verify(localizationService, Mockito.atLeastOnce()).locale(COUNTRY);
            Mockito.verifyNoMoreInteractions(localizationService);
        }
    }

    @Nested
    @DisplayName("если ip относится к американскому сегменту адресов")
    class WhenIPInUSASegment {

        static final Country COUNTRY = Country.USA;
        static final Location LOCATION = new Location("New York", COUNTRY, null, 0);

        @BeforeEach
        public void setupGeoService() {
            geoService = Mockito.mock(GeoService.class);
            Mockito.when(geoService.byIp(Mockito.argThat(arg -> arg.startsWith("96.") || arg.equals(GeoServiceImpl.MOSCOW_IP))))
                    .thenReturn(LOCATION);
        }

        @BeforeEach
        public void setupLocalizationService() {
            localizationService = Mockito.mock(LocalizationService.class);
            Mockito.when(localizationService.locale(COUNTRY))
                    .thenReturn("Mock:Welcome");
        }

        @ParameterizedTest
        @ValueSource(strings = {"96.1.2.3", GeoServiceImpl.NEW_YORK_IP})
        @DisplayName("Должны быть сообщения на английском")
        void shouldBeEnglishMessage(String ip) {

            // Тестируемая система отправки собщений
            final MessageSenderImpl sut = new MessageSenderImpl(geoService, localizationService);

            // Хэдеры сообщения
            final Map<String, String> headers = new HashMap<String, String>();
            headers.put(MessageSenderImpl.IP_ADDRESS_HEADER, ip);

            // ожидаемое сообщение
            final String expected = "Mock:Welcome";

            final String message = sut.send(headers);

            assertEquals(expected, message);
            Mockito.verify(localizationService, Mockito.atLeastOnce()).locale(COUNTRY);
            Mockito.verifyNoMoreInteractions(localizationService);
        }
    }

}