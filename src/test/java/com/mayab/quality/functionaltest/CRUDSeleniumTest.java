package com.mayab.quality.functionaltest;

import org.apache.commons.io.FileUtils;
import org.junit.*;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import io.github.bonigarcia.wdm.WebDriverManager;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CRUDSeleniumTest {
	private static WebDriver driver;
	private String baseUrl;
	private boolean acceptNextAlert = true;
	private StringBuffer verificationErrors = new StringBuffer();
	JavascriptExecutor js;
	
	@Before
	public void setUp() throws Exception {
		WebDriverManager.chromedriver().setup();
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--headless");
		options.addArguments("--no-sandbox");
		options.addArguments("--disable-dev-shm-usage");
		options.addArguments("--disable-gpu");
		options.addArguments("--window-size=1920,1080");
		driver = new ChromeDriver(options);
	    baseUrl = "\"https://www.google.com/\"";
	    driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(60));
	    js = (JavascriptExecutor) driver;
	    driver.get("https://mern-crud-mpfr.onrender.com/");
	  }
	
	//para limpiar la tabla
	@Test
	public void cleanUp() throws Exception {
		driver.get("https://mern-crud-mpfr.onrender.com/");
		pause(3000);
	    WebElement table = driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/table"));
	    List<WebElement> rows = table.findElements(By.xpath(".//tr"));

	    if (rows.size() > 1) {
	        for (int i = 1; i < rows.size(); i++) {
	            driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/table/tbody/tr[1]/td[5]/button[2]")).click();
	    	    driver.findElement(By.xpath("/html/body/div[3]/div/div[3]/button[1]")).click();
	    	    driver.get("https://mern-crud-mpfr.onrender.com/");
	        }
	    }
	    
	    takeScreenshot("postCleanup");
	}
	@Test
	public void testACreate() throws Exception {
		//crear usuario para tenerlo ahí
		driver.get("https://mern-crud-mpfr.onrender.com/");
		pause(3000);
		takeScreenshot("preCreate");
		
	    driver.findElement(By.xpath("//div[@id='root']/div/div[2]/button")).click();
	    driver.findElement(By.name("name")).click();
	    driver.findElement(By.name("name")).clear();
	    driver.findElement(By.name("name")).sendKeys("Max Verstappen");
	    driver.findElement(By.name("email")).click();
	    driver.findElement(By.name("email")).clear();
	    driver.findElement(By.name("email")).sendKeys("mverstappen@gmail.com");
	    driver.findElement(By.name("age")).click();
	    driver.findElement(By.name("age")).clear();
	    driver.findElement(By.name("age")).sendKeys("26");
	    driver.findElement(By.xpath("/html/body/div[3]/div/div[2]/form/div[3]/div[2]/div")).click();
	    driver.findElement(By.xpath("/html/body/div[3]/div/div[2]/form/div[3]/div[2]/div/div[2]/div[1]")).click();
	    driver.findElement(By.xpath("/html/body/div[3]/div/div[2]/form/button")).click();
	    driver.findElement(By.xpath("/html/body/div[3]/div/i")).click();
	    pause(3000);
	    
	    driver.get("https://mern-crud-mpfr.onrender.com/");
	    pause(3000);
	    
	    //tomar la tabla y revisar el texto que hay en esta
	    WebElement table = driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/table"));
	    String tableText = table.getText();
	    
	    assertThat(tableText, containsString("mverstappen@gmail.com"));
	    takeScreenshot("postCreate");
	    
	  }
	
	@Test
	public void testBEmailTaken() throws Exception {
	    driver.get("https://mern-crud-mpfr.onrender.com/");
	    pause(3000);
	    
	    takeScreenshot("preEmailTaken");
	    //crear usuario original
	    driver.findElement(By.xpath("//div[@id='root']/div/div[2]/button")).click();
	    driver.findElement(By.name("name")).sendKeys("Yuki Tsunoda");
	    driver.findElement(By.name("email")).sendKeys("ytsunoda@gmail.com");
	    driver.findElement(By.name("age")).sendKeys("50");
	    driver.findElement(By.xpath("/html/body/div[3]/div/div[2]/form/div[3]/div[2]/div")).click();
	    driver.findElement(By.xpath("/html/body/div[3]/div/div[2]/form/div[3]/div[2]/div/div[2]/div[1]")).click();
	    driver.findElement(By.xpath("/html/body/div[3]/div/div[2]/form/button")).click();
	    driver.findElement(By.xpath("/html/body/div[3]/div/i")).click();
	    pause(3000);
	    //crear usuario con el correo duplicado
	    driver.findElement(By.xpath("//div[@id='root']/div/div[2]/button")).click();
	    driver.findElement(By.name("name")).sendKeys("a");
	    driver.findElement(By.name("email")).sendKeys("ytsunoda@gmail.com");
	    driver.findElement(By.name("age")).sendKeys("23");
	    driver.findElement(By.xpath("/html/body/div[3]/div/div[2]/form/div[3]/div[2]/div")).click();
	    driver.findElement(By.xpath("/html/body/div[3]/div/div[2]/form/div[3]/div[2]/div/div[2]/div[1]")).click();
	    driver.findElement(By.xpath("/html/body/div[3]/div/div[2]/form/button")).click();

	    //esperar para que salga la alerta
	    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	    
	    //como es una alerta, hay que agregar esto extra para que se busque el texto cuando
	    //la alerta ya se ve, si no regresa una string vacía
	    WebElement alertMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[3]/div/div[2]/form/div[5]/div/p")));
	    
	   //extraer el texto de la alerta
	    String result = alertMessage.getText();
	 
	    assertThat(result, is("That email is already taken."));
	    takeScreenshot("postEmailTaken");
	}
	
	 @Test
	  public void testCEditAge() throws Exception {
	    
	    driver.get("https://mern-crud-mpfr.onrender.com/");
	    pause(3000);
	    
	    //creo usuario
	    driver.findElement(By.xpath("//div[@id='root']/div/div[2]/button")).click();
	    driver.findElement(By.name("name")).sendKeys("Oscar Piastri");
	    driver.findElement(By.name("email")).sendKeys("opiastri@gmail.com");
	    driver.findElement(By.name("age")).sendKeys("23");
	    driver.findElement(By.xpath("/html/body/div[3]/div/div[2]/form/div[3]/div[2]/div")).click();
	    driver.findElement(By.xpath("/html/body/div[3]/div/div[2]/form/div[3]/div[2]/div/div[2]/div[1]")).click();
	    driver.findElement(By.xpath("/html/body/div[3]/div/div[2]/form/button")).click();
	    driver.findElement(By.xpath("/html/body/div[3]/div/i")).click();
	    takeScreenshot("preEditAge");
	    
	    //edito la edad (muchas pausas porque si no no se guarda, el test va muy rápido)
	    pause(3000);
	    driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/table/tbody/tr[1]/td[5]/button[1]")).click();
	    driver.findElement(By.name("age")).click();
	    pause(6000);
	    driver.findElement(By.name("age")).clear();
	    pause(6000);
	    driver.findElement(By.name("age")).sendKeys("50");
	    pause(6000);
	    driver.findElement(By.xpath("/html/body/div[3]/div/div[2]/form/button")).click();
	    pause(6000);
	    driver.findElement(By.xpath("/html/body/div[3]/div/i")).click();
	    
	    takeScreenshot("postEditAge");
	    
	    String result = driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/table/tbody/tr[1]/td[3]")).getText();
	    
	    assertThat(result, is("50"));
	  }

	@Test
	  public void testDDelete() throws Exception {
	    driver.get("https://mern-crud-mpfr.onrender.com/");
	    pause(3000);
	    //crear usuario
	    driver.findElement(By.xpath("//div[@id='root']/div/div[2]/button")).click();
	    driver.findElement(By.name("name")).sendKeys("Valtteri Bottas");
	    driver.findElement(By.name("email")).sendKeys("vbottas@gmail.com");
	    driver.findElement(By.name("age")).sendKeys("33");
	    driver.findElement(By.xpath("/html/body/div[3]/div/div[2]/form/div[3]/div[2]/div")).click();
	    driver.findElement(By.xpath("/html/body/div[3]/div/div[2]/form/div[3]/div[2]/div/div[2]/div[1]")).click();
	    driver.findElement(By.xpath("/html/body/div[3]/div/div[2]/form/button")).click();
	    driver.findElement(By.xpath("/html/body/div[3]/div/i")).click();
	    pause(3000);
	    takeScreenshot("preDelete");
	    driver.get("https://mern-crud-mpfr.onrender.com/");
	    pause(3000);
	    //revisar la tabla y guardar el tamaño actual
	    WebElement table = driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/table"));
	    List<WebElement> rowsPreDelete = table.findElements(By.xpath(".//tr"));
	    
	    int rowNumPreDelete = rowsPreDelete.size();
	    
	    
	    //eliminar usuario
	    driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/table/tbody/tr[1]/td[5]/button[2]")).click();
	    driver.findElement(By.xpath("/html/body/div[3]/div/div[3]/button[1]")).click();
	    pause(3000);
	    driver.get("https://mern-crud-mpfr.onrender.com/");
	    pause(3000);
	    //tomar tabla después de refrescar y ver el tamaño nuevamente
	    table = driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/table"));
	    List<WebElement> rowsPostDelete = table.findElements(By.xpath(".//tr"));
	    int rowNumPostDelete = rowsPostDelete.size();
	    
	    String tableText = table.getText();
	    //verificar que haya una fila menos y que no esté el correo del usuario que eliminé
	    assertThat(rowNumPostDelete, is(rowNumPreDelete - 1));
	    assertThat(tableText, not(containsString("vbottas@gmail.com")));
	    
	    takeScreenshot("postDelete");
	    
	  }
	
	 @Test
	  public void testESearchName() throws Exception {
	    driver.get("https://mern-crud-mpfr.onrender.com/");
	    pause(3000);
	    takeScreenshot("preSearchName");
	    //crear usuario
	    driver.findElement(By.xpath("//div[@id='root']/div/div[2]/button")).click();
	    driver.findElement(By.name("name")).sendKeys("Lance Stroll");
	    driver.findElement(By.name("email")).sendKeys("lstroll@gmail.com");
	    driver.findElement(By.name("age")).sendKeys("25");
	    driver.findElement(By.xpath("/html/body/div[3]/div/div[2]/form/div[3]/div[2]/div")).click();
	    driver.findElement(By.xpath("/html/body/div[3]/div/div[2]/form/div[3]/div[2]/div/div[2]/div[1]")).click();
	    driver.findElement(By.xpath("/html/body/div[3]/div/div[2]/form/button")).click();
	    driver.findElement(By.xpath("/html/body/div[3]/div/i")).click();
	    pause(3000);
	    
	    driver.get("https://mern-crud-mpfr.onrender.com/");
	    pause(3000);
	    //buscar en la tabla si está presente el nombre del usuario creado
	    WebElement table = driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/table"));
	    String tableText = table.getText();
	    
	    assertThat(tableText, containsString("Lance Stroll"));
	    takeScreenshot("postNameFound");
	  }
	 
	 
	 @After
	  public void tearDown() throws Exception {
	    driver.quit();
	    String verificationErrorString = verificationErrors.toString();
	    if (!"".equals(verificationErrorString)) {
	      fail(verificationErrorString);
	    }
	  }
	 
	 private void pause(long mils) {
		  try {
			  Thread.sleep(mils);
		  }
		  catch(Exception e) {
			  e.printStackTrace();
		  }
	  }
	 
	 public static void takeScreenshot(String fileName)
			  	throws IOException {
				  File file = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
				  FileUtils.copyFile(file, new File("src/screenshots/" + fileName + ".jpeg"));
			  }
}
