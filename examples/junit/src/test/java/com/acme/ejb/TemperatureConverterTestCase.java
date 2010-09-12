package com.acme.ejb;

import javax.ejb.EJB;

import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.api.environment.rule.RequiresJavaEE6;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
@RequiresJavaEE6 // because EJB uses @Resource injection
public class TemperatureConverterTestCase 
{
   @EJB
   private TemperatureConverter converter;

   @Deployment
   public static JavaArchive createTestArchive() {
      return ShrinkWrap.create(JavaArchive.class, "test.jar")
         .addClasses(TemperatureConverter.class, TemperatureConverterBean.class);
   }

   @Test
   public void testConvertToCelsius() {
      Assert.assertEquals(converter.convertToCelsius(32d), 0d, 0d);
      Assert.assertEquals(converter.convertToCelsius(212d), 100d, 0d);
   }

   @Test
   public void testConvertToFarenheit() {
      Assert.assertEquals(converter.convertToFarenheit(0d), 32d, 0d);
      Assert.assertEquals(converter.convertToFarenheit(100d), 212d, 0d);
   }

   @Test
   public void testIsTransactional() {
      Assert.assertTrue(converter.isTransactional());
   }

}
