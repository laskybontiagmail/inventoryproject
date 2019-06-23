package com.aiur.utilities;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import javax.naming.InitialContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.collection.spi.PersistentCollection;
import org.hibernate.proxy.HibernateProxy;

import com.aiur.common.GenericCache;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Utilities factory.
 */
public class UtilitiesFactory {
	private static Logger logger = LogManager.getLogger(UtilitiesFactory.class);
	private static UtilitiesFactory instance;
	private transient Properties properties;

	private UtilitiesFactory() {
		properties = new Properties();
		try {
			InputStream stream = UtilitiesFactory.class.getResourceAsStream("/application.properties");
			properties.load(stream);
			stream.close();

		} catch (Exception exc) {
		}
	}
	
	/**
	 * Instance
	 */
	public static UtilitiesFactory factory() {
		if (instance == null) {
			instance = new UtilitiesFactory();
		}
		return instance;
	}

	/**
	 * Ping host
	 * 
	 * @param host
	 * @param port
	 * @param timeout
	 */
	public boolean pingHost(String host, int port, int timeout) {
		try (Socket socket = new Socket()) {
			socket.connect(new InetSocketAddress(host, port), timeout);
			return true;
		} catch (IOException ioexc) {
			return false;
		}
	}

	/**
	 * Resource
	 * 
	 * @param file
	 * @param targetType
	 * @throws JsonProcessingException
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> loadResourceAsList(File file, Class<T> targetType) throws JsonProcessingException, IOException {
		JsonFactory factory = new JsonFactory();
		ObjectMapper mapper = new ObjectMapper(factory);

		JsonNode jsonNode = mapper.readTree(file);

		List<T> tempList = mapper.convertValue(jsonNode, List.class);
		List<T> list = new ArrayList<T>();

		for (T obj : tempList) {
			T tempObj = mapper.convertValue(obj, targetType);
			list.add(tempObj);
		}

		return list;
	}

	/**
	 * Resource
	 * 
	 * @param file
	 * @param targetType
	 * @throws JsonProcessingException
	 * @throws IOException
	 */
	public <T> T loadResourceAsObject(File file, Class<T> targetType) throws JsonProcessingException, IOException {
		JsonFactory factory = new JsonFactory();
		ObjectMapper mapper = new ObjectMapper(factory);
		JsonNode jsonNode = mapper.readTree(file);
		T object = mapper.convertValue(jsonNode, targetType);
		return object;
	}

	/**
	 * Load resource
	 * 
	 * @param source
	 * @param dest
	 */
	@SuppressWarnings({ "unchecked" })
	public <T> List<T> loadResources(String source, Class<T> targetType) {
		try {
			if (source != null) {
				JsonFactory jsonFactory = new JsonFactory();
				ObjectMapper mapper = new ObjectMapper(jsonFactory);

				try {
					JsonNode jsonNode = mapper.readTree(source);
					List<T> tempList = mapper.convertValue(jsonNode, List.class);
					List<T> list = new ArrayList<T>();

					for (T obj : tempList) {
						T tempObj = mapper.convertValue(obj, targetType);
						list.add(tempObj);
					}
				} catch (Exception exc) {
					exc.printStackTrace();
				}
			}
		} catch (Exception exc) {
			exc.printStackTrace();
		}
		return null;
	}

	/**
	 * Load resource
	 * 
	 * @param source
	 * @param dest
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <T> T loadResource(String source, Class dest) {
		try {
			if (source != null) {
				JsonFactory jsonFactory = new JsonFactory();
				ObjectMapper mapper = new ObjectMapper(jsonFactory);

				try {
					return (T) mapper.readValue(source, dest);
				} catch (Exception exc) {
					exc.printStackTrace();
				}
			}
		} catch (Exception exc) {
			exc.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * @param jsonFilename
	 * @param targetType
	 * @return
	 * @throws JsonProcessingException
	 * @throws IOException
	 */
	public <T> T loadResourceFile(String jsonFilename, Class<T> targetType)
			throws JsonProcessingException, IOException {
		JsonFactory factory = new JsonFactory();
		ObjectMapper mapper = new ObjectMapper(factory);

		JsonNode jsonNode = mapper.readTree(UtilitiesFactory.class.getResourceAsStream("/" + jsonFilename + ".json"));

		T targetObject = mapper.convertValue(jsonNode, targetType);

		return targetObject;
	}

