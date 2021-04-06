package ar.edu.itba.paw.service;

import static org.junit.Assert.assertEquals;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import ar.edu.itba.paw.model.MenuItem;
import ar.edu.itba.paw.persistence.MenuDao;

@RunWith(MockitoJUnitRunner.class)
public class MenuServiceImplTest {

    private static final long ID = 1;
    private static final String NAME = "Fried Chicken Original";
    private static final String DESCRIPTION = "Delicious Fried Chicken";
    private static final float PRICE = 4.99f;
    private static final long RESTAURANT_ID = 1;

    @InjectMocks
    private MenuServiceImpl menuService = new MenuServiceImpl();

    @Mock
    private MenuDao mockDao;

    @Test
    public void testFindMenuByRestaurantId() {
        Mockito.when(mockDao.findMenuByRestaurantId(Mockito.eq(ID))).thenReturn(
                Stream.of(new MenuItem(
                        ID,
                        NAME,
                        DESCRIPTION,
                        PRICE,
                        RESTAURANT_ID)
                    )
                .collect(Collectors.toList()));

        assertEquals(1, menuService.findMenuByRestaurantId(ID).size());
    }
}
