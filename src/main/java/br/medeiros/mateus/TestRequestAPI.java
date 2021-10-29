package br.medeiros.mateus;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.hamcrest.core.Is;
import org.junit.BeforeClass;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public class TestRequestAPI {

	@BeforeClass
	public static void urlBase() {
		RestAssured.baseURI = "https://reqres.in/api";
		RequestSpecBuilder reqBuilder = new RequestSpecBuilder();
		reqBuilder.log(LogDetail.ALL);
		reqSpec = reqBuilder.build();

		RestAssured.requestSpecification = reqSpec;

	}

	public static RequestSpecification reqSpec;

	private ResponseSpecification createResSpec(int statusHttp) {
		ResponseSpecification resSpec;
		ResponseSpecBuilder resBuilderOk = new ResponseSpecBuilder();
		resBuilderOk.expectStatusCode(statusHttp);
		resSpec = resBuilderOk.build();
		return resSpec;
	}

	@Test
	public void getListSingleUsers() {
		given().log().all().when().get("users/2").then().log().all().spec(this.createResSpec(HttpStatus.SC_OK))
				.body("data.first_name", is("Janet")).body("data.last_name", is("Weaver"));
	}

	@Test
	public void getUserNotFound() {
		given().log().all().when().get("users/23").then().log().all().spec(this.createResSpec(HttpStatus.SC_NOT_FOUND))
				.body("data.first_name", is(nullValue()));

	}

	@Test
	public void clientSuccessfulRegister() {
		given().log().all().contentType("application/json")
				.body("{\"email\": \"eve.holt@reqres.in\", \"password\": \"pistol\"}").when().post("register")

				.then().log().all().spec(this.createResSpec(HttpStatus.SC_OK)).body("id", is(not(nullValue())))
				.body("token", is(not(nullValue())))

		;

	}

	@Test
	public void updateUser() {
		given().log().all().contentType("application/json").body("{\"name\": \"morpheus\", \"job\": \"zion resident\"}")
				.when().put("users/2").then().log().all().spec(this.createResSpec(HttpStatus.SC_OK))
				.body("name", is("morpheus")).body("job", is("zion resident"))

		;

	}

	@Test
	public void deleteUser() {
		given().log().all().when().delete("users/2").then().spec(this.createResSpec(HttpStatus.SC_NO_CONTENT));
	}

}