	/**
	 * To Json
	 * 
	 * @param source
	 */
	public <T> String toJsonString(T source) {
		JsonFactory jsonFactory = new JsonFactory();
		ObjectMapper mapper = new ObjectMapper(jsonFactory);
		try {
			return mapper.writeValueAsString(source);
		} catch (JsonProcessingException jpexc) {
			jpexc.printStackTrace();
		}
		return null;
	}

	/**
	 * Get configuration
	 * 
	 * @param property
	 */
	public String getConfig(String property) {
		return properties.getProperty(property);
	}

	/**
	 * Set configuration
	 * 
	 * @param key
	 * @param value
	 */
	public void setConfig(String key, String value) {
		properties.put(key, value);
	}

	/**
	 * Set field names and values of each element from source to each element in
	 * destination
	 * 
	 * @param sourceList
	 * @param destinationList
	 */
	@SuppressWarnings("unchecked")
	public <T> void copy(List<T> sourceList, List<T> destinationList) {

		if (sourceList != null && destinationList != null && !sourceList.isEmpty() && !destinationList.isEmpty()) {
			T source = null;
			T destination = null;

			for (int index = 0; index < sourceList.size(); index++) {
				source = sourceList.get(index);
				try {
					destination = (T) destination.getClass().newInstance();
					copy(source, destination);
					destinationList.add(destination);
				} catch (Exception exc) {
					exc.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * Set the field names and values
	 * 
	 * @param <T>
	 * @param sourceFields - source fields
	 * @param source - source
	 * @param destination - destination
	 * @param mapFieldConversion - map field conversion
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	@SuppressWarnings("rawtypes")
	public <T> void copy(T source, T destination) {
		Class clsSource = source.getClass();
		while (clsSource != null && !clsSource.getName().equals("java.lang.Object")) {
			copy(clsSource.getDeclaredFields(), source, destination, null);
			clsSource = clsSource.getSuperclass();
		}
	}
	
	/**
	 * 
	 * @param source - source fields
	 * @param destination - destination fields
	 * @param exceptionsMap - list of field names that are exempted from copy
	 */
	@SuppressWarnings("rawtypes")
	public <T> void copy(T source, T destination, GenericCache<String, String> exceptionsMap) {
		Class clsSource = source.getClass();
		while (clsSource != null && !clsSource.getName().equals("java.lang.Object")) {
			copy(clsSource.getDeclaredFields(), source, destination, exceptionsMap);
			clsSource = clsSource.getSuperclass();
		}
	}
	
	/**
	 * 
	 * @param classSource
	 * @return
	 */
	public <Type> Field[] getDeclaredFields(Class<Type> classSource) {
		return classSource.getDeclaredFields();
	}
	
	/**
	 * 
	 * @param object1
	 * @param object2
	 * @return
	 */
	public <Type> boolean areObjectsNull(Type object1, Type object2) {
		return (object1 == null && object1 == object2);
	}
	
	/**
	 * 
	 * @param object1
	 * @param object2
	 * @return
	 */
	public <Type> boolean areObjectsNotNull(Type object1, Type object2) {
		return (object1 != null && object1 != object2);
	}

	// /**
	// * Assumes that non-object and non-atomic fields of the object are just list
	// for now
	// * @param source
	// * @param destination
	// */
	// public <T> void duplicateUsingReflection(T source, T destination) {
	// if (source != null && destination != null
	// && source.getClass() == destination.getClass()) {
	// Class classType = source.getClass();
	// Field[] fields = classType.getDeclaredFields();
	// }
	// }

	/**
	 * Set the field names and values
	 * 
	 * @param <T>
	 * @param sourceFields - source fields
	 * @param source - source
	 * @param destination - destination
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private <T> void copy(Field[] sourceFields, T source, T destination, GenericCache<String, String> exceptionsMap) {

		Field sourceField = null;
		Field destinationField = null;
		T value;
		Class clsDestination;
		for (int count = 0; count < sourceFields.length; count++) {
			try {
				sourceField = sourceFields[count];
				String fieldName = sourceField.getName();
				
				if (fieldName != null && !fieldName.isEmpty()) {
					if (exceptionsMap != null && exceptionsMap.getMap().containsKey(fieldName)) {
						//skip and go to the next field
						continue;
					}
				}
				sourceField.setAccessible(true);
				value = (T) sourceField.get(source);

				clsDestination = destination.getClass();
				while (clsDestination != null && !clsDestination.getName().equals("java.lang.Object")) {
					try {
						destinationField = clsDestination.getDeclaredField(fieldName);
						break;
					} catch (NoSuchFieldException | NullPointerException nsfenspe) {
						clsDestination = clsDestination.getSuperclass();
					}
				}

				if (value != null && destinationField != null) {
					destinationField.setAccessible(true);
					if (!value.getClass().equals(destinationField.getType())) {
						if (value instanceof String) {
							BigDecimal convertedValue = new BigDecimal(((String) value).replace(",", ""));
							if (destinationField.getType().equals(BigDecimal.class)) {
								destinationField.set(destination, convertedValue);
							} else if (destinationField.getType().equals(Integer.class)) {
								destinationField.set(destination, convertedValue.intValue());
							} else if (destinationField.getType().equals(Float.class)) {
								destinationField.set(destination, convertedValue.doubleValue());
							}
						} else if (value instanceof java.sql.Date) {
							if (destinationField.getType().equals(Timestamp.class)) {
								destinationField.set(destination, new Timestamp(((java.sql.Date) value).getTime()));
							}
						} else if (value instanceof Timestamp) {
							if (destinationField.getType().equals(java.sql.Date.class)) {
								destinationField.set(destination, new java.sql.Date(((Timestamp) value).getTime()));
							}
						}
					} else {
						destinationField.set(destination, value);
					}

				}
			} catch (Exception exc) {
				sourceField = destinationField = null;
			}
		}
	}

// //the following won't be useful until protobuff and gRPC are used
//	/**
//	 * Serialize
//	 * 
//	 * @param <T>
//	 * @param source
//	 */
//	public <T> ByteString serialize(T source) {
//		return ByteString.copyFrom(SerializationUtils.serialize(source));
//	}
//
//	/**
//	 * Deserialize
//	 * 
//	 * @param <T>
//	 * @param <T>
//	 * @param source
//	 */
//	@SuppressWarnings("unchecked")
//	public <T> T deserialize(ByteString source) {
//		return (T) SerializationUtils.deserialize(source.toByteArray());
//	}
	
	/**
	 * Checks if obj is an instance of either HibernateProxy or PersistentCollection e.g. javassist
	 * @param object
	 * @return
	 */
	public <Type> boolean isLazyLoaded(Type object) {
		boolean result = false;
		
		if (object != null && (object instanceof HibernateProxy || object instanceof PersistentCollection)) {
			result = true;
		}
		
		return result;
	}
	
	/**
	 * Converts a proxy object to its original object type (form)
	 * @param source - the object that was queried but in proxy form
	 * @return Type - the type of the desired object
	 * 				  else null if the source object is not a proxy object
	 */
	@SuppressWarnings("unchecked")
	public <SourceType, DestinationType> DestinationType loadLazyLoadedObject(SourceType source, Class<DestinationType> destinationType) {
		DestinationType destination = null;
		
		if (source != null && this.isLazyLoaded(source)) {
			Hibernate.initialize((HibernateProxy) source);
			destination = (DestinationType) ((HibernateProxy) source).getHibernateLazyInitializer().getImplementation();
		} else if (source != null && destinationType.isInstance(source)) {
			destination = (DestinationType) source;
		}
		
		return destination;
	}
	
	/**
	 * 
	 * @param source
	 * @param excpectedClassType
	 * @return
	 */
	public <ExpectedType> ExpectedType loadLazilyLoaded(ExpectedType source, Class<ExpectedType> expectedClassType) {
		ExpectedType instanceReference = null;
		
		if (source != null && expectedClassType != null) {
			//where this here can be a java assist kind of object which a variant of Type
			String expectedClassName = expectedClassType.getName();
			String actualClassName = source.getClass().getName();
			if (!expectedClassName.equals(actualClassName)) {
				instanceReference = UtilitiesFactory.factory().loadLazyLoadedObject(source, expectedClassType);
			} else {
				instanceReference = source;
			}
		}
		
		return instanceReference;
	}
	
	/**
	 * 
	 * @param sources
	 * @param excpectedClassType
	 * @return
	 */
	public <ExpectedType> List<ExpectedType> loadLazilyLoaded(List<ExpectedType> sources, Class<ExpectedType> excpectedClassType) {
		List<ExpectedType> instanceReferences = null;
		
		if (sources != null && !sources.isEmpty()) {
			instanceReferences = new ArrayList<ExpectedType>();
			final List<ExpectedType> instanceReferencesLocal = instanceReferences;
			
			sources.forEach(source -> {
				instanceReferencesLocal.add(UtilitiesFactory.factory().loadLazilyLoaded(source, excpectedClassType));
			});
		}
		
		return instanceReferences;
	}
	
	/**
	 * Checks if the list is null or empty
	 * @param list
	 * @return true if its either null or empty otherwise it returns true
	 */
	public <Type> boolean isNullOrEmpty(List<Type> list) {
		return (list == null || list.isEmpty());
	}
	
	public String getModuleName() {
		String moduleName = null;
		
		try {
			moduleName = (String) (new InitialContext()).lookup("java:module/ModuleName");
		} catch (Exception exception) {
			UtilitiesFactory.logger.error("Error retrieving module name: " + exception.getMessage());
		}
		
		return moduleName;
	}
	
	public String getApplicationName() {
		String applicationName = null;
		
		try {
			applicationName = (String) (new InitialContext()).lookup("java:app/AppName");
		} catch (Exception exception) {
			UtilitiesFactory.logger.error("Error retrieving application name: " + exception.getMessage());
		}
		
		return applicationName;
	}
	
	public int generateRandomNumber(int limit) {
		String methodName = "generateRandomNumber()";
		UtilitiesFactory.logger.info(methodName + " {");
		
		UtilitiesFactory.logger.info("Generating a random integer within the range 0.." + limit + ".");
	    
	    //note a single Random object is reused here
	    Random randomGenerator = new Random();
	    int randomInt = randomGenerator.nextInt(limit);
	    UtilitiesFactory.logger.info("Generated : " + randomInt);
	    
	    UtilitiesFactory.logger.info("} " + methodName);
	    return randomInt;
	}
	
	public <Type> byte[] serializeToByteArray(Type sourceObject) {
		byte[] serializedBytes = null;
		String methodName = "serializeToByteArray()";
		
		if (sourceObject != null) {
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			ObjectOutput objectOutput = null;
			
			try {
				objectOutput = new ObjectOutputStream(byteArrayOutputStream);
				objectOutput.writeObject(sourceObject);
				objectOutput.flush();
				serializedBytes = byteArrayOutputStream.toByteArray();
			} catch (Exception exception) {
				UtilitiesFactory.logger.error(methodName + ": Exception during serialization/deserialization!");
				exception.printStackTrace();
			} finally {
				try {
					if (objectOutput != null) {
						objectOutput.close();
					}
				} catch (Exception exception) {
					UtilitiesFactory.logger.error(methodName + ": Unable to close objectOutput!");
					exception.printStackTrace();
				}
			}
		}
		
		return serializedBytes;
	}
	
	@SuppressWarnings({"unchecked"})
	public <Type> Type deserializeFromByteArray(byte[] serializedBytes) {
		Type destinationObject = null;
		String methodName = "deserializeFromByteArray()";
		
		if (serializedBytes != null && serializedBytes.length > 0) {
			ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(serializedBytes);
			ObjectInput objectInput = null;
			
			try {
				objectInput = new ObjectInputStream(byteArrayInputStream);
				destinationObject = (Type) objectInput.readObject();
			} catch (Exception exception) {
				UtilitiesFactory.logger.error(methodName + ": Exception during serialization/deserialization!");
				exception.printStackTrace();
			} finally {
				try {
					if (objectInput != null) {
						objectInput.close();
					}
				} catch (Exception exception) {
					UtilitiesFactory.logger.error(methodName + ": Unable to close objectInput!");
					exception.printStackTrace();
				}
			}
		}
		
		return destinationObject;
	}
}


