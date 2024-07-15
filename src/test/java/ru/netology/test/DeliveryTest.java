package ru.netology.test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import org.openqa.selenium.Keys;
import ru.netology.data.DataGenerator;
import java.time.Duration;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;


class DeliveryTest {
    @BeforeAll
    static void setUPAll(){
        SelenideLogger.addListener("allure", new AllureSelenide());
    }
    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should successful plan and replan meeting")
    void shouldSuccessfulPlanAndReplanMeeting() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 3;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        var daysToAddForSecondMeeting = 7;
        var secondMeetingDate = DataGenerator.generateDate(daysToAddForSecondMeeting);
        String city = DataGenerator.generateCity("ru");
        String name = DataGenerator.generateName("ru");
        String phone = DataGenerator.generatePhone("ru");

        Configuration.holdBrowserOpen = true;

        $("input[placeholder=Город]").setValue(city);

        $("[placeholder='Дата встречи']").doubleClick().sendKeys(Keys.DELETE);
        $x("//input[@placeholder='Дата встречи']").setValue(firstMeetingDate);
        $("input[name=name]").setValue(name);

        $("input[name=phone]").setValue(phone);
        $("label[data-test-id=agreement]").click();
        $(byText("Запланировать")).click();
        $(".notification__content")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(Condition.exactText("Встреча успешно запланирована на " + firstMeetingDate));
        refresh();

        $("input[placeholder=Город]").setValue(city);

        $("[placeholder='Дата встречи']").doubleClick().sendKeys(Keys.DELETE);
        $x("//input[@placeholder='Дата встречи']").setValue(secondMeetingDate);
        $("input[name=name]").setValue(name);

        $("input[name=phone]").setValue(phone);
        $("label[data-test-id=agreement]").click();
        $(byText("Запланировать")).click();
        $x("//*[contains(text(), 'У вас уже запланирована встреча на другую дату. Перепланировать?!");
        $x("//span[contains(text(), 'Перепланировать')]").click();
        $(".notification__content")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(Condition.exactText("Встреча успешно запланирована на " + secondMeetingDate));
    }
}