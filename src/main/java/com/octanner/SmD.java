package com.octanner;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;

import java.util.Date;

public class SmD
{
  private static final long MS_PER_SECOND = 1000l;
  private static final long MS_PER_MINUTE = 60l * MS_PER_SECOND;
  @VisibleForTesting
  static final long MS_PER_HOUR = 60l * MS_PER_MINUTE;
  @VisibleForTesting
  static final long RANGE_HRS = (long) Math.pow(2, 16);

  /**
   * Create a super-small date, expressed in hours. It is interpreted as
   * the number of hours since the last whole 2^16 hours since Jan 1, 1970.
   *
   * @return number of hours since the last whole 2^16 hours since Jan 1, 1970
   */
  public static long now()
  {
    return from(System.currentTimeMillis());
  }

  /**
   * Create a super-small date, expressed in hours. It is interpreted as
   * the number of hours since the last whole 2^16 hours since Jan 1, 1970.
   *
   * @param dateMs date im ms from Jan 1, 1970
   * @return number of hours since the last whole 2^16 hours since Jan 1, 1970
   */
  public static long from(long dateMs)
  {
    return from(dateMs, RANGE_HRS, MS_PER_HOUR);
  }

  /**
   * Create a super-small date, expressed in units (i.e seconds, minutes, hours). It is interpreted as
   * the number of units since the last time range since Jan 1, 1970.
   *
   * @param dateMs    date im ms from Jan 1, 1970
   * @param range     duration of the cyclic time range in time units
   * @param msPerUnit number of ms per time unit
   * @return number of time units
   */
  @VisibleForTesting
  static long from(long dateMs, long range, long msPerUnit)
  {
    Preconditions.checkArgument(msPerUnit != 0, "msPerUnit can not be 0");
    Preconditions.checkArgument(range != 0, "range can not be 0");
    long rangeMs = range * msPerUnit;
    return (long) Math.floor((double) (dateMs % rangeMs) / (double) msPerUnit);
  }

  /**
   * Convert a super-small date back to a regular Java Date object.
   *
   * @param dateUnits number of time units
   * @param range     duration of the cyclic time range in time units
   * @param msPerUnit
   * @return
   */
  @VisibleForTesting
  static java.util.Date at(long dateUnits, long range, long msPerUnit)
  {
    Preconditions.checkArgument(range != 0, "range can not be 0");
    long rangeMs = range * msPerUnit;
    return new Date((long) (dateUnits * msPerUnit + Math.floor((double) System.currentTimeMillis() / (double) rangeMs) * rangeMs));
  }

  /**
   * Convert a super-small date back to a regular Java Date object.
   *
   * @param hours number of hours that expresses time
   * @return Date regular Java date
   */
  public static Date at(long hours)
  {
    return at(hours, RANGE_HRS, MS_PER_HOUR);
  }

  /**
   * Earliest date we can represent
   *
   * @return Date
   */
  public static Date min()
  {
    return at(-1 * RANGE_HRS / 2);
  }

  /**
   * Latest date we can represent
   *
   * @return Date
   */
  public static Date max()
  {
    return at(RANGE_HRS - 1);
  }

  /**
   * Compare specified date in SmD format (the number of hours since the last whole 2^16 hours since Jan 1, 1970) with the current date
   *
   * @param hours SmD date in hours
   * @return boolean false if still the date is in the past, otherwise - true
   */
  public static boolean isExpired(long hours)
  {
    return !at(hours).after(new Date());
  }

}

