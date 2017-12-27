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
import java.util.ArrayList;
import java.io.StringWriter;

@Path("admin")
public class AdminControl {
  @GET
  @Path("getPatients")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getPatients(){
    try {
      MySQLConnector connector = new MySQLConnector();
      ArrayList<User> patients = connector.getPatients();
      connector.close();

      JsonArrayBuilder jsonArrBld = Json.createArrayBuilder();
      SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

      for (User user : patients) {
        jsonArrBld.add(
        Json.createObjectBuilder()
        .add("username", user.getUsername())
        .add("password", user.getPassword())
        .add("name", user.getName())
        .add("gender", user.getGender())
        .add("dob", df.format(user.getDob()))
        .add("height", user.getHeight())
        .add("weight", user.getWeight())
        .add("emergency_phone", user.getEmergencyPhone())
        .build());
      }
      JsonArray jsonArr = jsonArrBld.build();
      StringWriter stWriter = new StringWriter();
      JsonWriter jsonWriter = Json.createWriter(stWriter);
      jsonWriter.writeArray(jsonArr);
      jsonWriter.close();
      return Response.ok(stWriter.toString(), MediaType.APPLICATION_JSON).build();
    } catch (Exception e) {
      e.printStackTrace();
      return Response.ok(Json.createObjectBuilder().add("error",e.getMessage()).build(), MediaType.APPLICATION_JSON).build();
    }
  }
}
