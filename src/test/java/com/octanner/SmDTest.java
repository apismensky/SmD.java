package com.octanner;

import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.logging.Logger;

import static org.junit.Assert.*;

public class SmDTest
{

  private static final Logger log = Logger.getLogger("SmDTest");

  @Test(expected = IllegalArgumentException.class)
  public void testFromMsPerUnit0()
  {
    SmD.from(1, 1, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFromRange0()
  {
    SmD.from(1, 0, 1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAtRange0()
  {
    SmD.at(1, 0, 1);
  }

  /**
   * .now() should be very close to the current date
   */
  @Test
  public void testNow()
  {
    long now = SmD.now();
    log.info("now: " + now);
    assertTrue(now < SmD.RANGE_HRS);
    Date dtNow = SmD.at(now);
    log.info("converted back to java Date: " + dtNow);
    assertTrue(dtNow.getTime() - SmD.at(now).getTime() < SmD.MS_PER_HOUR);
  }

  /**
   * Calculation for min() value
   */
  @Test
  public void testMin()
  {
    Date early = SmD.at(-SmD.RANGE_HRS / 2);
    log.info("early: " + early);
    assertEquals(early, SmD.min());
  }

  /**
   * Calculation for max() value
   */
  @Test
  public void testMax()
  {
    Date late = SmD.at(SmD.RANGE_HRS - 1);
    log.info("late: " + late);
    assertEquals(late, SmD.max());
  }

  /**
   * Smd date calculated from current time should be the same as the value from
   * current +/- N * RANGE_HRS, where N is 1, 2 ...
   */
  @Test
  public void testCycle()
  {
    Calendar calendar = Calendar.getInstance();
    Date current = calendar.getTime();
    calendar.add(Calendar.HOUR, (int) SmD.RANGE_HRS);
    assertEquals(SmD.from(current.getTime()), SmD.from(calendar.getTimeInMillis()));
    calendar.add(Calendar.HOUR, (int) (-3 * SmD.RANGE_HRS));
    assertEquals(SmD.from(current.getTime()), SmD.from(calendar.getTimeInMillis()));
  }

  /**
   * Current time equals to compared date
   * expired = true
   */
  @Test
  public void testIsExpiredEquals()
  {
    Date current = new Date();
    Long smdDate = SmD.from(current.getTime());
    assertTrue(SmD.isExpired(smdDate));
  }

  /**
   * plus one hour to the current time, expired = false
   */
  @Test
  public void testIsExpiredPlus1Hr()
  {
    Calendar c = Calendar.getInstance();
    c.add(Calendar.HOUR, 1);
    c.getTime().getTime();
    Long smdDate = SmD.from(c.getTime().getTime());
    assertFalse(SmD.isExpired(smdDate));
  }

  /**
   * minus one hour from the current time, expired = true
   */
  @Test
  public void testIsExpiredMinus1Hr()
  {
    Calendar c = Calendar.getInstance();
    c.add(Calendar.HOUR, -1);
    c.getTime().getTime();
    Long smdDate = SmD.from(c.getTime().getTime());
    assertTrue(SmD.isExpired(smdDate));
  }

  @Test
  public void testSillyConstructor() {
    assertNotNull(new SmD());
  }

}

