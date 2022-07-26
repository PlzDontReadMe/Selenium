import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DebitCardApplicationsTest {

    private WebDriver driver;

    @BeforeAll
    public static void setUpAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
        driver.get("http://localhost:9999/");

    }

    @AfterEach
    public void tearDown() {
        driver.quit();
        driver = null;
    }

    @Test
    public void shouldHappyPathRequest() {
        driver.findElement(By.cssSelector("[data-test-id=\"name\"] input")).sendKeys("Александр Иванов-Иванов");
        driver.findElement(By.cssSelector("[data-test-id=\"phone\"] input")).sendKeys("+79099095555");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.className("button__content")).click();
        String actual = driver.findElement(By.cssSelector("[data-test-id=\"order-success\"]")).getText().trim();
        String expected = "Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.";
        assertEquals(expected, actual);
    }

    @Test
    public void shouldInvalidName() {


        driver.findElement(By.cssSelector("[data-test-id=\"name\"] input")).sendKeys("Alex Ivanov");
        driver.findElement(By.cssSelector("[data-test-id=\"phone\"] input")).sendKeys("+79099095555");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.className("button__content")).click();
        String actual = driver.findElement(By.cssSelector("[data-test-id=\"name\"].input_invalid .input__sub")).getText().trim();
        String expected = "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.";
        assertEquals(expected, actual);
    }

    @Test
    public void shouldNumberBelowLimit() {
        driver.findElement(By.cssSelector("[data-test-id=\"name\"] input")).sendKeys("Александр Иванов-Иванов");
        driver.findElement(By.cssSelector("[data-test-id=\"phone\"] input")).sendKeys("+7909909555");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.className("button__content")).click();
        String actual = driver.findElement(By.cssSelector("[data-test-id=\"phone\"].input_invalid .input__sub")).getText().trim();
        String expected = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        assertEquals(expected, actual);
    }

    @Test
    public void shouldNumberOverLimit() {
        driver.findElement(By.cssSelector("[data-test-id=\"name\"] input")).sendKeys("Александр Иванов-Иванов");
        driver.findElement(By.cssSelector("[data-test-id=\"phone\"] input")).sendKeys("+790990955555");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.className("button__content")).click();
        String actual = driver.findElement(By.cssSelector("[data-test-id=\"phone\"].input_invalid .input__sub")).getText().trim();
        String expected = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        assertEquals(expected, actual);
    }

    @Test
    public void shouldEmptyNumber() {
        driver.findElement(By.cssSelector("[data-test-id=\"name\"] input")).sendKeys("Александр Иванов-Иванов");
        driver.findElement(By.cssSelector("[data-test-id=\"phone\"] input")).sendKeys("");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.className("button__content")).click();
        String actual = driver.findElement(By.cssSelector("[data-test-id=\"phone\"].input_invalid .input__sub")).getText().trim();
        String expected = "Поле обязательно для заполнения";
        assertEquals(expected, actual);
    }

    @Test
    public void shouldEmptyName() {
        driver.findElement(By.cssSelector("[data-test-id=\"name\"] input")).sendKeys("");
        driver.findElement(By.cssSelector("[data-test-id=\"phone\"] input")).sendKeys("+79099095555");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.className("button__content")).click();
        String actual = driver.findElement(By.cssSelector("[data-test-id=\"name\"].input_invalid .input__sub")).getText().trim();
        String expected = "Поле обязательно для заполнения";
        assertEquals(expected, actual);
    }

    @Test
    public void shouldEmptyCheckBox() {
        driver.findElement(By.cssSelector("[data-test-id=\"name\"] input")).sendKeys("Александр Иванов-Иванов");
        driver.findElement(By.cssSelector("[data-test-id=\"phone\"] input")).sendKeys("+79099095555");
        driver.findElement(By.className("button__content")).click();
        String actual = driver.findElement(By.cssSelector("[data-test-id=\"agreement\"].input_invalid")).getText().trim();
        String expected = "Я соглашаюсь с условиями обработки и использования моих персональных данных и разрешаю сделать запрос в бюро кредитных историй";
        assertEquals(expected, actual);
    }
}

