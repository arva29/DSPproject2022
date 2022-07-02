package AdministratorServer.services;

import AdministratorServer.Beans.StatisticsRecord;
import AdministratorServer.StatisticsStorage;
import AdministratorServer.Beans.TaxiNetworkInfo;
import AdministratorServer.Beans.TaxiList;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Path("taxi")
public class TaxiServices {

    @Path("add")
    @POST
    @Consumes({"application/json", "application/xml"})
    @Produces({"application/json", "application/xml"})
    public Response addTaxi(TaxiNetworkInfo taxi){
        if(TaxiList.getInstance().addTaxi(taxi)){
            return Response.ok(TaxiList.getInstance().responseToAddition(taxi.getId())).build();
        } else {
            return Response.status(Response.Status.NOT_ACCEPTABLE).build();
        }
    }

    @Path("remove")
    @DELETE
    @Consumes({"application/json", "application/xml"})
    public Response removeTaxi(TaxiNetworkInfo taxiNetworkInfo){
        TaxiList.getInstance().removeTaxi(taxiNetworkInfo.getId());
        System.out.println("\nTAXI N. " + taxiNetworkInfo.getId() + " REMOVED FROM NETWORK");
        return Response.ok().build();
    }

    @Path("statistics")
    @POST
    @Consumes({"application/json", "application/xml"})
    public Response sendStatistics(StatisticsRecord stat){
        StatisticsStorage.getInstance().addStatistics(stat);
        return Response.ok().build();
    }
}
