package com.lukman.people;

import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

@Path("/people")
@Tag(name = "People")
public class PersonResource {

    @Inject PersonService personService;

    private static final String XLSX_MIME_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

    @GET
    @Path("export")
    @Produces(XLSX_MIME_TYPE)
    public Response getReport(@QueryParam("type") FileType type) {
        String contentDisposition;
        if (type == FileType.EXCEL) {
            contentDisposition = "attachment; filename=customer-report.xlsx";
            byte[] fileContent = personService.exportToExcel();
            return Response.ok(fileContent)
                    .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                    .build();
        }

        //TODO implement support for Apache PDFBox, Word, etc
        return Response.noContent().build();
    }

    public enum FileType { EXCEL, PDF }

}
