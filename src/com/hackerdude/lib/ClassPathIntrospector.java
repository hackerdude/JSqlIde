package com.hackerdude.lib;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * This class allows you to query the classpath for the
 * existence of classes that implement specified interfaces
 * or inherit from specific classes. You can use this to keep
 * your application logic and your "drivers" for specific
 * functionality decoupled.
 *
 * <P>For example, you might have a series of SQL classes that
 * implement the functionality you want using different database
 * syntaxes. With this you can have them all implement a specific
 * interface and instruct the people who write these drivers to
 * put them in a specific package (maybe the same package as your
 * interface). Then you can simply ask the introspector to "find them".
 * If they are in any jar in the classpath, they will be added the list.
 */
public class ClassPathIntrospector {

	// A cached classloader.
	protected ClassLoader cLoader;

	/**
	 * Creates a new classpath introspector using the
	 * system classloader.
	 */
	public ClassPathIntrospector() {
		this(ClassLoader.getSystemClassLoader());
	}

	/**
	 * Creates a new classpath introspector using the specified classloader.
	 */
	public ClassPathIntrospector(ClassLoader cLoader) {
		this.cLoader = cLoader;
	}

	/**
	 * Retrieves all the classnames found along the classpath which are
	 * assignment compatible with the specified class and live in the
	 * specified package.
	 * <P class=Note>Note: It only reports regular, instantiatable classes (no interfaces, inner
	 * classes or such).
	 */
	public Collection getClassNamesFor(Class compatibleClass, Package pkg) {
	  return getClassNamesFor(System.getProperty("java.class.path", ""),compatibleClass, pkg.getName());
	}

	/**
	 * Retrieves all the classnames found along the classpath which are
	 * assignment compatible with the specified class and live in the
	 * specified package.
	 * <P class=Note>Note: It only reports regular, instantiatable classes (no interfaces, inner
	 * classes or such).
	 */
	public Collection getClassNamesFor(Class compatibleClass, String pkg) {
		return getClassNamesFor(System.getProperty("java.class.path", ""), compatibleClass, pkg);
	}

	/**
	 * Retrieves all the classnames found along the specified classpath classpath which are
	 * assignment compatible with the specified class and live in the
	 * specified package.
	 * <P class=Note>Note: It only reports regular, instantiatable classes (no interfaces, inner
	 * classes or such).
	 */
	public Collection getClassNamesFor(String classPath, Class compatibleClass, Package pkg) {
		return getClassNamesFor(classPath, compatibleClass, pkg.getName());
	}

	/**
	 * This method retrieves all the class names compatible (assignable from)
	 * the class names for a classpath, inside the specified package.
	 * <P class=Note>Note: It only reports regular, instantiatable classes (no interfaces, inner
	 * classes or such).
	 */
	public Collection getClassNamesFor(String classPath, Class compatibleClass, String pkg) {

		String[] paths = initializePath(classPath);
		ArrayList allClasses = new ArrayList();
		for ( int i=0; i<paths.length; i++ ) {
			allClasses.addAll(getClassNamesMatching(paths[i], pkg));
		}

		// Now that we have all classes, filter them so only unique concrete instances of the requested compatibleclass can be used.
		Collection filtered = filterClasses(allClasses, compatibleClass);
		return filtered;
	}

	/**
	 * This method loads all the classes expressed as Strings inside the
	 * allClasses collection, and makes sure they are compatible (assignable
	 * from) the specified class. It also filters out abstracts or interfaces.
	 */
	public Collection filterClasses(Collection allClasses, Class compatibleClass) {
		Iterator iter = allClasses.iterator();
		Collection result = new ArrayList();
		while ( iter.hasNext() ) {
			String thisClass = (String)iter.next();
			// Don't add duplicates.
			if ( ! result.contains(thisClass) ) {
				try {
					Class theClass = Class.forName(thisClass, false, cLoader);
					if ( compatibleClass.isAssignableFrom(theClass) &&
						// Don't add abstracts or interfaces
						! java.lang.reflect.Modifier.isAbstract(theClass.getModifiers()) &&
						! java.lang.reflect.Modifier.isInterface(theClass.getModifiers())  ) {
						result.add(thisClass);
					}
				} catch ( ClassNotFoundException exc ) {} // Ignore. Just don't add it.
			}
		}
		return result;
	}

