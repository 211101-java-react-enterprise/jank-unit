package com.revature.jank_unit;

import com.revature.jank_unit.util.*;

@Describe
public class FakeServiceTestSuite {

    private FakeService sut;

    public void beforeAll() {
        System.out.println("FakeServiceTestSuite#cleanUp invoked!");
    }

    @BeforeEach
    public void setUp() {
        System.out.println("FakeServiceTestSuite#setUp invoked!");
        sut = new FakeService();
    }

    @AfterEach
    public void cleanUp() {
        System.out.println("FakeServiceTestSuite#cleanUp invoked!");
        sut = null;
    }

    public void afterAll() {
        System.out.println("FakeServiceTestSuite#cleanUp invoked!");
    }

    @Test(testDescription = "FakeService#isUserValid should return false when given a null argument")
    public void test_isUserValid_returnsFalse_givenNullUser() {

        System.out.println("Test start");
        // Arrange
        AppUser nullUser = null;

        // Act
        boolean actualResult = sut.isUserValid(nullUser);

        // Assert
        Assert.isFalse(actualResult);
        System.out.println("test end");
    }

    @Test(testDescription = "FakeService#isUserValid should return false when given a null argument")
    public void test_isUserValid_returnsFalse_givenUserWithNullFields() {

        System.out.println("Test start");
        // Arrange
        AppUser invalidUser_1 = new AppUser("valid-id", null, "valid-lastname", "valid-username", "valid-password");
        AppUser invalidUser_2 = new AppUser("valid-id", "valid-firstname", null, "valid-username", "valid-password");
        AppUser invalidUser_3 = new AppUser("valid-id", "valid-firstname", "valid-lastname", null, "valid-password");
        AppUser invalidUser_4 = new AppUser("valid-id", "valid-firstname", "valid-lastname", "valid-username", "asdasd");

        // Act
        boolean actualResult_1 = sut.isUserValid(invalidUser_1);
        boolean actualResult_2 = sut.isUserValid(invalidUser_2);
        boolean actualResult_3 = sut.isUserValid(invalidUser_3);
        boolean actualResult_4 = sut.isUserValid(invalidUser_4);


        // Assert
        Assert.isFalse(actualResult_1);
        Assert.isFalse(actualResult_2);
        Assert.isFalse(actualResult_3);
        Assert.isFalse(actualResult_4);

        System.out.println("test end");

    }





}
