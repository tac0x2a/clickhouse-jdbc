package net.tac42.clickhouse.util;

import org.testng.Assert;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

public class TypeUtilsTest {

  @Test
  public void testTypeIsNullable() throws Exception {
    Assert.assertEquals(TypeUtils.NULLABLE_NO,TypeUtils.isTypeNull("DateTime"));
    Assert.assertEquals(TypeUtils.NULLABLE_NO,TypeUtils.isTypeNull("Float64"));
    Assert.assertEquals(TypeUtils.NULLABLE_YES,TypeUtils.isTypeNull("Nullable(Float64)"));
    Assert.assertEquals(TypeUtils.NULLABLE_YES,TypeUtils.isTypeNull("Nullable(DateTime)"));
  }

  @Test
  public void testGetDecimalDigits() throws Exception {
      assertEquals(TypeUtils.getDecimalDigits("DateTime"), 0);
      assertEquals(TypeUtils.getDecimalDigits("Int32"), 0);
      assertEquals(TypeUtils.getDecimalDigits("Array(String)"), 0);
      assertEquals(TypeUtils.getDecimalDigits("Nullable(Int32)"), 0);
      assertEquals(TypeUtils.getDecimalDigits("Nullable(DateTime)"), 0);

      assertEquals(TypeUtils.getDecimalDigits("Float64"), 17);
      assertEquals(TypeUtils.getDecimalDigits("Nullable(Float64)"), 17);
      assertEquals(TypeUtils.getDecimalDigits("Float32"), 8);
      assertEquals(TypeUtils.getDecimalDigits("Nullable(Float32)"), 8);
  }

  @Test
  public void testGetColumnSize() throws Exception {
      assertEquals(TypeUtils.getColumnSize("DateTime"), 19);
      assertEquals(TypeUtils.getColumnSize("Date"), 10);
      assertEquals(TypeUtils.getColumnSize("UInt8"), 3);
      assertEquals(TypeUtils.getColumnSize("Int32"), 11);
      assertEquals(TypeUtils.getColumnSize("Float32"), 8);
      assertEquals(TypeUtils.getColumnSize("String"), 0);
      assertEquals(TypeUtils.getColumnSize("FixedString(12)"), 12);
      assertEquals(TypeUtils.getColumnSize("Enum8"), 0);
      assertEquals(TypeUtils.getColumnSize("Array(String)"), 0);

      assertEquals(TypeUtils.getColumnSize("Nullable(Int32)"), 11);
      assertEquals(TypeUtils.getColumnSize("Nullable(DateTime)"), 19);
      assertEquals(TypeUtils.getColumnSize("Nullable(FixedString(4))"), 4);
  }
}