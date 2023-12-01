package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.MenuItem;
import ar.edu.itba.paw.model.exceptions.MenuItemNotFoundException;
import ar.edu.itba.paw.service.MenuService;
import ar.edu.itba.paw.webapp.dto.MenuItemDto;
import ar.edu.itba.paw.webapp.forms.MenuItemForm;
import ar.edu.itba.paw.webapp.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Path("/restaurants/{restaurantId}/menu")
public class MenuController {
    private static final int AMOUNT_OF_MENU_ITEMS = 8;
    @Context
    private UriInfo uriInfo;

    @Autowired
    private MenuService menuService;

    @GET
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response findRestaurantMenu(@PathParam("restaurantId") final Long restaurantId,
                                       @QueryParam("page") @DefaultValue("1") Integer page,
                                       @QueryParam("pageAmount") @DefaultValue("8") Integer pageAmount
    ) {
        if (pageAmount > AMOUNT_OF_MENU_ITEMS || pageAmount < 1) {
            pageAmount = AMOUNT_OF_MENU_ITEMS;
        }

        final int amountOfMenuItems = menuService.getRestaurantMenuCount(restaurantId);
        final List<MenuItemDto> menu = menuService.getRestaurantMenu(page, pageAmount, restaurantId)
                .stream()
                .map(u -> MenuItemDto.fromMenuItem(u, uriInfo))
                .collect(Collectors.toList());
        return PageUtils.paginatedResponse(new GenericEntity<List<MenuItemDto>>(menu) {
        }, uriInfo, page, pageAmount, amountOfMenuItems);
    }

    @GET
    @Path("/{menuId}")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response getMenuItem(@PathParam("restaurantId") final Long restaurantId,
                                @PathParam("menuId") final Long menuId) {
        final MenuItem menuItem = menuService.getMenuItem(menuId).orElseThrow(MenuItemNotFoundException::new);
        return Response.ok(MenuItemDto.fromMenuItem(menuItem, uriInfo)).build();
    }

    @POST
    @Produces(value = {MediaType.APPLICATION_JSON})
    @Consumes(value = {MediaType.APPLICATION_JSON})
    @PreAuthorize("@authComponent.isRestaurantOwner(#restaurantId)")
    public Response addRestaurantMenuItem(@PathParam("restaurantId") final Long restaurantId,
                                          @Valid @NotNull final MenuItemForm menuItem) {
        final MenuItem item = new MenuItem(menuItem.getName(), menuItem.getDescription(), menuItem.getPrice());
        final MenuItem createdItem = menuService.addItemToRestaurant(restaurantId, item);
        final URI uri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(createdItem.getId())).build();
        return Response.created(uri).entity(MenuItemDto.fromMenuItem(createdItem, uriInfo)).build();
    }

    @PUT
    @Path("/{menuItemId}")
    @Produces(value = {MediaType.APPLICATION_JSON})
    @Consumes(value = {MediaType.APPLICATION_JSON})
    @PreAuthorize("@authComponent.isRestaurantOwner(#restaurantId)")
    public Response updateMenuItem(@PathParam("menuItemId") final Long menuItemId,
                                   @PathParam("restaurantId") final Long restaurantId,
                                   @Valid @NotNull MenuItemForm menuItemForm) {
        final MenuItem newMenuItem = new MenuItem(menuItemForm.getName(), menuItemForm.getDescription(), menuItemForm.getPrice());
        menuService.updateMenuItem(menuItemId, newMenuItem);
        return Response.noContent().build();
    }

    @DELETE
    @Path("/{menuId}")
    @Produces(value = {MediaType.APPLICATION_JSON})
    @PreAuthorize("@authComponent.isRestaurantOwner(#restaurantId)")
    public Response deleteRestaurantMenuItem(@PathParam("restaurantId") final Long restaurantId,
                                             @PathParam("menuId") final Long menuId) {
        menuService.deleteItemById(menuId);
        return Response.ok().build();
    }
}
