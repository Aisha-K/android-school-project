package com.example.angemichaella.homeservices;

import org.junit.Test;

import java.util.ArrayList;

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


    // 2 NEW UNIT TEST CASES ADDED & RELATED TO DELIVERABLE 3----------------------------------------------------------------

    @Test
    public void checkAvailabilityDay(){
        Availability av = new Availability(Day.MONDAY, new Time(1,0,1), new Time(1,10,1));
        assertEquals("Check the day of the availability", Day.MONDAY,av.getDay());
    }

    @Test
    public void checkAvailabilityTimeFrom(){
        Availability av = new Availability(Day.MONDAY, new Time(1,0,1), new Time(1,10,1));
        assertEquals("Check the availability string value", Day.MONDAY + " from " +new Time(1,0,1)+" to "+new Time(1,10,1),av.toString());
    }

    @Test
    public void checkMerginggAvls(){
        Availability av1 = new Availability(Day.MONDAY, new Time(12,0,Time.AM), new Time(9,0,Time.AM));
        Availability av2 = new Availability(Day.MONDAY, new Time(8,0,Time.AM), new Time(10,0,Time.PM));

        Availability avlMerged = av1.mergeWith(av2);
        Availability avlExpected = new Availability(Day.MONDAY, new Time(12,0,Time.AM), new Time(10,0,Time.PM));

        assertEquals(true, avlMerged.equals(avlExpected));
    }


    //10    NEW UNIT TESTS RELATED TO DELIVERABLE FOUR----------------------------------------------------------

    @Test
    public void checkGetAvgRating(){
        ServiceProvider sp= new ServiceProvider();
        assertEquals(-1.0, sp.getCurrAvgRating(), 0.1);
    }

    @Test
    public void checkAddRating(){
        ServiceProvider sp= new ServiceProvider();
        sp.addRating(5);
        sp.addRating(0);
        sp.addRating(3);
        assertEquals(2.67, sp.getCurrAvgRating(), 0.1);
    }


    @Test
    public void checkGetNumberOfRatesReceived(){
        ServiceProvider sp= new ServiceProvider();
        sp.addRating(5);
        sp.addRating(0);
        sp.addRating(3);
        assertEquals(3, sp.getNumOfRatesReceived());
    }

    @Test
    public void checkOverlapping(){ //for filter functionality
        Availability av = new Availability(Day.FRIDAY, new Time(1,0,0), new Time(3,0,0));
        Availability av2 = new Availability(Day.FRIDAY, new Time(0,0,0), new Time(3,0,0));  //overlaps
        Availability av3 = new Availability(Day.FRIDAY, new Time(0,0,0), new Time(0,30,0)); //not overlapping

        assertEquals( true, av.overlaps(av2));
        assertEquals( false, av.overlaps(av3));

    }


    @Test
    public void checkIsAvailableSometimeDuring(){
        ServiceProvider sp= new ServiceProvider();
        Availability av = new Availability(Day.FRIDAY, new Time(1,0,0), new Time(3,0,0));
        sp.addAvailability( av );
        ArrayList list = new ArrayList<Availability>(1);
        assertEquals(false, sp.isAvailableSometimeDuring( list));   //empty list
        list.add(av);
        assertEquals(true, sp.isAvailableSometimeDuring( list));
    }


    @Test
    public void checkGetServiceProviderId(){
        Booking b= new Booking("sp", "ho", "spId", "Air Conditioning", new Availability(), "101019", "bkId" );
        assertEquals( "spId", b.getServiceProviderId() );
    }

    @Test
    public void checkSetRating(){
        Booking b= new Booking();
        b.setRating(3.3);
        assertEquals(3.3, b.getRating(), 0.1);
    }

    @Test
    public void checkSetAndGetComment(){
        Booking b= new Booking();
        b.setComment("Test");
        assertEquals("Test",b.getComment());
    }

    @Test
    public void checkGetDate(){
        Booking b= new Booking("sp", "ho", "spId", "Air Conditioning", new Availability(), "101019", "bkId" );
        assertEquals( "101019", b.getDate() );

    }

    @Test
    public void checkSetBookingRating(){
        Booking b= new Booking();
        b.setRating(4.5);
        assertEquals(4.5, b.getRating(), 0.1);
    }


}


