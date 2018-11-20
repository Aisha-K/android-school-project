package com.example.angemichaella.homeservices;

import org.junit.Test;
import static org.junit.Assert.*;

public class HomeServiceTest {

    @Test
    public void checkServiceName(){
        Service aService = new Service("Furniture Assemble", false, 120, "id1234");
        assertEquals("Check the name of the service", "Furniture Assemble",aService.name());
    }

    @Test
    public void checkServiceType(){
        Service aService = new Service("Furniture Assemble", false, 120, "id1234");
        assertEquals("Check the type of the service", "indoor", aService.type());
    }

    @Test
    public void checkServiceRate(){
        Service aService = new Service("Furniture Assemble", false, 120, "id1234");
        assertEquals(120 ,aService.rate(),0);
    }

    @Test
    public void checkServiceId(){
        Service aService = new Service("Furniture Assemble", false, 120, "id1234");
        assertEquals("Check the id of the service", "id1234",aService.id());
    }

    @Test
    public void checkUserName(){
        User aUser = new User("name", "password", "myEmailAddress","myid", "myType");
        assertEquals("Check the name of the user", "name", aUser.getUsername());
    }


    // 2 NEW UNIT TEST CASES ADDED & RELATED TO DELIVERABLE 3

    @Test
    public void checkAvailabilityDay(){
        Availability av = new Availability(Day.MONDAY, new Time(1,0,1), new Time(1,10,1));
        assertEquals("Check the day of the availability", Day.MONDAY,av.getDay());
    }

    @Test
    public void checkAvailabilityTimeFrom(){
        Availability av = new Availability(Day.MONDAY, new Time(1,0,1), new Time(1,10,1));
        assertEquals("Check the time from of the availability", new Time(1,0,1),av.getFrom());
    }

}


