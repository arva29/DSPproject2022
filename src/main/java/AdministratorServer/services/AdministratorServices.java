package AdministratorServer.services;

import AdministratorServer.Beans.StatisticsStorage;
import AdministratorServer.Beans.TaxiList;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.sql.Timestamp;

@Path("statistics")
public class AdministratorServices {

    @Path("listOfTaxis")
    @GET
    @Produces({"application/json", "application/xml"})
    public Response getListOfTaxis(){
        return Response.ok(TaxiList.getInstance().getListOfTaxis()).build();
    }

    @Path("{id}/{n}")
    @GET
    @Produces({"application/json", "application/xml"})
    public Response getNStatisticsFromId(@PathParam("id") int id, @PathParam("n") int n){
        return Response.ok(StatisticsStorage.getInstance().getLastNStatistics(n, id)).build();
    }

    @Path("{s1}-{s2}")
    @GET
    @Produces({"application/json", "application/xml"})
    @Consumes({"application/json", "application/xml"})
    public Response getStatisticsBetweenT1nT2(@PathParam("s1") String s1, @PathParam("s2") String s2){
        try {
            Timestamp t1 = Timestamp.valueOf(s1);
            Timestamp t2 = Timestamp.valueOf(s1);
            return Response.ok(StatisticsStorage.getInstance().getAverageStatisticsFromT1ToT2(t1, t2)).build();
        } catch (IllegalArgumentException e){
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

    }
}
