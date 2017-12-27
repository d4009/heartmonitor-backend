package com.dathuynh.rest;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.FormParam;
import javax.ws.rs.core.Response;
import javax.json.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Root resource (exposed at "user" path)
 */
@Path("user")
public class UserControl {

    /**
     * Method handling HTTP POST requests. The returned object will be sent
     * to the client as "application/json" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
    @POST
    @Path("register")
    @Consumes("application/x-www-form-urlencoded")
    @Produces("application/json")
    public Response newUser(
            @FormParam("username") String username,
            @FormParam("password") String password,
            @FormParam("name") String name,
            @FormParam("gender") String gender,
            @FormParam("dob") String dob,
            @FormParam("height") int height,
            @FormParam("weight") int weight,
            @FormParam("emergency_phone") String emergency_phone
            ) {
        // data init
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date dobdate = new Date();
        try {
          dobdate = df.parse(dob);
        } catch (Exception e) {
          e.printStackTrace();
        }
        long dobepoch = dobdate.getTime();
        String hashedPassword = org.apache.commons.codec.digest.DigestUtils.sha256Hex(password);
        // end data init
        // create json response
        try {
          MySQLConnector connector = new MySQLConnector();
          connector.newUser(new User.UserBuilder(username, hashedPassword)
          .name(name)
          .gender(gender.equals("true"))
          .dob(dobdate)
          .height(height)
          .weight(weight)
          .emergency_phone(emergency_phone)
          .build());
          connector.close();
        } catch (Exception e) {
          e.printStackTrace();
          return Response.status(500).build();
        }
        JsonObject jsonObj = Json.createObjectBuilder()
            .add("username", username)
            .add("password", hashedPassword)
            .add("name", name)
            .add("gender", gender)
            .add("dob", df.format(dobdate))
            .add("height", String.valueOf(height))
            .add("weight", String.valueOf(weight))
            .add("emergency_phone", emergency_phone)
            .build();
        String json = jsonObj.toString();
        return Response.ok(json, MediaType.APPLICATION_JSON).build();
    }

    @POST
    @Path("login")
    @Consumes("application/x-www-form-urlencoded")
    @Produces("application/json")
    public Response login(
        @FormParam("username") String username,
        @FormParam("password") String password
        ) {
      User loginSuccess = null;
      try {
        MySQLConnector connector = new MySQLConnector();
        loginSuccess = connector.login(username, password);
        connector.close();
      } catch (Exception e) {
        e.printStackTrace();
      } finally {
        if (loginSuccess != null) {
          SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
          JsonObject jsonObj = Json.createObjectBuilder()
              .add("username", loginSuccess.getUsername())
              .add("password", loginSuccess.getPassword())
              .add("name", loginSuccess.getName())
              .add("gender", loginSuccess.getGender())
              .add("dob", df.format(loginSuccess.getDob()))
              .add("height", String.valueOf(loginSuccess.getHeight()))
              .add("weight", String.valueOf(loginSuccess.getWeight()))
              .add("emergency_phone", loginSuccess.getEmergencyPhone())
              .build();
          String json = jsonObj.toString();
          return Response
          .ok(json, MediaType.APPLICATION_JSON)
          .header("Access-Control-Allow-Origin", "*")
          .build();
        } else {
          return Response
            .status(401)
            .header("Access-Control-Allow-Origin", "*")
            .build();
        }
      }
    }
}
