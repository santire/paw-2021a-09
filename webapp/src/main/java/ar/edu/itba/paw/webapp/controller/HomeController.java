package ar.edu.itba.paw.webapp.controller;


import ar.edu.itba.paw.model.*;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import ar.edu.itba.paw.model.Restaurant;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.exceptions.EmailInUseException;
import ar.edu.itba.paw.model.exceptions.TokenCreationException;
import ar.edu.itba.paw.model.exceptions.TokenDoesNotExistException;
import ar.edu.itba.paw.model.exceptions.TokenExpiredException;
import ar.edu.itba.paw.service.RestaurantService;
import ar.edu.itba.paw.service.UserService;
import ar.edu.itba.paw.webapp.auth.PawUserDetailsService;
import ar.edu.itba.paw.webapp.forms.UserForm;



@Controller
public class HomeController {

    private static final Logger LOGGER = LoggerFactory.getLogger(HomeController.class);
    private static final int AMOUNT_OF_RESTAURANTS = 6;
    private static final int AMOUNT_OF_POPULAR_RESTAURANTS = 10;
    private static final int POPULAR_MIN_RATING = 1;
    private static Sorting DEFAULT_SORT = Sorting.NAME;
    private static String DEFAULT_ORDER = "asc";

    @Autowired
    private UserService userService;

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private PawUserDetailsService pawUserDetailsService;

    @Autowired
    private CommonAttributes ca;


    @RequestMapping("/")
    public ModelAndView home(@RequestParam(defaultValue = "1") Integer page) {
        final ModelAndView mav = new ModelAndView("home");
        User loggedUser = ca.loggedUser();



        if(page == null || page <1) {
            page=1;
        }

        List<Restaurant> popularRestaurants = restaurantService.getPopularRestaurants(AMOUNT_OF_POPULAR_RESTAURANTS, POPULAR_MIN_RATING);
        mav.addObject("popularRestaurants", popularRestaurants);
        LOGGER.debug("Amount of popular restaurants: {}", popularRestaurants.size());

        List<Restaurant> hotRestaurants = restaurantService.getHotRestaurants(AMOUNT_OF_POPULAR_RESTAURANTS, 7);
        mav.addObject("hotRestaurants", hotRestaurants);
        LOGGER.debug("Amount of hot restaurants: {}", hotRestaurants.size());

        // If a user is present, show their liked restaurants
        if(loggedUser != null){
            List<Restaurant> likedRestaurants = restaurantService.getLikedRestaurantsPreview(AMOUNT_OF_POPULAR_RESTAURANTS, loggedUser.getId());
            mav.addObject("likedRestaurants", likedRestaurants);
        }
        return mav;
    }






