import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import io.restassured.response.Response;

public class restfulApi {

  private String resourceId;
  
    @BeforeClass
    public void setup() {
      RestAssured.baseURI = "https://api.restful-api.dev";
    }

    //GET Method
    @Test
    public void testGetMethod() {
      Response response = given()
          .when()
          .get("/objects/1")
          .then()
          .statusCode(200)
          .body("id", equalTo("1"))
          .body("name", equalTo("Google Pixel 6 Pro"))
          .body("data.color", equalTo("Cloudy White"))
          .body("data.capacity", equalTo("128 GB"))
          .extract().response();

      System.out.println("GET Response: " + response.asString());
    }
    
    //POST Method
    @Test
    public void testPostMethod() {
      Response response = given()
          .header("Content-Type", "application/json")
          .body("{\n" +
              "   \"name\": \"Apple MacBook Pro 16\",\n" +
              "   \"data\": {\n" +
              "      \"year\": 2024,\n" +
              "      \"price\": 1849.99,\n" +
              "      \"CPU model\": \"Apple M4\",\n" +
              "      \"Hard disk size\": \"1 TB\"\n" +
              "   }\n" +
              "}")
          .when()
          .post("/objects")
          .then()
          .statusCode(200)
          .body("id", notNullValue())
          .body("name", equalTo("Apple MacBook Pro 16"))
          .body("data.year", equalTo(2024))
          .body("data.price", equalTo(1849.99f))
          .body("data.'CPU model'", equalTo("Apple M4"))
          .body("data.'Hard disk size'", equalTo("1 TB"))
          .extract().response();

      resourceId = response.path("id").toString();
      System.out.println("Created Resource ID: " + resourceId);

    }


    //DELETE Method
    @Test(dependsOnMethods = "testPostMethod")
    public void testDeleteMethod() {

      given()
      .when()
      .delete("/objects/" + resourceId)
          .then().statusCode(200);

      //Assert
        given()
        .when()
        .get("/objects/" + resourceId)
        .then()
            .statusCode(404);
    }
}
