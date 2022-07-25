import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DebitCardApplicationsTest {

    private WebDriver driver;

    @BeforeAll
    public static void setUpAll(){
        System.setProperty("webdriver.chrome.driver","C:\\Projects\\Selenium\\driver\\win\\chromedriver.exe");
    }

    @BeforeEach
    public void setUp(){
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);

    }

    @AfterEach
    public void tearDown(){
        driver.quit();
        driver = null;
    }

    @Test
    public void shouldHappyPathRequest(){
        driver.get("http://localhost:9999/");
        List<WebElement> result = driver.findElements(By.className("input__control"));
        result.get(0).sendKeys("Кулябин Александр");
        result.get(1).sendKeys("+79090909333");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.className("button__content")).click();
        String actual = driver.findElement(By.className("Success_successBlock__2L3Cw")).getText().trim();
        String expected = "Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.";
        assertEquals(expected,actual);
    }

    @Test
    public void shouldInvalidName (){
        driver.get("http://localhost:9999/");
        List<WebElement> result = driver.findElements(By.className("input__control"));
        result.get(0).sendKeys("Kulyabin Aleksandr");
        result.get(1).sendKeys("+79090909333");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.className("button__content")).click();
        List<WebElement> invalidResult = driver.findElements(By.className("input__sub"));
        String actual = invalidResult.get(0).getText().trim();
        String expected = "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.";
        assertEquals(expected,actual);
    }

    @Test
    public void shouldNumberBelowLimit(){
        driver.get("http://localhost:9999/");
        List<WebElement> result = driver.findElements(By.className("input__control"));
        result.get(0).sendKeys("Кулябин Александр");
        result.get(1).sendKeys("+7909090933");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.className("button__content")).click();
        List<WebElement> invalidResult = driver.findElements(By.className("input__sub"));
        String actual = invalidResult.get(1).getText().trim();
        String expected = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        assertEquals(expected,actual);
    }

    @Test
    public void shouldNumberOverLimit(){
        driver.get("http://localhost:9999/");
        List<WebElement> result = driver.findElements(By.className("input__control"));
        result.get(0).sendKeys("Кулябин Александр");
        result.get(1).sendKeys("+790909093333");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.className("button__content")).click();
        List<WebElement> invalidResult = driver.findElements(By.className("input__sub"));
        String actual = invalidResult.get(1).getText().trim();
        String expected = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        assertEquals(expected,actual);
    }

    @Test
    public void shouldEmptyNumber(){
        driver.get("http://localhost:9999/");
        List<WebElement> result = driver.findElements(By.className("input__control"));
        result.get(0).sendKeys("Кулябин Александр");
        result.get(1).sendKeys("");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.className("button__content")).click();
        List<WebElement> invalidResult = driver.findElements(By.className("input__sub"));
        String actual = invalidResult.get(1).getText().trim();
        String expected = "Поле обязательно для заполнения";
        assertEquals(expected,actual);
    }

    @Test
    public void shouldEmptyName(){
        driver.get("http://localhost:9999/");
        List<WebElement> result = driver.findElements(By.className("input__control"));
        result.get(0).sendKeys("");
        result.get(1).sendKeys("+79090903333");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.className("button__content")).click();
        List<WebElement> invalidResult = driver.findElements(By.className("input__sub"));
        String actual = invalidResult.get(0).getText().trim();
        String expected = "Поле обязательно для заполнения";
        assertEquals(expected,actual);
    }

}