    @RequestMapping(path ={"/restaurants"}, method = RequestMethod.GET)
    public ModelAndView restaurants(@RequestParam(defaultValue = "1") Integer page,
                                    @RequestParam(required = false) String search, @RequestParam(required = false) int[] tags,
                                    @RequestParam(defaultValue = "1") int min, @RequestParam(defaultValue = "10000") int max,
                                    @RequestParam(defaultValue = "name") String sortBy,
                                    @RequestParam(defaultValue = "asc") String order)
                                    {

        final ModelAndView mav = new ModelAndView("restaurants");
        if (search != null) {
            search = search.trim().replaceAll("[^a-zA-ZñÑáéíóúÁÉÍÓÚ\\s]+", "");
        }else {
            search = "";
        }

        mav.addObject("minPrice", min);
        mav.addObject("maxPrice", max);
        mav.addObject("tags", Tags.allTags());

        List<Tags> tagsSelected = new ArrayList<>();
        List<Integer> tagsChecked = new ArrayList<>();
        if(tags!=null){
            for( int i : tags){
                // TODO: fix this, should throw some exception
                if(Tags.valueOf(i) == null)
                    return new ModelAndView("redirect:/403");
                tagsSelected.add(Tags.valueOf(i));
                tagsChecked.add(i);
            }
        }
        mav.addObject("tagsChecked", tagsChecked);

        Sorting sort = Sorting.NAME;
        try {
            sort = Sorting.valueOf(sortBy.toUpperCase());
        } catch (Exception e) {
            LOGGER.warn("Caught illegal sorting option {}, defaulting to NAME", sortBy);
        }
        boolean desc = false;
        if(order != null && order.equals("DESC"))
            desc = true;

        order = desc ? "DESC" : "ASC";
        int maxPages = restaurantService.getRestaurantsFilteredByPageCount(AMOUNT_OF_RESTAURANTS, search, tagsSelected, min, max);
        if(page == null || page <1) {
            page=1;
        }else if (page > maxPages) {
            page = maxPages;
        }

        if(min < 0) {
            min = 0;
        }
        if(max < 0) {
            max = 0;
        }

        mav.addObject("userIsSearching", !search.isEmpty());
        mav.addObject("searchString", search);
        mav.addObject("maxPages", maxPages);
        mav.addObject("sortTypes", Sorting.getSortTypes());
        mav.addObject("defaultSortType", DEFAULT_SORT);
        mav.addObject("defaultOrder", DEFAULT_ORDER);

        mav.addObject("restaurants", restaurantService.getRestaurantsFilteredBy(page, AMOUNT_OF_RESTAURANTS, search, tagsSelected,min,max, sort, desc, 7));
        mav.addObject("page", page);
        mav.addObject("sortBy", sortBy);
        mav.addObject("order", order);
        mav.addObject("desc", desc);
        return mav;
    }




    @RequestMapping(path ={"/register"}, method = RequestMethod.GET)
    public ModelAndView registerForm(@ModelAttribute("userForm") final UserForm form,
            final BindingResult errors) {
       return new ModelAndView("register");
    }



    @RequestMapping(path ={"/register"}, method = RequestMethod.POST)
    public ModelAndView register(@Valid @ModelAttribute("userForm") final UserForm form, 
            final BindingResult errors ) {

        if (errors!=null && errors.hasErrors()) {
            return registerForm(form,errors);
        }

        try{
           userService.register(form.getUsername(), form.getPassword(), form.getFirstName(),
                    form.getLastName(), form.getEmail(), form.getPhone(), ca.getUri());
            return new ModelAndView("activate");
        } catch (EmailInUseException e) {
            LOGGER.error("Email {} is already in use (this should have been caught by validator)", e.getEmail());
            return registerForm(form, errors);
        } catch(TokenCreationException e) {
            LOGGER.error("Could not generate token");
            return registerForm(form, errors).addObject("tokenError", true);
        }
    }


    @RequestMapping(value={"/activate"}, method = RequestMethod.GET)
    public ModelAndView activate(@RequestParam(name="token", required=true) final String token) {
        User user;
        try {
            user = userService.activateUserByToken(token);
        } catch(TokenExpiredException e) {
            LOGGER.error("token {} is expired", token);
            return new ModelAndView("redirect:/register").addObject("expiredToken", true);
        }catch(TokenDoesNotExistException e) {
            LOGGER.warn("token {} does not exist", token);
            return new ModelAndView("activate").addObject("invalidToken", true);
        } catch(Exception e) {
            // Unexpected error happened, showing register screen with generic error message
            return new ModelAndView("redirect:/register").addObject("tokenError", true);
        }

        UserDetails userDetails = pawUserDetailsService.loadUserByUsername(user.getEmail());
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userDetails, userDetails.getPassword(), userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return new ModelAndView("redirect:/");
    }

    @RequestMapping(value={ "/login" }, method=RequestMethod.GET)
    public ModelAndView login(@RequestParam(value = "error", required = false) final String error) {
        final ModelAndView mav = new ModelAndView("login");
        mav.addObject("error", error!=null);

        return mav;
    }

    @RequestMapping("/403")
    public ModelAndView forbidden() {
        final ModelAndView mav = new ModelAndView("error");
        mav.addObject("code", 403);

        return mav;
    }

}
