package AdministratorServer.services;

import AdministratorServer.Beans.StatisticsRecord;
import AdministratorServer.Beans.StatisticsStorage;
import AdministratorServer.Beans.TaxiBean;
import AdministratorServer.Beans.TaxiList;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Path("taxi")
public class TaxiServices {

    @Path("add")
    @POST
    @Consumes({"application/json", "application/xml"})
    @Produces({"application/json", "application/xml"})
    public Response addTaxi(TaxiBean taxi){
        if(TaxiList.getInstance().addTaxi(taxi)){
            return Response.ok(TaxiList.getInstance().responseToAddition(taxi.getId())).build();
        } else {
            return Response.status(Response.Status.NOT_ACCEPTABLE).build();
        }
    }

    @Path("remove")
    @DELETE
    @Consumes({"application/json", "application/xml"})
    public Response removeTaxi(TaxiBean taxiBean){
        TaxiList.getInstance().removeTaxi(taxiBean.getId());
        return Response.ok().build();
    }

    @Path("sendStatistics")
    @POST
    @Consumes({"application/json", "application/xml"})
    public Response sendStatistics(StatisticsRecord statisticsRecord){
        StatisticsStorage.getInstance().addStatistics(statisticsRecord);
        return Response.ok().build();
    }
}
