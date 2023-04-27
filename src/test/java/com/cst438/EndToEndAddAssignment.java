package com.cst438;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.cst438.domain.AssignmentRepository;
import com.cst438.domain.Course;
import com.cst438.domain.CourseRepository;

@SpringBootTest
public class EndToEndAddAssignment {
    public static final String CHROME_DRIVER_FILE_LOCATION = "C:/chromedriver_win32/chromedriver.exe";

    public static final String URL = "http://localhost:3000";
    public static final String TEST_INSTRUCTOR_EMAIL = "dwisneski@csumb.edu";
    public static final int SLEEP_DURATION = 1000; // 1 second.
    public static final String TEST_ASSIGNMENT_NAME = "Test Assignment";
    public static final String TEST_COURSE_TITLE = "Test Course";
    public static final int TEST_COURSE_ID = 99999;

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    AssignmentRepository assignmentRepository;

    @Test
    public void addAssignment() throws Exception {
        Course c = new Course();
        c.setCourse_id(TEST_COURSE_ID);
        c.setInstructor(TEST_INSTRUCTOR_EMAIL);
        c.setSemester("Fall");
        c.setYear(2021);
        c.setTitle(TEST_COURSE_TITLE);

        courseRepository.save(c);

        System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_FILE_LOCATION);
        WebDriver driver = new ChromeDriver();
        // Puts an Implicit wait for 10 seconds before throwing exception
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        driver.get(URL);
        Thread.sleep(SLEEP_DURATION);

        try {
            /*
             * locate input element for test assignment by assignment name
             *
             * To select a radio button in a Datagrid display
             * 1.  find the elements in the assignmentName column of the data grid table.
             * 2.  locate the element with test assignment name and click the input tag.
             */


            //click add assignment button
            driver.findElement(By.xpath("//button[@id='addButton']")).click();

            //enter course ID
            driver.findElement(By.xpath("//input[@id='course']")).sendKeys(Integer.toString(TEST_COURSE_ID));

            //enter assignment name
            driver.findElement(By.xpath("//input[@id='name']")).sendKeys(TEST_ASSIGNMENT_NAME);

            //enter due date
            driver.findElement(By.xpath("//input[@id='date']")).sendKeys(new java.sql.Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000).toString());

            //add course
            driver.findElement(By.xpath("//button[@id='Add']")).click();
            Thread.sleep(SLEEP_DURATION);

            List<WebElement> elements  = driver.findElements(By.xpath("//div[@data-field='assignmentName']/div"));
            boolean found = false;
            for (WebElement we : elements) {
                System.out.println(we.getText()); // for debug
                if (we.getText().equals(TEST_ASSIGNMENT_NAME)) {
                    found=true;
                    we.findElement(By.xpath("descendant::input")).click();
                    break;
                }
            }
            Thread.sleep(SLEEP_DURATION);
            assertTrue( found, "Unable to locate TEST ASSIGNMENT in list of assignments to be graded.");


        } catch (Exception ex) {
            throw ex;
        } finally {

            /*
             *  clean up database so the test is repeatable.
             */
            assignmentRepository.delete(assignmentRepository.findByCourseId(TEST_COURSE_ID));
            courseRepository.delete(c);

            driver.quit();
        }
    }
}
