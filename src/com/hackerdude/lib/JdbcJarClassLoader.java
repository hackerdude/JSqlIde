
package com.hackerdude.lib;

import java.util.zip.*;
import java.util.*;
import java.io.*;

/**
 * This is a simple, custom classloader that can load a class from a Jar.
 */
public class JdbcJarClassLoader extends ClassLoader {

   long lastModified; // The last modification date/time of the file itself

   ZipFile file; // The ZipFile we're going to use to load the classes in this classLoader.
   String driverFileName; // The filename of the driver.

   /**
    * Creates a new Driver ClassLoader. This call receives the
    * filename of the container this classloader will get its files
    * from. This is only a glorified (and possibly renamed) JAR, but
    * it's dynamically loaded/unloaded as the driver changes date/time
    * (in more or less the same way the XML nodes of DXS are.
    */
   public JdbcJarClassLoader(String driverContainer) throws IOException {

     driverFileName = driverContainer;
     File theFile = new File(driverFileName);
     lastModified = theFile.lastModified();
     file = new ZipFile(theFile);
   }

   /**
    * Returns a class definition by loading the class data and asking
    * defineclass to create a class out of it.
    */
   public Class findClass(String name) throws ClassNotFoundException {
      byte[] b = loadClassData(name);
      return defineClass(name, b, 0, b.length);
   }

   /**
    * This function loads the class. Pretty basic, it gets a ZipEntry
    * from the file and returns the bytes it gets.
    */
   private byte[] loadClassData(String name) throws ClassNotFoundException {
     // This loads the class from the Jar file.
     String entryName = name.replace('.','/').concat(".class"); // com.katmango.whatever -> com/katmango/whatever
     ZipEntry entry = file.getEntry(entryName);
     if ( entry == null ) throw new ClassNotFoundException();
     byte[] bytes = new byte[new Long(entry.getSize()).intValue()];
     try {
       InputStream is = file.getInputStream(entry);
       is.read(bytes,0, bytes.length);
     } catch ( IOException exc ) {
       throw new ClassNotFoundException("I/O Error while reading file: "+exc.toString());
     }
     return bytes;
   }

}