	/**
	 * This method simply replaces package names in the format xxx.yyy.zzz for
	 * xxx/yyy/zzz or xxx\yyy\zzz (depending on the OS).
	 */
	private String initializePackage(String pkg) {
		return pkg.replace('.', File.separatorChar);
	}

	/**
	 * This method returns all the classes in a specified package from a
	 * spacified path (a JAR, Zip or a directory) as an array of Strings.
	 */
	private Collection getClassNamesMatching(String path, String packageName) {
		String FSPackageName = initializePackage(packageName);
		String packageName4Jar = packageName.replace('.', '/');
		ArrayList al = new ArrayList();
		File theFile = new File(path);
		FileFilter classFileFilter = new ClassFileFilter();
		if ( theFile.isDirectory() ) {
			// Deal with File
			File newDir = new File(theFile.getPath()+File.separator+FSPackageName);
			if ( newDir.exists() ) {
//				System.out.println("Oh, looky there - we found the package in "+newDir.getName()+" Let's see...");
				File[] theFiles = newDir.listFiles(classFileFilter);
				for ( int i=0; i<theFiles.length; i++ ) {
				   String theClassName = packageName+"."+theFiles[i].getName();
				   // Remove the .class
				   theClassName = theClassName.substring(0, theClassName.length()-6); // length of .class
				   al.add(theClassName);
				}
			}
		} else {
//			System.out.println("Can't deal with jar yet - "+path);
			try {
				ZipFile theZipFile = new ZipFile(theFile);
				Enumeration entries = theZipFile.entries();
				while ( entries.hasMoreElements() ) {
					ZipEntry current = (ZipEntry)entries.nextElement();

					String currentName = current.getName();
					if ( currentName.startsWith(packageName4Jar) && currentName.endsWith(".class" ) ) {
//						System.out.println("Jar "+theFile+" - Found! "+currentName);
						String theClassName = currentName;
						// Remove the .class
						theClassName = theClassName.substring(0, theClassName.length()-6); // length of .class
						theClassName = theClassName.replace('/','.');
						al.add(theClassName);
					}

				}
				ZipEntry entry = theZipFile.getEntry(FSPackageName);

				if ( entry != null ) {
					System.out.println("[ClassPathIntrospector] We have ignition ");
				}
			// Ignore errors. Just don't verify that one.
			} catch ( java.io.IOException exc ) {}

		}
		return al;
	}

	/**
	 * This method parses a series of path elements.
	 */
	private String[] initializePath(String ldpath) {
		String ps = File.pathSeparator;
		int ldlen = ldpath.length();
		int i, j, n;
		// Count the separators in the path
		i = ldpath.indexOf(ps);
		n = 0;
		while (i >= 0) {
			n++;
			i = ldpath.indexOf(ps, i+1);
		}
		// allocate the array of paths - n :'s = n + 1 path elements
		String[] paths = new String[n + 1];

		// Fill the array with paths from the ldpath
		n = i = 0;
		j = ldpath.indexOf(ps);
		while (j >= 0) {
			if (j - i > 0) {
				paths[n++] = ldpath.substring(i, j);
			} else if (j - i == 0) {
				paths[n++] = ".";
			}
			i = j + 1;
			j = ldpath.indexOf(ps, i);
		}
		paths[n] = ldpath.substring(i, ldlen);
		return paths;

	}

	/**
	 * A little classFile filter that allows in files ending with .class.
	 */
	class ClassFileFilter implements FileFilter {
	  public boolean accept(File theFile) {
		return theFile.getName().endsWith(".class");
	  }
	}

}